package com.sakharu.queregardercesoir.data.locale.category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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