package com.sakharu.queregardercesoir.ui.detailMovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.repository.GenreRepository
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.ui.base.BaseViewModel
import com.sakharu.queregardercesoir.util.ERROR_CODE
import kotlinx.coroutines.Dispatchers

class DetailMovieViewModel : BaseViewModel()
{
    fun getMovieById(id:Long) : LiveData<Movie> =
        liveData(Dispatchers.IO) {
            val movie = MovieRepository.getMovieLiveById(id)
            emitSource(movie)
            val isSuggested = if (movie.value!=null) movie.value!!.isSuggested else null
            if (MovieRepository.downloadMovieDetail(id,isSuggested) == ERROR_CODE)
                setError()
        }

    fun getGenresFromMovie(movie:Movie) : LiveData<List<Genre>> =
        liveData(Dispatchers.IO)
        {
            emitSource(GenreRepository.getGenresFromMovie(movie))
        }

    fun getSimilarMovies(id: Long) : LiveData<List<Movie>> =
        liveData(Dispatchers.IO)
        {
            emitSource(MovieRepository.getMoviesFromListIdLive(MovieRepository.downloadSimilarMovies(id)))
        }
}