package com.sakharu.queregardercesoir.data.locale.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakharu.queregardercesoir.data.locale.model.Movie

@Dao
interface MovieDAO
{
    @Query("SELECT * FROM movie WHERE id in (:moviesListId)")
    fun getMoviesByListId(moviesListId:List<Long>) : LiveData<List<Movie>>

    @Query("SELECT * FROM movie ")
    fun getAll() : LiveData<List<Movie>>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getById(id:Long) : LiveData<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies:List<Movie>)
}