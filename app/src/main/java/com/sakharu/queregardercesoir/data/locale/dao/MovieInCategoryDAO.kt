package com.sakharu.queregardercesoir.data.locale.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService

@Dao
interface MovieInCategoryDAO
{
    @Query("SELECT * FROM movieInCategory WHERE id = :id")
    fun getById(id:Long) : LiveData<MovieInCategory>

    @Query("SELECT * FROM movieInCategory WHERE idCategory = :idCategory AND page = :page ORDER BY `order`LIMIT ${MovieService.NUMBER_MOVIES_RETRIEVE_BY_REQUEST}")
    fun getMoviesIdFromCategoryId(idCategory:Long,page:Int) : LiveData<List<MovieInCategory>>

    @Query("SELECT * FROM movieInCategory WHERE idCategory = :idCategory ORDER BY page,`order` ASC LIMIT ${MovieService.NUMBER_MOVIES_RETRIEVE_BY_REQUEST}")
    fun getFirstMoviesIdFromCategoryID(idCategory:Long) : LiveData<List<MovieInCategory>>

    @Query("SELECT * FROM movieInCategory")
    fun getAllMoviesInCategory() : LiveData<List<MovieInCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movieInCategory:List<MovieInCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieInCategory: MovieInCategory)
}