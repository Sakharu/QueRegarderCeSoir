package com.sakharu.queregardercesoir.ui.discover.suggestedMovie

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService
import com.sakharu.queregardercesoir.ui.base.BaseActivity
import com.sakharu.queregardercesoir.ui.detailMovie.DetailMovieActivity
import com.sakharu.queregardercesoir.ui.discover.DiscoverViewModel
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.ui.search.title.TitleSearchMovieAdapter
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.activity_result_advanced_search.*

class SuggestMovieActivity : BaseActivity(), OnBottomReachedListener, OnMovieClickListener
{
    private lateinit var searchTitleMovieAdapter : TitleSearchMovieAdapter
    private lateinit var discoverViewModel: DiscoverViewModel
    private var currentPage=1
    private var genresId:ArrayList<Long> = arrayListOf()
    private var isLoading=true
    private var movieObserver : Observer<List<Movie>> = Observer {
        //Si la liste contient plus d'éléments que ceux récupérés via l'api dans cette session
        //on appelle onBottomReached afin de récupérer les pages suivantes et potentiellement corriger les positions
        if (it.size>currentPage* MovieService.NUMBER_MOVIES_RETRIEVE_BY_REQUEST)
            onBottomReached()

        if (it.isEmpty())
        {
            if (discoverViewModel.totalPagesSuggestions==0)
            {
                noFilmFromTitleSearch.show()
                noFilmFromTitleSearch.text = getString(R.string.noFilmFromTitleSearch)
            }
            else
                onBottomReached()
        }
        else
        {
            searchTitleMovieAdapter.addMovie(it)
            noFilmFromTitleSearch.hide()
        }
        loadingMoreAnimationResultAdvancedSearch.hide()
        isLoading=false

    }

    private var genreObserver : Observer<List<Genre>> = Observer {
        if (it.isNotEmpty())
        {
            searchTitleMovieAdapter.addGenres(it)
            discoverViewModel.genresId = it.map { genre -> genre.id }.toCollection(ArrayList())
            discoverViewModel.suggestMovies.observe(this,movieObserver)
            discoverViewModel.genresListLive.removeObservers(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_advanced_search)

        discoverViewModel = ViewModelProvider(this, ViewModelFactory()).get(DiscoverViewModel::class.java)

        discoverViewModel.genresListLive.observe(this,genreObserver)

        searchTitleMovieAdapter = TitleSearchMovieAdapter(arrayListOf(), arrayListOf(), this, this)

        recyclerAdvancedTitleMovie.apply {
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(false)
            addItemDecoration(DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation))
            adapter = searchTitleMovieAdapter
        }
        loadingMoreAnimationResultAdvancedSearch.show()

        if (intent.getLongArrayExtra(EXTRA_GENRES)!=null)
            genresId = intent.getLongArrayExtra(EXTRA_GENRES)!!.toCollection(ArrayList())

        setUpActionBar(getString(R.string.resultSearchTitle))
    }

    override fun onClickOnMovie(movie: Movie, imageView: ImageView)
    {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
            androidx.core.util.Pair<View,String>(imageView,getString(R.string.transitionMovieListToDetail)))

        startActivity(Intent(this, DetailMovieActivity::class.java).putExtra(EXTRA_MOVIE_ID,movie.id),options.toBundle())
    }

    override fun onBottomReached()
    {
        //On lance le téléchargement de la page suivante lorsqu'on arrive au bout de la page en cours
        if (currentPage-1<discoverViewModel.totalPagesSuggestions && !isLoading)
        {
            currentPage++
            discoverViewModel.downloadSuggestMovies(currentPage)
            loadingMoreAnimationResultAdvancedSearch.show()
            isLoading=true
        }
    }
}