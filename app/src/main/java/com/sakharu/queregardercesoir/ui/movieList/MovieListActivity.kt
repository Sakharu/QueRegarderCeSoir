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
import com.sakharu.queregardercesoir.base.BaseActivity
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.ui.detailMovie.DetailMovieActivity
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.LittleMovieAdapter
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.fragment_detail_category.*


class MovieListActivity : BaseActivity(),OnMovieClickListener
{
    private lateinit var movieListCategoryViewModel: MovieListCategoryViewModel
    private lateinit var littleMoviePagingAdapter: LittleMovieAdapter
    private lateinit var observer : Observer<List<Movie>>
    private var page=1

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_detail_category)

        ajouterActionAIntentFilter(ACTION_LOAD_MORE_CATEGORY_DETAIL)

        littleMoviePagingAdapter = LittleMovieAdapter(arrayListOf(),true,this)
        recyclerCategoryDetails.apply {
            layoutManager = GridLayoutManager(this@MovieListActivity,3)
            setHasFixedSize(true)
            adapter = littleMoviePagingAdapter
        }

        observer = Observer {
            littleMoviePagingAdapter.addData(it)
            loadingMoreAnimationDetailCategory.hide()
            isLoading=false
        }

        backButtonCategoryDetail.setOnClickListener{
            finish()
        }

        movieListCategoryViewModel = ViewModelProvider(this, ViewModelFactory()).get(MovieListCategoryViewModel::class.java)
        movieListCategoryViewModel.category = intent.getSerializableExtra(EXTRA_CATEGORY) as Category

        nameCategoryDetail.text = movieListCategoryViewModel.category.name
        overviewCategoryDetail.text = movieListCategoryViewModel.category.overview

        //on charge les films populaires lorsqu'on en a en BD
        movieListCategoryViewModel.getMoviesLiveList(page).observe(this, observer)
        listePageChargee.add(page)
    }

    override fun doOnReceive(intent: Intent)
    {
        super.doOnReceive(intent)
        /*
        Si l'adapter notifie le fragment qu'il a chargé les derniers items de la liste,
        on va télécharger et afficher les films de la page suivante
         */
        if (intent.action== ACTION_LOAD_MORE_CATEGORY_DETAIL)
        {
            page = intent.getIntExtra(EXTRA_PAGE,page)
            movieListCategoryViewModel.getMoviesLiveList(page).removeObserver(observer)
            movieListCategoryViewModel.getMoviesLiveList(page).observe(this, observer)
            loadingMoreAnimationDetailCategory.show()
            listePageChargee.add(page)
        }
    }

    companion object
    {
        var isLoading=true
        var listePageChargee= arrayListOf<Int>()
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
