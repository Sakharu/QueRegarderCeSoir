package com.sakharu.queregardercesoir.ui.movieGridCategory

import androidx.lifecycle.*
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.ui.base.BaseViewModel
import com.sakharu.queregardercesoir.util.CATEGORY_TOPRATED_ID
import com.sakharu.queregardercesoir.util.CATEGORY_TRENDING_ID
import com.sakharu.queregardercesoir.util.ERROR_CODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieGridCategoryViewModel : BaseViewModel()
{
    lateinit var category : Category
    private var totalPages = 1
    var page : MutableLiveData<Int> = MutableLiveData(1)

    /*
    Récupération des films populaires du moment : on récupère les ID de films qui apparaissent dans
    la table ID
    */

    private var getmoviesIdInSelectedCategoryByPage: LiveData<List<MovieInCategory>> =
        liveData(Dispatchers.IO) {
            emitSource(MovieRepository.getMovieInCategoryLive(category.id!!,page))
        }
    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    var getMoviesLiveList: LiveData<List<Movie>> = Transformations.switchMap(getmoviesIdInSelectedCategoryByPage)
    {
        MovieRepository.getMoviesFromMovieListIdLive(it.map { movieInCategory-> movieInCategory.idMovie })
    }



    fun downloadMovies()
    {
        viewModelScope.launch {
            if (page.value!!<=totalPages)
            {
                page.value = page.value!!+1
                totalPages = when(category.id)
                {
                    CATEGORY_TOPRATED_ID->MovieRepository.downloadTopRatedMovies(page.value!!)
                    CATEGORY_TRENDING_ID->MovieRepository.downloadTrendingMovies(page.value!!)
                    else->MovieRepository.downloadNowPlayingMovies(page.value!!)
                }
                if (totalPages == ERROR_CODE)
                    setError()
            }
        }
    }
}
