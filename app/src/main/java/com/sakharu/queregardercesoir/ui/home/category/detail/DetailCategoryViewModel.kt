package com.sakharu.queregardercesoir.ui.home.category.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.util.CATEGORY_NOWPLAYING_ID
import com.sakharu.queregardercesoir.util.CATEGORY_POPULAR_ID
import com.sakharu.queregardercesoir.util.CATEGORY_TOPRATED_ID
import com.sakharu.queregardercesoir.util.CATEGORY_UPCOMING_ID
import kotlinx.coroutines.Dispatchers

class DetailCategoryViewModel : ViewModel()
{
    lateinit var category : Category

    /*
    Récupération des films populaires du moment : on récupère les ID de films qui apparaissent dans
    la table ID
    */

    private fun getmoviesIdInSelectedCategoryByPage(page:Int) : LiveData<List<MovieInCategory>> =
        liveData(Dispatchers.IO) {
            emitSource(MovieRepository.getMovieInCategory(category.id!!))
            downloadMovies(page)
        }
    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    fun getMoviesLiveList(page:Int) : LiveData<List<Movie>> =
        Transformations.switchMap(getmoviesIdInSelectedCategoryByPage(page))
        {
            MovieRepository.getMoviesInCategory(it.map { movieInCategory-> movieInCategory.idMovie })
        }

    private suspend fun downloadMovies(page:Int)
    {
        when(category.id)
        {
            CATEGORY_POPULAR_ID->MovieRepository.downloadPopularMovies(page)
            CATEGORY_TOPRATED_ID->MovieRepository.downloadTopRatedMovies(page)
            CATEGORY_UPCOMING_ID->MovieRepository.downloadUpcomingMovies(page)
            CATEGORY_NOWPLAYING_ID->MovieRepository.downloadNowPlayingMovies(page)
        }
    }


}
