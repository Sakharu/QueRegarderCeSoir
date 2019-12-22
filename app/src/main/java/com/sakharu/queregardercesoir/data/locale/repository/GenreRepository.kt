package com.sakharu.queregardercesoir.data.locale.repository

import android.app.Application
import com.sakharu.queregardercesoir.data.locale.AppDatabase
import com.sakharu.queregardercesoir.data.locale.dao.GenreDAO
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.webservice.GenreService

object GenreRepository
{
    private lateinit var database: AppDatabase
    private lateinit var genreDAO: GenreDAO

    private val genreService = GenreService.create()

    fun initialize(application: Application)
    {
        database = AppDatabase.buildInstance(application)
        genreDAO = database.genreDAO()
    }

    /***********************
     *  REGION LOCALE
     **********************/
    /*
        GENRE
     */
    suspend fun insertAllGenre(genreList:List<Genre>) = genreDAO.insertAll(genreList)

    fun getGenresFromMovie(movie: Movie) = genreDAO.getGenreFromListId(movie.genresId)

    fun getAllGenre() = genreDAO.getAllGenres()

    fun getNumberGenres() = genreDAO.getNumberGenres()

    /***********************
     *  REGION REMOTE
     **********************/

    suspend fun downloadAllGenre()
    {
        insertAllGenre(genreService.getAllGenres().genres)
    }
}
