package com.sakharu.queregardercesoir.data.locale.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.sakharu.queregardercesoir.data.locale.AppDatabase
import com.sakharu.queregardercesoir.data.locale.dao.MovieDAO
import com.sakharu.queregardercesoir.data.locale.dao.MovieInCategoryDAO
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService
import com.sakharu.queregardercesoir.ui.movieList.MovieListViewModel
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
        val timeStamp = System.currentTimeMillis()
        for (i in idMovieList.indices)
            movieInCategoryDAO.insert(MovieInCategory(null, categoryId, idMovieList[i],i+ MovieService.NUMBER_MOVIES_RETRIEVE_BY_REQUEST*(page-1),page,timeStamp))
        MovieListViewModel.lastTimeStamp = timeStamp
    }

    fun getAllMoviesInCategoryLive() : LiveData<List<MovieInCategory>>
            = movieInCategoryDAO.getAllMoviesInCategory()

    fun getMoviesFromListIdLive(popularMoviesListId:List<Long>): LiveData<List<Movie>>
            = movieDAO.getMoviesByListId(popularMoviesListId)

    fun getMovieInCategoryLive(id:Long, lastTimestamp:Long=0): LiveData<List<MovieInCategory>> =
        movieInCategoryDAO.getMoviesIdFromCategoryId(id,lastTimestamp)

    fun getFirstMoviesInCategoryFromCategoryId(id:Long): LiveData<List<MovieInCategory>> =
        movieInCategoryDAO.getFirstMoviesIdFromCategoryID(id)

    fun deleteDeprecatedMoviesInCategory(listId : List<Long>)
            = movieInCategoryDAO.deleteOldMoviesInCategory(listId)

    fun getMoviesFromTitleSearch(search:String)
            = movieDAO.getMoviesFromTitleSearch(search)

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


    fun getSuggestedMoviesFromGenres(genresId: List<Long>):LiveData<List<Movie>>
    {
        /*ON CONSTRUIT LA PARTIE DES GENRES DE LA REQUETES GRACE A LA CONCATENATION
        ON PARCOURT LA LISTE DES ID DE GENRES SELECTIONNES ET ON L'AJOUTE A LA REQUETE
        EXEMPLE DE REQUETE
        SELECT * FROM movie WHERE vote_average>3.0 AND genresId LIKE '%' || 28  || '%' OR genresId LIKE '%' ||  16  || '%'  ORDER BY popularity DESC
         */

        var orGenres = ""
        for (id in genresId)
            orGenres+=" genresId LIKE '%' || $id  || '%' OR"
        orGenres = orGenres.removeSuffix("OR")

        val query = "SELECT * FROM movie WHERE $orGenres ORDER BY RANDOM()"
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
        val topRatedMovies = movieService.getTopRatedMovies(page = page)
        val idCategory = CategoryRepository.insertCategory(
            CATEGORY_TOPRATED_ID,
            CATEGORY_TOPRATED_NAME
        )
        insertAllMovies(addYearAndCertificationsToMovies(topRatedMovies.results))
        insertMovieListInCategory(topRatedMovies.results.map { it.id }, idCategory,page)
        return topRatedMovies.total_pages
    }

    suspend fun downloadNowPlayingMovies(page:Int=1) : Int
    {
        val newPlayingMovies = movieService.getNowPlayingMovies(page = page)
        val idCategory = CategoryRepository.insertCategory(
            CATEGORY_NOWPLAYING_ID,
            CATEGORY_NOWPLAYING_NAME
        )
        insertAllMovies(addYearAndCertificationsToMovies(newPlayingMovies.results))
        insertMovieListInCategory(newPlayingMovies.results.map { it.id }, idCategory,page)
        return newPlayingMovies.total_pages
    }

    suspend fun downloadTrendingMovies(page:Int=1) : Int
    {
        val upcomingMovies = movieService.getTrendingMovies(page = page)
        val idCategory = CategoryRepository.insertCategory(
            CATEGORY_TRENDING_ID,
            CATEGORY_TRENDING_NAME
        )
        insertAllMovies(addYearAndCertificationsToMovies(upcomingMovies.results))
        insertMovieListInCategory(upcomingMovies.results.map { it.id }, idCategory,page)
        return upcomingMovies.total_pages
    }

    suspend fun downloadMovieDetail(id:Long)
    {
        val movieResult = movieService.getMovieDetail(id = id)
        val movie = Movie(movieResult.id,movieResult.title,movieResult.genres.map { it.id },movieResult.overview,
            movieResult.popularity,movieResult.posterImg,movieResult.backdropImg,movieResult.releaseDate,
            movieResult.original_title,movieResult.vote_average,movieResult.budget,movieResult.vote_count,
            null,null)
        insertMovie(addYearToMovie(movie))
        GenreRepository.insertAllGenre(movieResult.genres)
    }

    suspend fun searchMovieFromQuery(query : String, page: Int=1) : Int
    {
        val movieResult = movieService.searchMovieFromQuery(query = query,page = page)
        insertAllMovies( addYearAndCertificationsToMovies(movieResult.results))
        return movieResult.total_pages
    }

    suspend fun searchMovieFromCharacteristics(page:Int=1,sortBy:String?=null,voteAverageGte:Double?=null, withGenres:String?=null,
                                               releaseDateGte:String?=null,releaseDateLte:String?=null,year:Int?=null,
                                               certificationCountry:String?=null, certification:String?=null): Int {
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

    suspend fun downloadSimilarMovies(movieId:Long) : List<Long>
    {
        val similarMovies = movieService.getSimilarMoviesFromMovieId(movieId).results
        insertAllMovies(similarMovies)
        return similarMovies.map { it.id }
    }

    suspend fun downloadSuggestedMovies(page:Int=1,sortBy:String?=null, withGenres:String?=null): Int
    {
        val returnResult = movieService.searchForSuggestedMovies(
            page = page,
            sortBy = sortBy,
            withGenres = withGenres)
        val movieResult = returnResult.results
        insertAllMovies(addYearAndCertificationsToMovies(movieResult))
        return returnResult.total_pages
    }
}





