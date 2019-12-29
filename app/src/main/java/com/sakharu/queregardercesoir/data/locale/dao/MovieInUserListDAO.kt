package com.sakharu.queregardercesoir.data.locale.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakharu.queregardercesoir.data.locale.model.MovieInUserList
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService

@Dao
interface MovieInUserListDAO
{
    @Query("SELECT * FROM MovieInUserList WHERE id = :id")
    fun getById(id:Long) : LiveData<MovieInUserList>

    @Query("SELECT * FROM MovieInUserList WHERE idMovie = :idMovie")
    fun getMovieInUserListFromMovieId(idMovie:Long) : LiveData<List<MovieInUserList>>

    @Query("SELECT * FROM MovieInUserList WHERE idUserList = :idUserList LIMIT ${MovieService.NUMBER_MOVIES_RETRIEVE_BY_REQUEST}")
    fun getMoviesIdFromUserListId(idUserList:Long) : LiveData<List<MovieInUserList>>

    @Query("SELECT * FROM MovieInUserList WHERE idUserList = :idUserList ORDER BY RANDOM() ASC LIMIT 10")
    fun getFirstMoviesIdFromUserListID(idUserList:Long) : LiveData<List<MovieInUserList>>

    @Query("SELECT idMovie FROM MovieInUserList WHERE idUserList = :idUserList")
    fun getFavoritesMoviesId(idUserList:Long) : LiveData<List<Long>>

    @Query("SELECT count(*) FROM MovieInUserList WHERE idUserList = :idUserList AND idMovie = :idMovie")
    fun getCountFromIdUserListAndIdMovie(idUserList:Long, idMovie:Long) : LiveData<Int>

    @Query("DELETE FROM MovieInUserList WHERE idUserList = :idUserList AND idMovie = :idMovie")
    fun deleteMovieInUserList(idUserList: Long,idMovie:Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(MovieInUserList:List<MovieInUserList>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(MovieInUserList: MovieInUserList)


}