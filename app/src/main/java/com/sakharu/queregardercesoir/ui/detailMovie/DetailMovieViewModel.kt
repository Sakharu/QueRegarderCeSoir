package com.sakharu.queregardercesoir.ui.detailMovie

import androidx.lifecycle.*
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.model.MovieInUserList
import com.sakharu.queregardercesoir.data.locale.model.UserList
import com.sakharu.queregardercesoir.data.locale.repository.GenreRepository
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.data.locale.repository.UserListRepository
import com.sakharu.queregardercesoir.ui.base.BaseViewModel
import com.sakharu.queregardercesoir.util.ERROR_CODE
import com.sakharu.queregardercesoir.util.USER_LIST_FAVORITE_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailMovieViewModel : BaseViewModel()
{
    var userLists = listOf<UserList>()
    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                userLists = UserListRepository.getAllUserLists()
            }
        }
    }

    fun getMovieById(id:Long) : LiveData<Movie> = liveData(Dispatchers.IO)
    {
        val movie = MovieRepository.getMovieLiveById(id)
        emitSource(movie)
        if (MovieRepository.downloadMovieDetail(id, movie.value?.isSuggested) == ERROR_CODE)
            setError()
    }

    fun getGenresFromMovie(movie:Movie) : LiveData<List<Genre>> = liveData(Dispatchers.IO)
    {
        emitSource(GenreRepository.getGenresFromMovie(movie))
    }

    fun getSimilarMovies(id: Long) : LiveData<List<Movie>> = liveData(Dispatchers.IO)
    {
        emitSource(MovieRepository.getMoviesFromMovieListIdLive(MovieRepository.downloadSimilarMovies(id)))
    }

    fun isMovieFavorite(idMovie : Long) : LiveData<Int> = liveData {
        emitSource(UserListRepository.isMovieFavorite(idMovie))
    }

    fun isMovieWatched(idMovie : Long) : LiveData<Int> = liveData {
        emitSource(UserListRepository.isMovieWatched(idMovie))
    }

    fun isMovieToWatch(idMovie : Long) : LiveData<Int> = liveData {
        emitSource(UserListRepository.isMovieToWatch(idMovie))
    }

    private fun allUserLists() : LiveData<List<UserList>> = liveData(Dispatchers.IO)
    {
        emitSource(UserListRepository.getAllUserListsLive())
    }

    private fun getMoviesInUserListFromMovieId(idMovie:Long) : LiveData<List<MovieInUserList>> = liveData(Dispatchers.IO)
    {
        emitSource(UserListRepository.getMovieInUserListFromMovieId(idMovie))
    }

    fun getUserListsForMovie(idMovie: Long) = Transformations.switchMap(getMoviesInUserListFromMovieId(idMovie))
    {
        UserListRepository.getUserListsFromMovieInUserLists(it.map { movieInUserList -> movieInUserList.id!! })
    }

    fun updateFavoriteMovie(idMovie: Long,isFavorite:Boolean)
    {
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                if (isFavorite)
                    UserListRepository.insertMovieInUserList(idMovie, USER_LIST_FAVORITE_ID)
                else
                    UserListRepository.deleteMovieInUserList(idMovie, USER_LIST_FAVORITE_ID)
            }
        }
    }

    var getFavoritesMoviesId: LiveData<List<Long>> =
        liveData(Dispatchers.IO) {
            emitSource(UserListRepository.getFavoriteMoviesId())
        }



}