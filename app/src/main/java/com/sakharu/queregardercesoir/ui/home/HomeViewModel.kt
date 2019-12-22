package com.sakharu.queregardercesoir.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.locale.repository.CategoryRepository
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.util.CATEGORY_NOWPLAYING_ID
import com.sakharu.queregardercesoir.util.CATEGORY_TOPRATED_ID
import com.sakharu.queregardercesoir.util.CATEGORY_TRENDING_ID
import kotlinx.coroutines.Dispatchers

class HomeViewModel : ViewModel()
{
    var refreshData=true
    /*
      on récupère toutes les catégories de la BD
    */
    var categoriesLiveList : LiveData<List<Category>> = liveData (Dispatchers.IO)
    {
        emitSource(CategoryRepository.getAllCategoriesLive())
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
        if (refreshData)
            MovieRepository.downloadTopRatedMovies()
        emitSource(MovieRepository.getFirstMoviesInCategoryFromCategoryId(CATEGORY_TOPRATED_ID))
    }

    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    var topRatedMoviesLiveList = Transformations.switchMap(moviesInTopRatedCategory)
    {
        MovieRepository.getMoviesFromListIdLive(it.map { movieInCategory-> movieInCategory.idMovie })
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
        if (refreshData)
            MovieRepository.downloadNowPlayingMovies()
        emitSource(MovieRepository.getFirstMoviesInCategoryFromCategoryId(CATEGORY_NOWPLAYING_ID))
    }

    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    var nowPlayingMoviesLiveList = Transformations.switchMap(nowPlayingMovieInCategory)
    {
        MovieRepository.getMoviesFromListIdLive(it.map { movieInCategory-> movieInCategory.idMovie })
    }

    /************************************
     * TRENDING MOVIES
     ***********************************/
    /*
       Récupération des films les mieux notés : on récupère les ID de films qui apparaissent dans
       la table ID
    */
    var trendingMoviesInCategory : LiveData<List<MovieInCategory>> = liveData(Dispatchers.IO)
    {
        if (refreshData)
            MovieRepository.downloadTrendingMovies()
        emitSource(MovieRepository.getFirstMoviesInCategoryFromCategoryId(CATEGORY_TRENDING_ID))
    }

    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    var trendingMoviesLiveList = Transformations.switchMap(trendingMoviesInCategory)
    {
        MovieRepository.getMoviesFromListIdLive(it.map { movieInCategory-> movieInCategory.idMovie })
    }


    /************************************
     * DELETE MOVIES FROM CATEGORY
     ***********************************/
    /*
    On récupère toutes les liaisons entre les films et les catégories en vue de les supprimer
     lorsque celles-ci datent de plus de 3 jours
    */
    var getAllMoviesInCategory : LiveData<List<MovieInCategory>> = liveData(Dispatchers.IO)
    {
        emitSource(MovieRepository.getAllMoviesInCategoryLive())
    }

}