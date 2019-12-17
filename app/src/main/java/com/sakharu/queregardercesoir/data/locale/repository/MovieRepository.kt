package com.sakharu.queregardercesoir.data.locale.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sakharu.queregardercesoir.data.locale.AppDatabase
import com.sakharu.queregardercesoir.data.locale.dao.CategoryDAO
import com.sakharu.queregardercesoir.data.locale.dao.GenreDAO
import com.sakharu.queregardercesoir.data.locale.dao.MovieDAO
import com.sakharu.queregardercesoir.data.locale.dao.MovieInCategoryDAO
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService
import com.sakharu.queregardercesoir.ui.movieList.MovieListCategoryViewModel
import com.sakharu.queregardercesoir.util.*


object MovieRepository
{
    private lateinit var database: AppDatabase
    private lateinit var movieDAO: MovieDAO
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var movieInCategoryDAO: MovieInCategoryDAO
    private lateinit var genreDAO: GenreDAO

    private val movieService = MovieService.create()

    fun initialize(application: Application)
    {
        database = AppDatabase.buildInstance(application)
        movieDAO = database.movieDAO()
        categoryDAO = database.categoryDAO()
        movieInCategoryDAO = database.movieInCategoryDAO()
        genreDAO = database.genreDAO()
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
        CATEGORY
     */

    suspend fun insertCategory(id:Long,name:String,overview:String) = categoryDAO.insert(Category(id, name, overview))

    fun getAllCategoriesLive() : LiveData<List<Category>> = categoryDAO.getAllCategories()



    /*
        MOVIEINCATEGORY
     */

    suspend fun insertMovieListInCategory(idMovieList : List<Long>, categoryId:Long, page: Int)
    {
        for (i in idMovieList.indices)
        {
            val timeStamp = System.currentTimeMillis()
            movieInCategoryDAO.insert(MovieInCategory(null, categoryId, idMovieList[i],i+ MovieService.NUMBER_MOVIES_RETRIEVE_BY_REQUEST*page,timeStamp))
            MovieListCategoryViewModel.lastTimeStamp = timeStamp
        }
    }

    fun getAllMoviesInCategoryLive() : LiveData<List<MovieInCategory>>
            = movieInCategoryDAO.getAllMoviesInCategory()

    fun getMoviesFromCategoryLive(popularMoviesListId:List<Long>): LiveData<List<Movie>>
            = movieDAO.getMoviesByListId(popularMoviesListId)

    fun getMovieInCategoryLive(id:Long, lastTimestamp:Long=0): LiveData<List<MovieInCategory>> =
        movieInCategoryDAO.getMoviesIdFromCategoryId(id,lastTimestamp)

    fun getFirstMoviesInCategoryFromCategoryId(id:Long): LiveData<List<MovieInCategory>> =
        movieInCategoryDAO.getFirstMoviesIdFromCategoryID(id)

    fun deleteDeprecatedMoviesInCategory(listId : List<Long>)
            = movieInCategoryDAO.deleteOldMoviesInCategory(listId)

    fun getMoviesFromTitleSearch(search:String)
            = movieDAO.getMoviesFromTitleSearch(search)

    /*
        GENRE
     */
    suspend fun insertAllGenre(genreList:List<Genre>) = genreDAO.insertAll(genreList)

    fun getGenresFromMovie(movie: Movie) = genreDAO.getGenreFromListId(movie.genresId)


    /***********************
     *  REGION REMOTE
     **********************/

    suspend fun downloadPopularMovies(page:Int=1)
    {
        val lastPopularMovies = movieService.getPopularMovies(page = page)
        val idCategory = insertCategory(CATEGORY_POPULAR_ID, CATEGORY_POPULAR_NAME,
            CATEGORY_POPULAR_OVERVIEW)
        insertAllMovies(lastPopularMovies.results)
        insertMovieListInCategory(lastPopularMovies.results.map { it.id }, idCategory,page)
    }

    suspend fun downloadTopRatedMovies(page:Int=1)
    {
        val topRatedMovies = movieService.getTopRatedMovies(page = page)
        val idCategory = insertCategory(CATEGORY_TOPRATED_ID, CATEGORY_TOPRATED_NAME,
            CATEGORY_TOPRATED_OVERVIEW)
        insertAllMovies(topRatedMovies.results)
        insertMovieListInCategory(topRatedMovies.results.map { it.id }, idCategory,page)
    }

    suspend fun downloadNowPlayingMovies(page:Int=1)
    {
        val newPlayingMovies = movieService.getNowPlayingMovies(page = page)
        val idCategory = insertCategory(CATEGORY_NOWPLAYING_ID, CATEGORY_NOWPLAYING_NAME,
        CATEGORY_NOWPLAYING_OVERVIEW)
        insertAllMovies(newPlayingMovies.results)
        insertMovieListInCategory(newPlayingMovies.results.map { it.id }, idCategory,page)
    }

    suspend fun downloadUpcomingMovies(page:Int=1)
    {
        val upcomingMovies = movieService.getUpcomingMovies(page = page)
        val idCategory = insertCategory(CATEGORY_UPCOMING_ID, CATEGORY_UPCOMING_NAME,
            CATEGORY_UPCOMING_OVERVIEW)
        insertAllMovies(upcomingMovies.results)
        insertMovieListInCategory(upcomingMovies.results.map { it.id }, idCategory,page)
    }

    suspend fun downloadMovieDetail(id:Long)
    {
        val movieResult = movieService.getMovieDetail(id = id)
        val movie = Movie(movieResult.id,movieResult.title,movieResult.genres.map { it.id },movieResult.overview,
            movieResult.popularity,movieResult.posterImg,movieResult.backdropImg,movieResult.releaseDate,
            movieResult.original_title,movieResult.vote_average,movieResult.budget,movieResult.vote_count)
        insertMovie(movie)
        insertAllGenre(movieResult.genres)
    }

    suspend fun searchMovie(query : String)
    {
        val movieResult = movieService.searchMovieFromQuery(query = query)
        insertAllMovies(movieResult.results)
    }
}





