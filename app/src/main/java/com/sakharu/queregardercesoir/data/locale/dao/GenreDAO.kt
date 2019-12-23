package com.sakharu.queregardercesoir.data.locale.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakharu.queregardercesoir.data.locale.model.Genre

@Dao
interface GenreDAO
{
    @Query("SELECT * FROM genre WHERE id = :id")
    fun getById(id:Long) : LiveData<Genre>

    @Query("SELECT * FROM genre WHERE id in (:genresId)")
    fun getGenreFromListId(genresId:List<Long>) : LiveData<List<Genre>>

    @Query("SELECT * FROM genre ORDER BY name")
    fun getAllGenres() : LiveData<List<Genre>>

    @Query("SELECT count(*) FROM genre ")
    fun getNumberGenres() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genre:List<Genre>)


}