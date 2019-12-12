package com.sakharu.queregardercesoir

import android.app.Application
import androidx.lifecycle.LiveData


object FilmRepository {
    private lateinit var database: AppDatabase

    private lateinit var filmDAO: FilmDAO

    private val service = FilmService.create()

    fun initialize(application: Application) {
        database = AppDatabase.buildInstance(application)
        filmDAO = database.filmDAO()
    }

    //region locale

    suspend fun insertAll(films: List<Film>) {
        filmDAO.insertAll(films)
    }


    suspend fun insert(film: Film) = insertAll(listOf(film))

    fun getById(id: Int): LiveData<Film> = filmDAO.getById(id)

    fun getPopularMovies(): LiveData<List<Film>> = filmDAO.getPopularMovies()

    //endregion

    //region remote


    suspend fun downloadPopularMovies()
    {
        val lastPopularMovies = service.getPopularMovies()
        insertAll(lastPopularMovies.results)
    }


}





