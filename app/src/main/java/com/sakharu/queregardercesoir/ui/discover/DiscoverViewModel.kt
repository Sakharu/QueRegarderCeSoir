package com.sakharu.queregardercesoir.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.model.UsualSearch
import com.sakharu.queregardercesoir.data.locale.repository.GenreRepository
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.data.locale.repository.UsualSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscoverViewModel : ViewModel()
{

    var totalPagesSuggestions = 0
    var refreshSuggestedMovies = true

    var genresListLive : LiveData<List<Genre>> = liveData (Dispatchers.IO)
    {
        if (GenreRepository.getNumberGenres()==0)
            GenreRepository.downloadAllGenre()
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

    fun getSuggestedMovies(page:Int,genresId:ArrayList<Long>) : LiveData<List<Movie>> = liveData(Dispatchers.IO)
    {
        if (refreshSuggestedMovies)
            MovieRepository.downloadSuggestedMovies(page,withGenres = genresId.joinToString("|"))
        emitSource(MovieRepository.getSuggestedMoviesFromGenres(genresId = genresId))
    }
}


