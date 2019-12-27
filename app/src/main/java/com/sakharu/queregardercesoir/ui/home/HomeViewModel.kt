package com.sakharu.queregardercesoir.ui.home

import androidx.lifecycle.*
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.locale.repository.CategoryRepository
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.ui.base.BaseViewModel
import com.sakharu.queregardercesoir.util.*
import kotlinx.coroutines.Dispatchers

class HomeViewModel : BaseViewModel()
{

    var refreshData=true
    var arrayCategoryNames = arrayOf<String>()

    /*
      on récupère toutes les catégories de la BD
    */
    val categoriesLiveList : LiveData<List<Category>> = liveData (Dispatchers.IO)
    {
        if (CategoryRepository.getNbCategory()!= NUMBER_OF_CATEGORIES)
            CategoryRepository.insertAllCategories(arrayCategoryNames)
        emitSource(CategoryRepository.getAllCategoriesLive())
    }


    /************************************
     * TOPRATED MOVIES
     ***********************************/
    /*
       Récupération des films les mieux notés : on récupère les ID de films qui apparaissent dans
       la table ID
    */
    private var moviesInTopRatedCategory : LiveData<List<MovieInCategory>> = liveData(Dispatchers.IO)
    {
        if (refreshData)
            if (MovieRepository.downloadTopRatedMovies() == ERROR_CODE)
                setError()
        emitSource(MovieRepository.getFirstMoviesInCategoryFromCategoryId(CATEGORY_TOPRATED_ID))
    }

    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    var topRatedMoviesLiveList = Transformations.switchMap(moviesInTopRatedCategory)
    {
        MovieRepository.getMoviesFromMovieListIdLive(it.map { movieInCategory-> movieInCategory.idMovie })
    }

    /************************************
     * NOWPLAYING MOVIES
     ***********************************/
    /*
       Récupération des films les mieux notés : on récupère les ID de films qui apparaissent dans
       la table ID
    */
    private var nowPlayingMovieInCategory : LiveData<List<MovieInCategory>> = liveData(Dispatchers.IO)
    {
        if (refreshData)
            if (MovieRepository.downloadNowPlayingMovies() == ERROR_CODE)
                setError()
        emitSource(MovieRepository.getFirstMoviesInCategoryFromCategoryId(CATEGORY_NOWPLAYING_ID))
    }

    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    var nowPlayingMoviesLiveList = Transformations.switchMap(nowPlayingMovieInCategory)
    {
        MovieRepository.getMoviesFromMovieListIdLive(it.map { movieInCategory-> movieInCategory.idMovie })
    }

    /************************************
     * TRENDING MOVIES
     ***********************************/
    /*
       Récupération des films les mieux notés : on récupère les ID de films qui apparaissent dans
       la table ID
    */
    private var trendingMoviesInCategory : LiveData<List<MovieInCategory>> = liveData(Dispatchers.IO)
    {
        if (refreshData)
            if (MovieRepository.downloadTrendingMovies() == ERROR_CODE)
                setError()
        emitSource(MovieRepository.getFirstMoviesInCategoryFromCategoryId(CATEGORY_TRENDING_ID))
    }

    /*
        On récupère la liste des film populairs une fois qu'on a récupéré les ID nécessaires
        de la table MovieInCategory
    */
    var trendingMoviesLiveList = Transformations.switchMap(trendingMoviesInCategory)
    {
        MovieRepository.getMoviesFromMovieListIdLive(it.map { movieInCategory-> movieInCategory.idMovie })
    }
}