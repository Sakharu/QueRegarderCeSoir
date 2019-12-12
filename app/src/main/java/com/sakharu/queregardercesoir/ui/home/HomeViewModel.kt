package com.sakharu.queregardercesoir.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.category.Category
import com.sakharu.queregardercesoir.data.locale.movie.Movie
import com.sakharu.queregardercesoir.data.locale.movie.MovieRepository
import com.sakharu.queregardercesoir.util.CATEGORY_POPULAR_ID
import kotlinx.coroutines.Dispatchers

class HomeViewModel : ViewModel() {

    /*
    var films : LiveData<List<Movie>> = liveData(Dispatchers.IO)
    {
        //on recupère les films que l'on a en bd
        emitSource(MovieRepository.getPopularMovies())
        //on télécharge les films les plus réçcents et leur insertion en bd va permettre de recharger les films grâce au liveData
        MovieRepository.downloadPopularMovies()
    }
*/
    /*
        récupération des films populaires : on récupère les ID de films qui apparaissent dans
        la table ID
     */

    var popularMoviesLiveList : LiveData<List<Movie>> = liveData(Dispatchers.IO)
    {
        val popularMoviesId = MovieRepository.getMovieInCategory(CATEGORY_POPULAR_ID).value?.map { it.idMovie }
        if (popularMoviesId!=null)
            emitSource(MovieRepository.getPopularMovies(popularMoviesId))
    }

    var categoriesLiveList : LiveData<List<Category>> = liveData (Dispatchers.IO)
    {
        emitSource(MovieRepository.getAllCategories())
    }

    var allMoviesInAllCategories : List<List<Movie>> = listOf(popularMoviesLiveList.value.orEmpty())



}