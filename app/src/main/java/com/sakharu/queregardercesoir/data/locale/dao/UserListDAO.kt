package com.sakharu.queregardercesoir.data.locale.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakharu.queregardercesoir.data.locale.model.UserList

@Dao
interface UserListDAO
{
    @Query("SELECT * FROM UserList WHERE id = :id")
    fun getById(id:Long) : LiveData<UserList>

    @Query("SELECT * FROM userList WHERE id in (:userListsIds)")
    fun getUserListsFromListId(userListsIds:List<Long>) : LiveData<List<UserList>>

    @Query("SELECT * FROM UserList ORDER BY id")
    fun getAllListsLive() : LiveData<List<UserList>>

    @Query("SELECT * FROM UserList ORDER BY id")
    fun getAllList() : List<UserList>

    @Query("SELECT count(*) FROM UserList")
    fun getNbUserList() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(UserList:List<UserList>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(UserList: UserList) : Long

}