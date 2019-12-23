package com.sakharu.queregardercesoir.ui.movieList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService
import com.sakharu.queregardercesoir.ui.base.BaseActivity
import com.sakharu.queregardercesoir.ui.detailMovie.DetailMovieActivity
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.LittleMovieAdapter
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.activity_detail_category.*


class MovieListActivity : BaseActivity(),OnMovieClickListener, OnBottomReachedListener
{
    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var littleMoviePagingAdapter: LittleMovieAdapter
    private lateinit var observer : Observer<List<Movie>>
    private var currentPage=1
    private var isLoading=false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_category)

        MovieListViewModel.lastTimeStamp=0

        littleMoviePagingAdapter = LittleMovieAdapter(arrayListOf(), this, this)
        recyclerCategoryDetails.apply {
            layoutManager = GridLayoutManager(this@MovieListActivity,3)
            setHasFixedSize(false)
            adapter = littleMoviePagingAdapter
        }

        observer = Observer {
            if (it.size>currentPage* MovieService.NUMBER_MOVIES_RETRIEVE_BY_REQUEST)
                onBottomReached()

            if (it.isNotEmpty())
                littleMoviePagingAdapter.addData(it)
            else
                onBottomReached()
            loadingMoreAnimationDetailCategory.hide()
            isLoading=false
        }

        movieListViewModel = ViewModelProvider(this, ViewModelFactory()).get(MovieListViewModel::class.java)
        movieListViewModel.category = intent.getSerializableExtra(EXTRA_CATEGORY) as Category

        setUpActionBar(movieListViewModel.category.name)

        //on charge les films populaires lorsqu'on en a en BD
        movieListViewModel.getMoviesLiveList(currentPage).observe(this, observer)
    }

    override fun onBottomReached()
    {
        if (!isLoading && currentPage<=movieListViewModel.totalPages)
        {
            currentPage++
            movieListViewModel.getMoviesLiveList(currentPage-1).removeObserver(observer)
            movieListViewModel.getMoviesLiveList(currentPage).observe(this, observer)
            loadingMoreAnimationDetailCategory.show()
            isLoading=true
        }
    }


    /*
    Si l'adapter notifie l'activite qu'un film a été touché, on ouvre l'activite de tail
    et on lui donne en transition l'imageview pour avoir l'animation du poster et éviter de
    recharger l'image dans l'activite suivante
     */
    override fun onClickOnMovie(movie: Movie, imageView: ImageView)
    {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MovieListActivity,
            androidx.core.util.Pair<View,String>(imageView,getString(R.string.transitionMovieListToDetail)))

        startActivity(Intent(this, DetailMovieActivity::class.java).
            putExtra(EXTRA_MOVIE_ID,movie.id),options.toBundle())
    }
}
