package com.sakharu.queregardercesoir.data.locale.genre

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakharu.queregardercesoir.data.locale.genre.Genre

@Dao
interface GenreDAO
{
    @Query("SELECT * FROM genre WHERE id = :id")
    fun getById(id:Long) : LiveData<Genre>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genre:List<Genre>) : Long


}