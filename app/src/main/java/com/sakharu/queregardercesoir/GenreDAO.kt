package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GenreDAO
{
    @Query("SELECT * FROM genre WHERE id = :id")
    fun getById(id:Int) : LiveData<Genre>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genre:List<Genre>)


}