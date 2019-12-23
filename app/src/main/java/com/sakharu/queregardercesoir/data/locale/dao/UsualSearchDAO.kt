package com.sakharu.queregardercesoir.data.locale.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakharu.queregardercesoir.data.locale.model.UsualSearch


@Dao
interface UsualSearchDAO
{
    @Query("SELECT * FROM usualSearch WHERE id = :id")
    fun getById(id:Long) : LiveData<UsualSearch>

    @Query("SELECT * FROM usualSearch")
    fun getAllUsualSearches() : LiveData<List<UsualSearch>>


    @Query("SELECT count(*) FROM usualSearch")
    fun numberUsualSearches() : LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genre:List<UsualSearch>)
}