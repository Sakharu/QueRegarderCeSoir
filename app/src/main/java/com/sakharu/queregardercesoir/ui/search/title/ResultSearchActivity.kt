package com.sakharu.queregardercesoir.ui.search.title

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
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.ui.discover.DiscoverViewModel
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.activity_result_advanced_search.*
import java.util.*


class ResultSearchActivity : BaseActivity(), OnMovieClickListener, OnBottomReachedListener
{
    private lateinit var searchTitleAdapter : MovieResultTitleSearchAdapter
    private lateinit var discoverViewModel: DiscoverViewModel
    private var currentPage=1
    private var sortBy : String="popularity"
    private var before:String?=null
    private var during:Int?=null
    private var after:String?=null
    private var year:Int=0
    private var averageVoteMin:Double=0.0
    private var genresId:String?=null
    private var isLoading=false
    private var movieObserver : Observer<List<Movie>> = Observer {
        //Si la liste contient plus d'éléments que ceux récupérés via l'api dans cette session
        //on appelle onBottomReached afin de récupérer les pages suivantes et potentiellement corriger les positions
        if (it.size>currentPage*MovieService.NUMBER_MOVIES_RETRIEVE_BY_REQUEST)
            onBottomReached()

        if (it.isEmpty())
        {
            if (discoverViewModel.totalPagesSearch==0)
            {
                noFilmFromTitleSearch.show()
                noFilmFromTitleSearch.text = getString(R.string.noFilmFromTitleSearch)
            }
            else
                onBottomReached()
        }
        else
        {
            searchTitleAdapter.addMovie(it)
            noFilmFromTitleSearch.hide()
        }
        loadingMoreAnimationResultAdvancedSearch.hide()
        isLoading=false
    }

    private var genreObserver : Observer<List<Genre>> = Observer {
        if (it.isNotEmpty())
        {
            searchTitleAdapter.addGenres(it)

            discoverViewModel.searchMovieFromCharacteristics(currentPage,sortBy,averageVoteMin,
                genresId,after,before,during,year).observe(this,movieObserver)
            discoverViewModel.genresListLive.removeObservers(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_advanced_search)

        discoverViewModel = ViewModelProvider(this, ViewModelFactory()).get(DiscoverViewModel::class.java)

        discoverViewModel.genresListLive.observe(this,genreObserver)

        searchTitleAdapter = MovieResultTitleSearchAdapter(arrayListOf(), arrayListOf(),this,this)

        recyclerAdvancedTitleMovie.apply {
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation))
            adapter = searchTitleAdapter
        }
        loadingMoreAnimationResultAdvancedSearch.show()

        sortBy = intent.getStringExtra(EXTRA_SORTBY).orEmpty()
        val beforeAfter = intent.getIntExtra(EXTRA_IS_BEFORE,0)
        year = intent.getIntExtra(EXTRA_YEAR,Calendar.getInstance().get(Calendar.YEAR))
        averageVoteMin = intent.getDoubleExtra(EXTRA_AVERAGE_VOTE_MIN,0.0)
        genresId = intent.getStringExtra(EXTRA_GENRES)

        when(beforeAfter)
        {
            0-> before = "$year-12-31"
            1-> during = year
            else-> after = "$year-01-01"
        }

        setUpActionBar(getString(R.string.resultSearchTitle))
    }

    override fun onClickOnMovie(movie: Movie, imageView: ImageView)
    {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
            androidx.core.util.Pair<View,String>(imageView,getString(R.string.transitionMovieListToDetail)))

        startActivity(Intent(this, DetailMovieActivity::class.java).
            putExtra(EXTRA_MOVIE_ID,movie.id),options.toBundle())
    }

    override fun onBottomReached()
    {
        //Si on a atteint la dernière page, on arrête d'écouter la page en cours et on s'abonne à la page suivante
        if (currentPage-1<discoverViewModel.totalPagesSearch && !isLoading)
        {
            currentPage++
            //si on est dans le cas d'une recherche par titre

            discoverViewModel.searchMovieFromCharacteristics(currentPage-1,sortBy,averageVoteMin,
                genresId,after,before,during,year).removeObservers(this)
            //on se désabonne de la page précédente et on s'abonne à la page suivante
            discoverViewModel.searchMovieFromCharacteristics(currentPage,sortBy,averageVoteMin,
                genresId,after,before,during,year).observe(this,movieObserver)

            loadingMoreAnimationResultAdvancedSearch.show()
            isLoading=true
        }
    }
}
