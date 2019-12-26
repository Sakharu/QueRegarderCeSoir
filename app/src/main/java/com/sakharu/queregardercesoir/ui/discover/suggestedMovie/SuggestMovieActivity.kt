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
import com.sakharu.queregardercesoir.ui.base.BaseActivity
import com.sakharu.queregardercesoir.ui.detailMovie.DetailMovieActivity
import com.sakharu.queregardercesoir.ui.discover.DiscoverViewModel
import com.sakharu.queregardercesoir.ui.movieGridCategory.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.ui.search.title.TitleSearchMovieAdapter
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.activity_result_advanced_search.*
import kotlinx.android.synthetic.main.fragment_discover.*

class SuggestMovieActivity : BaseActivity(), OnBottomReachedListener, OnMovieClickListener
{
    private lateinit var searchTitleMovieAdapter : TitleSearchMovieAdapter
    private lateinit var discoverViewModel: DiscoverViewModel
    private var isLoading=true
    private var oldSize = 0

    private var movieObserver : Observer<List<Movie>> = Observer {
        //si aucun film n'a été trouvé en BD
        if (it.isEmpty())
            if (discoverViewModel.totalPagesSuggestions==0)
                noFilmFromTitleSearch.show()
            else
                onBottomReached()
        else
        {
            //si on a pas chargé assez de film, on charge la page suivante
            if (searchTitleMovieAdapter.addMovie(it))
                onBottomReached()
            noFilmFromTitleSearch.hide()

            //si on a récupéré des nouveaux films
            if (oldSize!=it.size)
                loadingMoreAnimationResultAdvancedSearch.hide()
            //si on a pas récupéré de nouveaux films, on va lancer le chargement de la page suivante
            else
                onBottomReached()
        }
        isLoading=false
        oldSize = it.size
    }

    private var genreObserver : Observer<List<Genre>> = Observer {
        if (it.isNotEmpty())
        {
            searchTitleMovieAdapter.addGenres(it)
            discoverViewModel.suggestedMovies.observe(this,movieObserver)
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
            discoverViewModel.favoriteGenresId = intent.getLongArrayExtra(EXTRA_GENRES)!!.toCollection(ArrayList())

        setUpActionBar(getString(R.string.suggestionMoviesTitle))

        discoverViewModel.errorNetwork.observe(this, Observer {
            if (it)
            {
                showDialogNetworkError()
                loadingMoreAnimationSuggestedMovies.hide()
            }
        })
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
        if (!isLoading)
        {
            discoverViewModel.downloadSuggestMovies()
            loadingMoreAnimationResultAdvancedSearch.show()
            isLoading=true
        }
    }
}