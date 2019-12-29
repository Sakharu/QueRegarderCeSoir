package com.sakharu.queregardercesoir.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.model.UsualSearch
import com.sakharu.queregardercesoir.data.locale.repository.GenreRepository
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.data.locale.repository.UsualSearchRepository
import com.sakharu.queregardercesoir.ui.base.BaseViewModel
import com.sakharu.queregardercesoir.util.ERROR_CODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscoverViewModel : BaseViewModel()
{
    var totalPagesSuggestions = 1
    var refreshSuggestedMovies = true
    var page=1
    var favoriteGenresId:ArrayList<Long> = arrayListOf()

    var genresListLive : LiveData<List<Genre>> = liveData (Dispatchers.IO)
    {
        if (!GenreRepository.isAllGenreInDB)
            if (GenreRepository.downloadAllGenre() == ERROR_CODE)
                setError()
        emitSource(GenreRepository.getAllGenre())
    }

    var nbUsualSearches :LiveData<Int> = UsualSearchRepository.getNumberUsualSearches()

    var getAllUsualSearchesListLive : LiveData<List<UsualSearch>> = UsualSearchRepository.getAllUsualSearches()

    fun insertUsualSearches(arrayTitles: Array<String>, arraySubTitle: Array<String>,nbUsualSearch: Int) =
        viewModelScope.launch {
            if (nbUsualSearch==0)
            {
                val listUsualSearch = arrayListOf<UsualSearch>()
                for (i in arrayTitles.indices)
                    listUsualSearch.add(UsualSearch(i,arrayTitles[i],arraySubTitle[i]))
                UsualSearchRepository.insertAllUsualSearches(listUsualSearch)
            }
        }

    fun getSuggestedMovies(genresId:ArrayList<Long>,orderBy:String="RANDOM()") : LiveData<List<Movie>> = liveData(Dispatchers.IO)
    {
        if (refreshSuggestedMovies)
        {
            totalPagesSuggestions = MovieRepository.downloadSuggestedMovies(page,withGenres = genresId.joinToString("|"))
            if (totalPagesSuggestions == ERROR_CODE)
                setError()
        }
        emitSource(MovieRepository.getSuggestedMoviesFromGenres(genresId = genresId,orderBy = orderBy))
    }

    val suggestedMovies =  liveData(Dispatchers.IO)
    {
        emitSource(MovieRepository.getSuggestedMoviesFromGenres(genresId = favoriteGenresId,orderBy = "vote_average DESC",limit = ""))
    }

    fun downloadSuggestMovies() =
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                page++
                totalPagesSuggestions = MovieRepository.downloadSuggestedMovies(page,withGenres = favoriteGenresId.joinToString("|"))
                if (totalPagesSuggestions == ERROR_CODE)
                    setError()
            }
        }

}


