package com.sakharu.queregardercesoir.ui.detailMovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import kotlinx.coroutines.Dispatchers

class DetailMovieViewModel : ViewModel()
{
    fun getMovieById(id:Long) : LiveData<Movie> =
        liveData(Dispatchers.IO) {
            emitSource(MovieRepository.getById(id))
            MovieRepository.downloadMovieDetail(id)
        }
}