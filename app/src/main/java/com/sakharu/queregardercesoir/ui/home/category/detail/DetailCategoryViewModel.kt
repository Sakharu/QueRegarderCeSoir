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
            val liste = MovieRepository.getMovieInCategory(category.id!!,lastTimeStamp)
            emitSource(liste)
            downloadMovies(page)
        }
    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    fun getMoviesLiveList(page: Int) : LiveData<List<Movie>> =
        Transformations.switchMap(getmoviesIdInSelectedCategoryByPage(page))
        {
            MovieRepository.getMoviesFromCategory(it.map { movieInCategory-> movieInCategory.idMovie })
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

    companion object
    {
        /*
        En gardant le timestamp du dernier film ajouté, on est capable de facilement récupérer
        les nouveaux films ajoutés en base de données sans avoir besoin de récupérer toutes la liste
        et donc d'optimiser aussi l'affichage des nouveaux films
         */

        var lastTimeStamp = 0L
    }


}
