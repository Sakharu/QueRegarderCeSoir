package com.sakharu.queregardercesoir

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakharu.queregardercesoir.Film

@Dao
interface FilmDAO
{
    @Query("SELECT * FROM film ORDER BY popularity DESC")
    fun getPopularMovies() : LiveData<List<Film>>

    @Query("SELECT * FROM film WHERE id = :id")
    fun getById(id:Int) : LiveData<Film>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(films:List<Film>)


}