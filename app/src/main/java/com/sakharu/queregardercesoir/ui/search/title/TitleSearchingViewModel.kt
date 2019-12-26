package com.sakharu.queregardercesoir.ui.search.title

import androidx.lifecycle.*
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.repository.GenreRepository
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.ui.base.BaseViewModel
import com.sakharu.queregardercesoir.util.ERROR_CODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TitleSearchingViewModel : BaseViewModel()
{
    var totalPagesSearch = 1
    var query:MutableLiveData<String> = MutableLiveData()
    var page:Int=1

    val searchMovieByTitle : LiveData<List<Movie>> = liveData (
        Dispatchers.IO)
    {
        emitSource(MovieRepository.getMoviesFromTitleSearch(query))
    }

    var genresListLive : LiveData<List<Genre>> = liveData (Dispatchers.IO)
    {
        if(GenreRepository.isAllGenreInDB)
            if (GenreRepository.downloadAllGenre() == ERROR_CODE)
                setError()
        emitSource(GenreRepository.getAllGenre())
    }

    fun downloadSearchingMoviesFromQuery() =
        viewModelScope.launch {
            if (page<=totalPagesSearch)
            {
                totalPagesSearch = MovieRepository.searchMovieFromQuery(query.value!!,page)
                if (totalPagesSearch == ERROR_CODE)
                    setError()
                page++
            }
        }


}