package com.sakharu.queregardercesoir

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.repository.GenreRepository
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import kotlinx.coroutines.Dispatchers

class TitleSearchingViewModel : ViewModel()
{
    var totalPagesSearch = 0

    fun searchMovieByTitle(query:String,page:Int=1) : LiveData<List<Movie>> = liveData (
        Dispatchers.IO)
    {
        totalPagesSearch = MovieRepository.searchMovieFromQuery(query,page)
        emitSource(MovieRepository.getMoviesFromTitleSearch(query))
    }

    var genresListLive : LiveData<List<Genre>> = liveData (Dispatchers.IO)
    {
        GenreRepository.downloadAllGenre()
        emitSource(GenreRepository.getAllGenre())
    }
}