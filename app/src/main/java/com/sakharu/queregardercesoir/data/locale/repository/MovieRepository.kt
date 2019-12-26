package com.sakharu.queregardercesoir.data.locale.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.sqlite.db.SimpleSQLiteQuery
import com.sakharu.queregardercesoir.data.locale.AppDatabase
import com.sakharu.queregardercesoir.data.locale.dao.MovieDAO
import com.sakharu.queregardercesoir.data.locale.dao.MovieInCategoryDAO
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService
import com.sakharu.queregardercesoir.util.*


object MovieRepository
{
    private lateinit var database: AppDatabase
    private lateinit var movieDAO: MovieDAO
    private lateinit var movieInCategoryDAO: MovieInCategoryDAO

    private val movieService = MovieService.create()

    fun initialize(application: Application)
    {
        database = AppDatabase.buildInstance(application)
        movieDAO = database.movieDAO()
        movieInCategoryDAO = database.movieInCategoryDAO()
    }

    /***********************
     *  REGION LOCALE
     **********************/

    /*
        MOVIE
     */
    suspend fun insertAllMovies(movies: List<Movie>) = movieDAO.insertAll(movies)

    suspend fun insertMovie(movie: Movie) = insertAllMovies(listOf(movie))

    fun getMovieLiveById(id: Long): LiveData<Movie> = movieDAO.getById(id)

    /*
        MOVIEINCATEGORY
     */

    suspend fun insertMovieListInCategory(idMovieList : List<Long>, categoryId:Long, page: Int)
    {
        for (i in idMovieList.indices)
            movieInCategoryDAO.insert(MovieInCategory(null, categoryId, idMovieList[i],i+ MovieService.NUMBER_MOVIES_RETRIEVE_BY_REQUEST*(page-1),page))
    }

    fun getAllMoviesInCategoryLive() : LiveData<List<MovieInCategory>>
            = movieInCategoryDAO.getAllMoviesInCategory()

    fun getMoviesFromListIdLive(popularMoviesListId:List<Long>): LiveData<List<Movie>>
            = movieDAO.getMoviesByListId(popularMoviesListId)

    fun getMovieInCategoryLive(id:Long, page:MutableLiveData<Int>): LiveData<List<MovieInCategory>> =
        Transformations.switchMap(page) {
            movieInCategoryDAO.getMoviesIdFromCategoryId(id,it)
        }


    fun getFirstMoviesInCategoryFromCategoryId(id:Long): LiveData<List<MovieInCategory>> =
        movieInCategoryDAO.getFirstMoviesIdFromCategoryID(id)

    fun getMoviesFromTitleSearch(search:MutableLiveData<String>)
            = Transformations.switchMap(search) {
        movieDAO.getMoviesFromTitleSearch(it)
    }

    fun getMoviesFromCharacSearch(sortBy: String, voteAverageGte: Double = 0.0, genresId: List<String> = listOf(),
                                  yearGte: String? = null, yearLte: String? = null, yearDuring: Int? = null,
                                  year: Int?, certification: String?):LiveData<List<Movie>>
    {
        //SI LES DONNEES SONT PASSEES ON LES AJOUTE A LA REQUETE
        val yearGteQuery = if (yearGte!=null) "AND releaseYear>$year" else ""
        val yearLteQuery = if (yearLte!=null) "AND releaseYear<$year" else ""
        val yearDuringQuery = if (yearDuring!=null) "AND releaseYear=$year" else ""
        val certificationQuery = if (certification!=null) "AND certification='$certification'" else ""

        //ON RAJOUTE A LA REQUETE L'ORDONNENCEMENT EN FONCTION DE CE QUI A ETE PASSE EN PARAMETRE
        val sortByQuery = when (sortBy)
        {
            POPULARITYDESC -> "popularity DESC"
            RELEASEDATEDESC -> "releaseYear DESC"
            else -> "vote_average DESC"
        }

        /*ON CONSTRUIT LA PARTIE DES GENRES DE LA REQUETES GRACE A LA CONCATENATION
        ON PARCOURT LA LISTE DES ID DE GENRES SELECTIONNES ET ON L'AJOUTE A LA REQUETE
        EXEMPLE DE REQUETE
            SELECT * FROM movie WHERE vote_average>3.0 AND genresId LIKE '%' || 28  || '%' AND genresId LIKE '%' ||  16  || '%'
             AND releaseYear<2020  ORDER BY popularity DESC
         */
        val  genresQuery = if (genresId.any { it.isNotEmpty() })
        {
            var andGenres = "AND"
            for (id in genresId)
                andGenres+=" genresId LIKE '%' || $id  || '%' AND"
            andGenres.removeSuffix("AND")
        }
        else
            ""

        val query = "SELECT * FROM movie WHERE vote_average>$voteAverageGte"  +
                " $genresQuery $yearGteQuery $yearLteQuery $yearDuringQuery $certificationQuery" +
                " ORDER BY $sortByQuery"
        return movieDAO.getMoviesFromCharacRaw(SimpleSQLiteQuery(query))
    }


