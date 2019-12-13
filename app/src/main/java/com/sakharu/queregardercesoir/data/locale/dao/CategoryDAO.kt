package com.sakharu.queregardercesoir.data.locale.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakharu.queregardercesoir.data.locale.model.Category

@Dao
interface CategoryDAO
{
    @Query("SELECT * FROM category WHERE id = :id")
    fun getById(id:Long) : LiveData<Category>

    @Query("SELECT * FROM category ORDER BY id")
    fun getAllCategories() : LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(category:List<Category>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category) : Long

}