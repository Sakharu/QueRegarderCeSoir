package com.sakharu.queregardercesoir.data.locale.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sakharu.queregardercesoir.data.locale.AppDatabase
import com.sakharu.queregardercesoir.data.locale.dao.MovieInUserListDAO
import com.sakharu.queregardercesoir.data.locale.dao.UserListDAO
import com.sakharu.queregardercesoir.data.locale.model.MovieInUserList
import com.sakharu.queregardercesoir.data.locale.model.UserList
import com.sakharu.queregardercesoir.util.USER_LIST_FAVORITE_ID

object UserListRepository
{
    private lateinit var database: AppDatabase
    private lateinit var userListDAO: UserListDAO
    private lateinit var movieInUserListDAO: MovieInUserListDAO


    fun initialize(application: Application)
    {
        database = AppDatabase.buildInstance(application)
        userListDAO = database.userListDAO()
        movieInUserListDAO = database.movieInUserListDAO()
    }

    /***********************
     *  REGION LOCALE
     **********************/

    /*
        USER LIST
     */

    fun getNbUserList() = userListDAO.getNbUserList()

    suspend fun insertUserList(id: Long, name: String,iconResourceName:String)
            = userListDAO.insert(UserList(id, name,iconResourceName))

    fun getAllUserLists() : LiveData<List<UserList>> = userListDAO.getAllLists()

    suspend fun insertBaseUserList(names:Array<String>,resourceNames:Array<String>)
    {
        for (i in names.indices)
            insertUserList(i.toLong(),names[i],resourceNames[i])
    }

    /**
     * MOVIE IN USER LIST
     */

    suspend fun insertMovieInUserList(idMovie:Long, idUserList:Long)=
        movieInUserListDAO.insert(MovieInUserList(null,idUserList,idMovie))

    suspend fun deleteMovieInUserList(idMovie:Long, idUserList:Long)=
        movieInUserListDAO.deleteMovieInUserList(idUserList,idMovie)

    fun getFirstMoviesInMovieLisIdLive(userListId:Long): LiveData<List<MovieInUserList>> =
        movieInUserListDAO.getFirstMoviesIdFromUserListID(userListId)

    fun getMovieInUserListFromIdUserListAndIdMovie(idUserList:Long, idMovie:Long)
            = movieInUserListDAO.getMovieInUserListFromIdUserListAndIdMovie(idUserList,idMovie)

    fun getFavoriteMoviesId() = movieInUserListDAO.getFavoritesMoviesId(USER_LIST_FAVORITE_ID)

}
