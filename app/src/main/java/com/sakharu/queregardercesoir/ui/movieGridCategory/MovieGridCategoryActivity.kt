package com.sakharu.queregardercesoir.ui.movieGridCategory

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
import com.sakharu.queregardercesoir.ui.base.BaseActivity
import com.sakharu.queregardercesoir.ui.base.OnBottomReachedListener
import com.sakharu.queregardercesoir.ui.detailMovie.DetailMovieActivity
import com.sakharu.queregardercesoir.ui.movieGridCategory.littleMovie.LittleMovieAdapter
import com.sakharu.queregardercesoir.ui.movieGridCategory.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.activity_movie_grid.*


class MovieGridCategoryActivity : BaseActivity(),OnMovieClickListener, OnBottomReachedListener
{
    private lateinit var movieGridCategoryViewModel: MovieGridCategoryViewModel
    private lateinit var littleMoviePagingAdapter: LittleMovieAdapter
    private lateinit var observer : Observer<List<Movie>>
    private var isLoading=false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_grid)

        littleMoviePagingAdapter = LittleMovieAdapter(arrayListOf(),this, this)
        recyclerCategoryDetails.apply {
            layoutManager = GridLayoutManager(this@MovieGridCategoryActivity,3)
            setHasFixedSize(false)
            adapter = littleMoviePagingAdapter
        }

        observer = Observer {
            if (it.isNotEmpty())
            {
                littleMoviePagingAdapter.addData(it)
                loadingMoreAnimationDetailCategory.hide()
                isLoading=false
            }
            else
                onBottomReached()
        }

        movieGridCategoryViewModel = ViewModelProvider(this, ViewModelFactory()).get(MovieGridCategoryViewModel::class.java)
        movieGridCategoryViewModel.category = intent.getSerializableExtra(EXTRA_CATEGORY) as Category

        setUpActionBar(movieGridCategoryViewModel.category.name)

        //on charge les films populaires lorsqu'on en a en BD
        movieGridCategoryViewModel.getMoviesLiveList.observe(this, observer)

        movieGridCategoryViewModel.errorNetwork.observe(this, Observer {
            if (it)
            {
                showDialogNetworkError()
                loadingMoreAnimationDetailCategory.hide()
            }
        })
    }

    override fun onBottomReached()
    {
        if (!isLoading)
        {
            movieGridCategoryViewModel.downloadMovies()
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
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MovieGridCategoryActivity,
            androidx.core.util.Pair<View,String>(imageView,movie.id.toString()))

        startActivity(Intent(this, DetailMovieActivity::class.java).
            putExtra(EXTRA_MOVIE_ID,movie.id),options.toBundle())
    }
}
