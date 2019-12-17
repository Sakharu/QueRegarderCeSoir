package com.sakharu.queregardercesoir.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import kotlinx.coroutines.Dispatchers

class SearchViewModel : ViewModel()
{
    fun searchMovieByTitle(query:String) : LiveData<List<Movie>> = liveData (Dispatchers.IO)
    {
        MovieRepository.searchMovie(query)
        emitSource(MovieRepository.getMoviesFromTitleSearch(query))
    }
}