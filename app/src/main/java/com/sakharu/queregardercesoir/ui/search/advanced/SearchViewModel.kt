package com.sakharu.queregardercesoir.ui.search.advanced

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.repository.GenreRepository
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel()
{
    var page : Int = 0
    var totalPagesSearch = 0
    private var sortBy : String=""
    private var voteAverageGte : Double=0.0
    private var withGenres : String?=null
    private var yearDateGte : String?=null
    private var yearDateLte : String?=null
    private var duringYear : Int?=null
    private var year : Int = 0
    private var certificationCountry : String?=null
    private var certification : String?=null

    //on affecte les valeurs aux attributs de la classe, on passe par le constructeur natif
    //pour pouvoir continuer d'utiliser le viewModelFactory
    fun init(page: Int,sortBy: String,voteAverageGte: Double,withGenres: String?,
             yearDateGte: String?,yearDateLte: String?,duringYear: Int?,year: Int,
             certificationCountry: String?,certification: String?)
    {
        this.page = page
        this.sortBy = sortBy
        this.voteAverageGte = voteAverageGte
        this.withGenres = withGenres
        this.yearDateGte = yearDateGte
        this.yearDateLte = yearDateLte
        this.duringYear = duringYear
        this.year = year
        this.certificationCountry = certificationCountry
        this.certification = certification
    }

    val searchMovieFromCharacteristics = liveData(Dispatchers.IO)
    {
        val genresId = withGenres?.split(",") ?: listOf()
        emitSource(MovieRepository.getMoviesFromCharacSearch(sortBy, voteAverageGte, genresId.filter { it.isNotEmpty() },
            yearDateGte, yearDateLte, duringYear, year, certification))
    }

    var genresListLive : LiveData<List<Genre>> = liveData (Dispatchers.IO)
    {
        if (GenreRepository.getNumberGenres()==0)
            GenreRepository.downloadAllGenre()
        emitSource(GenreRepository.getAllGenre())
    }

    fun downloadNextPage()
    {
        viewModelScope.launch {
            if (page-1<=totalPagesSearch)
            {
                totalPagesSearch = MovieRepository.searchMovieFromCharacteristics(page,sortBy,
                    voteAverageGte,withGenres, yearDateGte,yearDateLte,duringYear,
                    certificationCountry,certification)
                page++
            }
        }
    }

}