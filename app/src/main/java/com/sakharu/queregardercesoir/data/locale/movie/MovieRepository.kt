package com.sakharu.queregardercesoir.data.locale.movie

import android.app.Application
import androidx.lifecycle.LiveData
import com.sakharu.queregardercesoir.data.locale.AppDatabase
import com.sakharu.queregardercesoir.data.locale.category.Category
import com.sakharu.queregardercesoir.data.locale.category.CategoryDAO
import com.sakharu.queregardercesoir.data.locale.movieInCategory.MovieInCategory
import com.sakharu.queregardercesoir.data.locale.movieInCategory.MovieInCategoryDAO
import com.sakharu.queregardercesoir.data.remote.MovieService
import com.sakharu.queregardercesoir.util.CATEGORY_POPULAR_NAME


object MovieRepository {
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

    suspend fun insertCategory(name:String) = categoryDAO.insert(Category(null,name))

    suspend fun insert(movie: Movie) = insertAll(listOf(movie))

    suspend fun insertMovieListInCategory(idMovieList : List<Long>, categoryId:Long)
    {
        for(idMovie in idMovieList)
            movieInCategoryDAO.insert(MovieInCategory(null,categoryId,idMovie))
    }

    fun getById(id: Long): LiveData<Movie> = movieDAO.getById(id)

    fun getPopularMovies(popularMoviesListId:List<Long>): LiveData<List<Movie>> = movieDAO.getPopularMovies(popularMoviesListId)

    fun getMovieInCategory(id:Long): LiveData<List<MovieInCategory>> = movieInCategoryDAO.getMoviesIdFromCategoryId(id)

    fun getAllCategories() : LiveData<List<Category>> = categoryDAO.getAllCategories()

    //endregion

    //region remote


    suspend fun downloadPopularMovies()
    {
        val lastPopularMovies = service.getPopularMovies()
        val idCategory = insertCategory(CATEGORY_POPULAR_NAME)
        insertAll(lastPopularMovies.results)
        insertMovieListInCategory(lastPopularMovies.results.map { it.id }, idCategory)
    }


}





