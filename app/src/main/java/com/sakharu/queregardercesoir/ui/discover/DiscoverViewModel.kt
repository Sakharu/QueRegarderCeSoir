package com.sakharu.queregardercesoir.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.repository.GenreRepository
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import kotlinx.coroutines.Dispatchers

class DiscoverViewModel : ViewModel()
{
    var totalPagesSearch = 0

    fun searchMovieFromCharacteristics(page:Int=1, sortBy:String, voteAverageGte:Double=0.0, withGenres:String?=null,
                                       yearDateGte:String?=null, yearDateLte:String?=null, duringYear:Int?=null,year:Int)= liveData(Dispatchers.IO)
    {
        totalPagesSearch = MovieRepository.searchMovieFromCharacteristics(page,sortBy,voteAverageGte,withGenres,yearDateGte,yearDateLte,duringYear)
        val genresId = withGenres?.split(",") ?: listOf()
        emitSource(MovieRepository.getMoviesFromCharacSearch(sortBy,voteAverageGte,genresId.filter { it.isNotEmpty() },yearDateGte,yearDateLte,duringYear,year))
    }

    var genresListLive : LiveData<List<Genre>> = liveData (Dispatchers.IO)
    {
        if (GenreRepository.getNumberGenres()==0)
            GenreRepository.downloadAllGenre()
        emitSource(GenreRepository.getAllGenre())
    }


}