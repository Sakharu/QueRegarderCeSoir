package com.sakharu.queregardercesoir.data.locale.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sakharu.queregardercesoir.data.locale.AppDatabase
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.dao.CategoryDAO
import com.sakharu.queregardercesoir.data.locale.dao.MovieDAO
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.locale.dao.MovieInCategoryDAO
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.MovieService
import com.sakharu.queregardercesoir.util.*


object MovieRepository
{
    private lateinit var database: AppDatabase
    private lateinit var movieDAO: MovieDAO
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var movieInCategoryDAO: MovieInCategoryDAO

    private val service = MovieService.create()

    fun initialize(application: Application)
    {
        database = AppDatabase.buildInstance(application)
        movieDAO = database.movieDAO()
        categoryDAO = database.categoryDAO()
        movieInCategoryDAO = database.movieInCategoryDAO()
    }

    //region locale

    suspend fun insertAll(movies: List<Movie>) = movieDAO.insertAll(movies)

    suspend fun insertCategory(id:Long,name:String) = categoryDAO.insert(Category(id, name))

    suspend fun insert(movie: Movie) = insertAll(listOf(movie))

    suspend fun insertMovieListInCategory(idMovieList : List<Long>, categoryId:Long)
    {
        for(idMovie in idMovieList)
            movieInCategoryDAO.insert(MovieInCategory(null, categoryId, idMovie))
    }

    fun getById(id: Long): LiveData<Movie> = movieDAO.getById(id)

    fun getPopularMovies(popularMoviesListId:List<Long>): LiveData<List<Movie>>
            = movieDAO.getMoviesByListId(popularMoviesListId)

    fun getTopRatedMovies(topRatedMoviesListId:List<Long>): LiveData<List<Movie>>
            = movieDAO.getMoviesByListId(topRatedMoviesListId)

    fun getNowPlayingMovies(nowPlayingMoviesListId:List<Long>): LiveData<List<Movie>>
            = movieDAO.getMoviesByListId(nowPlayingMoviesListId)

    fun getUpcomingMovies(upcomingMoviesListId:List<Long>): LiveData<List<Movie>>
            = movieDAO.getMoviesByListId(upcomingMoviesListId)

    fun getMovieInCategory(id:Long): LiveData<List<MovieInCategory>> =
        movieInCategoryDAO.getMoviesIdFromCategoryId(id)

    fun getAllCategories() : LiveData<List<Category>> = categoryDAO.getAllCategories()

    //endregion

    //region remote

    suspend fun downloadPopularMovies()
    {
        val lastPopularMovies = service.getPopularMovies()
        val idCategory = insertCategory(CATEGORY_POPULAR_ID, CATEGORY_POPULAR_NAME)
        insertAll(lastPopularMovies.results)
        insertMovieListInCategory(lastPopularMovies.results.map { it.id }, idCategory)
    }

    suspend fun downloadTopRatedMovies()
    {
        val topRatedMovies = service.getTopRatedMovies()
        val idCategory = insertCategory(CATEGORY_TOPRATED_ID, CATEGORY_TOPRATED_NAME)
        insertAll(topRatedMovies.results)
        insertMovieListInCategory(topRatedMovies.results.map { it.id }, idCategory)
    }

    suspend fun downloadNowPlayingMovies()
    {
        val newPlayingMovies = service.getNowPlayingMovies()
        val idCategory = insertCategory(CATEGORY_NOWPLAYING_ID, CATEGORY_NOWPLAYING_NAME)
        insertAll(newPlayingMovies.results)
        insertMovieListInCategory(newPlayingMovies.results.map { it.id }, idCategory)
    }

    suspend fun downloadUpcomingMovies()
    {
        val upcomingMovies = service.getUpcomingMovies()
        val idCategory = insertCategory(CATEGORY_UPCOMING_ID, CATEGORY_UPCOMING_NAME)
        insertAll(upcomingMovies.results)
        insertMovieListInCategory(upcomingMovies.results.map { it.id }, idCategory)
    }
}