    fun getSuggestedMoviesFromGenres(genresId: List<Long>,orderBy:String="RANDOM()",limit:String="LIMIT 20"):LiveData<List<Movie>>
    {
        /*ON CONSTRUIT LA PARTIE DES GENRES DE LA REQUETES GRACE A LA CONCATENATION
        ON PARCOURT LA LISTE DES ID DE GENRES SELECTIONNES ET ON L'AJOUTE A LA REQUETE
        EXEMPLE DE REQUETE
        SELECT * FROM movie WHERE vote_average>3.0 AND genresId LIKE '%' || 28  || '%' OR genresId LIKE '%' ||  16  || '%'
         */

        var orGenres = ""
        for (id in genresId)
            orGenres+=" genresId LIKE '%' || $id  || '%' OR"
        orGenres = orGenres.removeSuffix("OR")

        //le AND est prioritaire sur le OR donc on rajoute des parenthèses
        val query = "SELECT * FROM movie WHERE ($orGenres) AND isSuggested=1 ORDER BY $orderBy $limit"
        return movieDAO.getMoviesFromCharacRaw(SimpleSQLiteQuery(query))
    }

    private fun addYearAndCertificationsToMovies(movies: List<Movie>, certification: String?=null) : List<Movie>
    {
        for (movie in movies)
        {
            movie.certification=certification
            addYearToMovie(movie)
        }
        return movies
    }

    private fun addYearToMovie(movie:Movie) : Movie
    {
        if (!movie.releaseDate.isNullOrEmpty())
            try { movie.releaseYear=movie.releaseDate?.substring(0,4)?.toInt() }
            catch (e:Exception){e.printStackTrace()}
        return movie
    }

    /***********************
     *  REGION REMOTE
     **********************/

    suspend fun downloadTopRatedMovies(page:Int=1) : Int
    {
        return try
        {
            val topRatedMovies = movieService.getTopRatedMovies(page = page)
            insertAllMovies(addYearAndCertificationsToMovies(topRatedMovies.results))
            insertMovieListInCategory(topRatedMovies.results.map { it.id }, CATEGORY_TOPRATED_ID,page)
            topRatedMovies.total_pages
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            ERROR_CODE
        }

    }

    suspend fun downloadNowPlayingMovies(page:Int=1) : Int
    {
        return try
        {
            val newPlayingMovies = movieService.getNowPlayingMovies(page = page)
            insertAllMovies(addYearAndCertificationsToMovies(newPlayingMovies.results))
            insertMovieListInCategory(newPlayingMovies.results.map { it.id }, CATEGORY_NOWPLAYING_ID,page)
            newPlayingMovies.total_pages
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            ERROR_CODE
        }
    }

    suspend fun downloadTrendingMovies(page:Int=1) : Int
    {
        return try
        {
            val upcomingMovies = movieService.getTrendingMovies(page = page)
            insertAllMovies(addYearAndCertificationsToMovies(upcomingMovies.results))
            insertMovieListInCategory(upcomingMovies.results.map { it.id }, CATEGORY_TRENDING_ID,page)
            return upcomingMovies.total_pages
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            ERROR_CODE
        }
    }

    suspend fun downloadMovieDetail(id:Long,isSuggested:Boolean?=null) : Int?
    {
        return try
        {
            val movieResult = movieService.getMovieDetail(id = id)
            val movie = Movie(movieResult.id,movieResult.title,movieResult.genres.map { it.id },movieResult.overview,
                movieResult.popularity,movieResult.posterImg,movieResult.backdropImg,movieResult.releaseDate,
                movieResult.original_title,movieResult.vote_average,movieResult.budget,movieResult.vote_count,
                null,null,isSuggested)
            insertMovie(addYearToMovie(movie))
            GenreRepository.insertAllGenre(movieResult.genres)
            null
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            ERROR_CODE
        }
    }

    suspend fun searchMovieFromQuery(query : String, page: Int=1) : Int
    {
        return try
        {
            val movieResult = movieService.searchMovieFromQuery(query = query,page = page)
            insertAllMovies( addYearAndCertificationsToMovies(movieResult.results))
            return movieResult.total_pages
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            ERROR_CODE
        }
    }

    suspend fun searchMovieFromCharacteristics(page:Int=1,sortBy:String?=null,voteAverageGte:Double?=null, withGenres:String?=null,
                                               releaseDateGte:String?=null,releaseDateLte:String?=null,year:Int?=null,
                                               certificationCountry:String?=null, certification:String?=null): Int {
        return try
        {
            val returnResult = movieService.searchMovieFromCharacs(
                page = page,
                sortBy = sortBy,
                releaseDateGte = releaseDateGte,
                releaseDatelte = releaseDateLte,
                voteAverageGte = voteAverageGte,
                withGenres = withGenres,
                year=year,
                certificationCountry = certificationCountry,
                certification = certification)
            val movieResult = returnResult.results
            insertAllMovies(addYearAndCertificationsToMovies(movieResult,certification))
            return returnResult.total_pages
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            ERROR_CODE
        }
    }

    suspend fun downloadSimilarMovies(movieId:Long) : List<Long>
    {
        return try
        {
            val similarMovies = movieService.getSimilarMoviesFromMovieId(movieId).results
            insertAllMovies(similarMovies)
            similarMovies.map { it.id }
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            arrayListOf(ERROR_CODE.toLong())
        }
    }

    suspend fun downloadSuggestedMovies(page:Int=1, withGenres:String?=null): Int
    {
        return try
        {
            val returnResult = movieService.searchForSuggestedMovies(page = page, withGenres = withGenres)
            val movieResult = returnResult.results
            movieResult.map { it.isSuggested=true }
            insertAllMovies(addYearAndCertificationsToMovies(movieResult))
            return returnResult.total_pages
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            ERROR_CODE
        }
    }
}





