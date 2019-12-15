package com.sakharu.queregardercesoir.data.locale.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sakharu.queregardercesoir.data.locale.AppDatabase
import com.sakharu.queregardercesoir.data.locale.dao.CategoryDAO
import com.sakharu.queregardercesoir.data.locale.dao.MovieDAO
import com.sakharu.queregardercesoir.data.locale.dao.MovieInCategoryDAO
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.remote.MovieService
import com.sakharu.queregardercesoir.ui.home.category.detail.DetailCategoryViewModel
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

    suspend fun insertCategory(id:Long,name:String,overview:String) =
        categoryDAO.insert(Category(id, name, overview))

    suspend fun insert(movie: Movie) = insertAll(listOf(movie))

    suspend fun insertMovieListInCategory(idMovieList : List<Long>, categoryId:Long, page: Int)
    {
        for (i in idMovieList.indices)
        {
            val timeStamp = System.currentTimeMillis()
            movieInCategoryDAO.insert(MovieInCategory(null, categoryId, idMovieList[i],i+MovieService.NUMBER_MOVIES_RETRIEVE_BY_REQUEST*page,timeStamp))
            DetailCategoryViewModel.lastTimeStamp = timeStamp
        }

    }

    fun getById(id: Long): LiveData<Movie> = movieDAO.getById(id)

    fun getMoviesFromCategory(popularMoviesListId:List<Long>): LiveData<List<Movie>>
            = movieDAO.getMoviesByListId(popularMoviesListId)

    fun getMovieInCategory(id:Long, lastTimestamp:Long=0): LiveData<List<MovieInCategory>> =
        movieInCategoryDAO.getMoviesIdFromCategoryId(id,lastTimestamp)

    fun getAllCategories() : LiveData<List<Category>> = categoryDAO.getAllCategories()

    fun getAllMoviesInCategory() : LiveData<List<MovieInCategory>> = movieInCategoryDAO.getAllMoviesInCategory()

    fun deleteDeprecatedMoviesInCategory(listId : List<Long>) = movieInCategoryDAO.deleteOldMoviesInCategory(listId)

    //endregion

    //region remote

    suspend fun downloadPopularMovies(page:Int=1)
    {
        val lastPopularMovies = service.getPopularMovies(page = page)
        val idCategory = insertCategory(CATEGORY_POPULAR_ID, CATEGORY_POPULAR_NAME,
            CATEGORY_POPULAR_OVERVIEW)
        insertAll(lastPopularMovies.results)
        insertMovieListInCategory(lastPopularMovies.results.map { it.id }, idCategory,page)
    }

    suspend fun downloadTopRatedMovies(page:Int=1)
    {
        val topRatedMovies = service.getTopRatedMovies(page = page)
        val idCategory = insertCategory(CATEGORY_TOPRATED_ID, CATEGORY_TOPRATED_NAME,
            CATEGORY_TOPRATED_OVERVIEW)
        insertAll(topRatedMovies.results)
        insertMovieListInCategory(topRatedMovies.results.map { it.id }, idCategory,page)
    }

    suspend fun downloadNowPlayingMovies(page:Int=1)
    {
        val newPlayingMovies = service.getNowPlayingMovies(page = page)
        val idCategory = insertCategory(CATEGORY_NOWPLAYING_ID, CATEGORY_NOWPLAYING_NAME,
        CATEGORY_NOWPLAYING_OVERVIEW)
        insertAll(newPlayingMovies.results)
        insertMovieListInCategory(newPlayingMovies.results.map { it.id }, idCategory,page)
    }

    suspend fun downloadUpcomingMovies(page:Int=1)
    {
        val upcomingMovies = service.getUpcomingMovies(page = page)
        val idCategory = insertCategory(CATEGORY_UPCOMING_ID, CATEGORY_UPCOMING_NAME,
            CATEGORY_UPCOMING_OVERVIEW)
        insertAll(upcomingMovies.results)
        insertMovieListInCategory(upcomingMovies.results.map { it.id }, idCategory,page)
    }

    suspend fun downloadMovieDetail(id:Long)
    {
        val movieResult = service.getMovieDetail(id = id)
        val movie = Movie(movieResult.id,movieResult.title,movieResult.genres.map { it.id },movieResult.overview,
            movieResult.popularity,movieResult.posterImg,movieResult.backdropImg,movieResult.releaseDate,
            movieResult.original_title,movieResult.vote_average,movieResult.budget,movieResult.vote_count)
        insert(movie)
    }
}





