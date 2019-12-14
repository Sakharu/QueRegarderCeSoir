package com.sakharu.queregardercesoir.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.util.CATEGORY_NOWPLAYING_ID
import com.sakharu.queregardercesoir.util.CATEGORY_POPULAR_ID
import com.sakharu.queregardercesoir.util.CATEGORY_TOPRATED_ID
import com.sakharu.queregardercesoir.util.CATEGORY_UPCOMING_ID
import kotlinx.coroutines.Dispatchers

class HomeViewModel : ViewModel()
{

    /*
      on récupère toutes les catégories de la BD
    */
    var categoriesLiveList : LiveData<List<Category>> = liveData (Dispatchers.IO)
    {
        emitSource(MovieRepository.getAllCategories())
    }

    /************************************
     * POPULAR MOVIES
     ***********************************/
    /*
        Récupération des films populaires du moment : on récupère les ID de films qui apparaissent dans
        la table ID
    */
    var moviesInPopularCategory : LiveData<List<MovieInCategory>> = liveData(Dispatchers.IO)
    {
        emitSource(MovieRepository.getMovieInCategory(CATEGORY_POPULAR_ID))
        MovieRepository.downloadPopularMovies()
    }

    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    var popularMoviesLiveList = Transformations.switchMap(moviesInPopularCategory)
    {
        MovieRepository.getMoviesInCategory(it.map { movieInCategory-> movieInCategory.idMovie })
    }

    /************************************
     * TOPRATED MOVIES
     ***********************************/
     /*
        Récupération des films les mieux notés : on récupère les ID de films qui apparaissent dans
        la table ID
     */
    var moviesInTopRatedCategory : LiveData<List<MovieInCategory>> = liveData(Dispatchers.IO)
    {
        emitSource(MovieRepository.getMovieInCategory(CATEGORY_TOPRATED_ID))
        MovieRepository.downloadTopRatedMovies()
    }

    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    var topRatedMoviesLiveList = Transformations.switchMap(moviesInTopRatedCategory)
    {
        MovieRepository.getMoviesInCategory(it.map { movieInCategory-> movieInCategory.idMovie })
    }

    /************************************
     * NOWPLAYING MOVIES
     ***********************************/
    /*
       Récupération des films les mieux notés : on récupère les ID de films qui apparaissent dans
       la table ID
    */
    var nowPlayingMovieInCategory : LiveData<List<MovieInCategory>> = liveData(Dispatchers.IO)
    {
        emitSource(MovieRepository.getMovieInCategory(CATEGORY_NOWPLAYING_ID))
        MovieRepository.downloadNowPlayingMovies()
    }

    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    var nowPlayingMoviesLiveList = Transformations.switchMap(nowPlayingMovieInCategory)
    {
        MovieRepository.getMoviesInCategory(it.map { movieInCategory-> movieInCategory.idMovie })
    }

    /************************************
     * UPCOMING MOVIES
     ***********************************/
    /*
       Récupération des films les mieux notés : on récupère les ID de films qui apparaissent dans
       la table ID
    */
    var upcomingMovieInCategory : LiveData<List<MovieInCategory>> = liveData(Dispatchers.IO)
    {
        emitSource(MovieRepository.getMovieInCategory(CATEGORY_UPCOMING_ID))
        MovieRepository.downloadUpcomingMovies()
    }

    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    var upcomingMoviesLiveList = Transformations.switchMap(upcomingMovieInCategory)
    {
        MovieRepository.getMoviesInCategory(it.map { movieInCategory-> movieInCategory.idMovie })
    }

}