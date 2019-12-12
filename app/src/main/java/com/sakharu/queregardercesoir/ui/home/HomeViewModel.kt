package com.sakharu.queregardercesoir.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.FilmRepository
import kotlinx.coroutines.Dispatchers

class HomeViewModel : ViewModel() {

    var films = liveData(Dispatchers.IO)
    {
        emitSource(FilmRepository.getPopularMovies())
        val latestPopMovies = FilmRepository.downloadPopularMovies()

    }
}