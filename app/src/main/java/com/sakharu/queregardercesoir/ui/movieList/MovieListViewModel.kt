package com.sakharu.queregardercesoir.ui.movieList

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.util.CATEGORY_TOPRATED_ID
import com.sakharu.queregardercesoir.util.CATEGORY_TRENDING_ID
import kotlinx.coroutines.Dispatchers

class MovieListViewModel : ViewModel()
{
    lateinit var category : Category
    var totalPages = 0

    /*
    Récupération des films populaires du moment : on récupère les ID de films qui apparaissent dans
    la table ID
    */

    private fun getmoviesIdInSelectedCategoryByPage(page:Int) : LiveData<List<MovieInCategory>> =
        liveData(Dispatchers.IO) {
            totalPages = downloadMovies(page)
            val liste = MovieRepository.getMovieInCategoryLive(category.id!!, lastTimeStamp)
            emitSource(liste)
        }
    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    fun getMoviesLiveList(page: Int) : LiveData<List<Movie>> =
        Transformations.switchMap(getmoviesIdInSelectedCategoryByPage(page))
        {
            MovieRepository.getMoviesFromListIdLive(it.map { movieInCategory-> movieInCategory.idMovie })
        }

    private suspend fun downloadMovies(page:Int) : Int
    {
        return when(category.id)
        {
            CATEGORY_TOPRATED_ID->MovieRepository.downloadTopRatedMovies(page)
            CATEGORY_TRENDING_ID->MovieRepository.downloadTrendingMovies(page)
            else->MovieRepository.downloadNowPlayingMovies(page)
        }
    }

    companion object
    {
        /*
        En gardant le timestamp du dernier film ajouté, on est capable de facilement récupérer
        les nouveaux films ajoutés en base de données sans avoir besoin de récupérer toute la liste
        et donc d'optimiser aussi l'affichage des nouveaux films
         */

        var lastTimeStamp = 0L
    }


}
