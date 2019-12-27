package com.sakharu.queregardercesoir.ui.userList

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.model.MovieInUserList
import com.sakharu.queregardercesoir.data.locale.model.UserList
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.data.locale.repository.UserListRepository
import com.sakharu.queregardercesoir.util.NUMBER_OF_BASE_LIST
import com.sakharu.queregardercesoir.util.USER_LIST_FAVORITE_ID
import com.sakharu.queregardercesoir.util.USER_LIST_TOWATCH_ID
import com.sakharu.queregardercesoir.util.USER_LIST_WATCHED_ID
import kotlinx.coroutines.Dispatchers

class UserListViewModel : ViewModel()
{
    var arrayBaseUserListNames = arrayOf<String>()
    var arrayBaseUserListResourceName = arrayOf<String>()

    val userListLiveList : LiveData<List<UserList>> = liveData (Dispatchers.IO)
    {
        if (UserListRepository.getNbUserList()< NUMBER_OF_BASE_LIST)
            UserListRepository.insertBaseUserList(arrayBaseUserListNames,arrayBaseUserListResourceName)
        emitSource(UserListRepository.getAllUserLists())
    }

    fun getMoviesInUserListFromUserListId(idUserList:Long): LiveData<List<MovieInUserList>> = liveData(Dispatchers.IO)
    {
        emitSource(UserListRepository.getFirstMoviesInMovieLisIdLive(idUserList))
    }

    var firstFavoriteMovieList = Transformations.switchMap(getMoviesInUserListFromUserListId(USER_LIST_FAVORITE_ID))
    {
        MovieRepository.getMoviesFromMovieListIdLive(it.map { movieInCategory-> movieInCategory.idMovie })
    }

    var firstToWatchMovieList = Transformations.switchMap(getMoviesInUserListFromUserListId(USER_LIST_TOWATCH_ID))
    {
        MovieRepository.getMoviesFromMovieListIdLive(it.map { movieInCategory-> movieInCategory.idMovie })
    }

    var firstWatchedMovieList = Transformations.switchMap(getMoviesInUserListFromUserListId(USER_LIST_WATCHED_ID))
    {
        MovieRepository.getMoviesFromMovieListIdLive(it.map { movieInCategory-> movieInCategory.idMovie })
    }
}