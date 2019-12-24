package com.sakharu.queregardercesoir.ui.search.advanced

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
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.ui.search.title.TitleSearchMovieAdapter
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.activity_result_advanced_search.*
import java.util.*


class AdvancedResultSearchActivity : BaseActivity(), OnMovieClickListener, OnBottomReachedListener
{
    private lateinit var searchTitleMovieAdapter : TitleSearchMovieAdapter
    private lateinit var searchViewModel: SearchViewModel
    private var currentPage=1
    private var sortBy : String="popularity"
    private var before:String?=null
    private var during:Int?=null
    private var after:String?=null
    private var year:Int=0
    private var averageVoteMin:Double=0.0
    private var genresId:String?=null
    private var isLoading=false
    private var certification:String?=null
    private var movieObserver : Observer<List<Movie>> = Observer {
        if (it.isEmpty())
        {
            if (searchViewModel.totalPagesSearch==0)
            {
                noFilmFromTitleSearch.show()
                noFilmFromTitleSearch.text = getString(R.string.noFilmFromTitleSearch)
            }
            else
                onBottomReached()
        }
        else
        {
            val pair = searchTitleMovieAdapter.addMovie(it)
            if (pair.first)
                onBottomReached()
            recyclerAdvancedTitleMovie.scrollToPosition(pair.second)
            noFilmFromTitleSearch.hide()
        }
        loadingMoreAnimationResultAdvancedSearch.hide()
        isLoading=false
    }

    private var genreObserver : Observer<List<Genre>> = Observer {
        if (it.isNotEmpty())
        {
            searchTitleMovieAdapter.addGenres(it)

            searchViewModel.searchMovieFromCharacteristics.observe(this,movieObserver)
            searchViewModel.genresListLive.removeObservers(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_advanced_search)

        searchViewModel = ViewModelProvider(this, ViewModelFactory()).get(SearchViewModel::class.java)

        searchViewModel.genresListLive.observe(this,genreObserver)

        searchTitleMovieAdapter = TitleSearchMovieAdapter(arrayListOf(), arrayListOf(), this, this)

        recyclerAdvancedTitleMovie.apply {
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(false)
            addItemDecoration(DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation))
            adapter = searchTitleMovieAdapter
        }
        loadingMoreAnimationResultAdvancedSearch.show()

        sortBy = intent.getStringExtra(EXTRA_SORTBY).orEmpty()
        val beforeAfter = intent.getIntExtra(EXTRA_BEFORE_DURING_AFTER,0)
        year = intent.getIntExtra(EXTRA_YEAR,Calendar.getInstance().get(Calendar.YEAR))
        averageVoteMin = intent.getDoubleExtra(EXTRA_AVERAGE_VOTE_MIN,0.0)
        genresId = intent.getStringExtra(EXTRA_GENRES)
        certification = intent.getStringExtra(EXTRA_CERTIFICATION)

        when(beforeAfter)
        {
            0-> before = "$year-12-31"
            1-> during = year
            else-> after = "$year-01-01"
        }

        searchViewModel.init(currentPage,sortBy,averageVoteMin,
            genresId,after,before,during,year,CERTIFICATION_FRANCE,certification)

        searchViewModel.downloadNextPage()

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
        if (!isLoading)
        {
            currentPage++
            searchViewModel.downloadNextPage()
            loadingMoreAnimationResultAdvancedSearch.show()
            isLoading=true
        }
    }
}
