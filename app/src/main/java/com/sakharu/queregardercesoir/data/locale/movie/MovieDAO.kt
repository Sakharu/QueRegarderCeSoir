package com.sakharu.queregardercesoir.data.locale.movie

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDAO
{
    @Query("SELECT * FROM movie WHERE id in (:moviesListId)")
    fun getPopularMovies(moviesListId:List<Long>) : LiveData<List<Movie>>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getById(id:Long) : LiveData<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies:List<Movie>)


}