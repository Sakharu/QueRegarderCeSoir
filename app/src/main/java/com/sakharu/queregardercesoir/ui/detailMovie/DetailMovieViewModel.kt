package com.sakharu.queregardercesoir.ui.detailMovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import kotlinx.coroutines.Dispatchers

class DetailMovieViewModel : ViewModel()
{
    fun getMovieById(id:Long) : LiveData<Movie> =
        liveData(Dispatchers.IO) {
            emitSource(MovieRepository.getMovieLiveById(id))
            MovieRepository.downloadMovieDetail(id)
        }

    fun getGenresFromMovie(movie:Movie) : LiveData<List<Genre>> =
        liveData(Dispatchers.IO)
        {
            emitSource(MovieRepository.getGenresFromMovie(movie))
        }
}