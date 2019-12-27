package com.sakharu.queregardercesoir.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.repository.UserListRepository
import com.sakharu.queregardercesoir.util.USER_LIST_FAVORITE_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel()
{
    var errorNetwork: MutableLiveData<Boolean> = MutableLiveData(false)

    suspend fun setError()
    {
        withContext(Dispatchers.Main)
        {
            errorNetwork.value=true
        }
    }

    fun updateFavoriteMovie(movie: Movie) = liveData<Pair<Long,Boolean>>
    {
        withContext(Dispatchers.IO)
        {
            //Si le film n'était pas dans les favoris, on l'ajoute, sinon on l'enlève
            if (UserListRepository.getMovieInUserListFromIdUserListAndIdMovie(movie.id,
                    USER_LIST_FAVORITE_ID)==0)
            {
                UserListRepository.insertMovieInUserList(movie.id, USER_LIST_FAVORITE_ID)
                emit(Pair(movie.id,true))
            }
            else
            {
                UserListRepository.deleteMovieInUserList(movie.id, USER_LIST_FAVORITE_ID)
                emit(Pair(movie.id,false))
            }
        }
    }

    var getFavoritesMoviesId: LiveData<List<Long>> =
        liveData(Dispatchers.IO) {
            emitSource(UserListRepository.getFavoriteMoviesId())
        }


}