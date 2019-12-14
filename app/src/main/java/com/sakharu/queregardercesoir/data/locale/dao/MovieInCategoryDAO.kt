package com.sakharu.queregardercesoir.data.locale.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory

@Dao
interface MovieInCategoryDAO
{
    @Query("SELECT * FROM movieInCategory WHERE id = :id")
    fun getById(id:Long) : LiveData<MovieInCategory>

    @Query("SELECT * FROM movieInCategory WHERE idCategory = :idCategory ORDER BY `order`")
    fun getMoviesIdFromCategoryId(idCategory:Long) : LiveData<List<MovieInCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movieInCategory:List<MovieInCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieInCategory: MovieInCategory)
}