package com.sakharu.queregardercesoir.ui.search.title

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
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
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.activity_title_searching.*


class TitleSearchingActivity : BaseActivity(), OnMovieClickListener, OnBottomReachedListener
{
    private lateinit var searchTitleMovieAdapter : TitleSearchMovieAdapter
    private lateinit var titleSearchingViewModel : TitleSearchingViewModel
    private var currentPage=1
    private var isLoading=false
    private var handler = Handler()

    private var movieObserver : Observer<List<Movie>> = Observer {

        if (it.isEmpty())
            if (titleSearchingViewModel.totalPagesSearch==0)
                noMovieFromTitleSearch.show()
            else
                onBottomReached()
        else
        {
            val pair = searchTitleMovieAdapter.addMovie(it)
            if (pair.first)
                onBottomReached()
            recyclerResultTitleSearch.scrollToPosition(pair.second)
            noMovieFromTitleSearch.hide()
        }
        loadingMoreAnimationResultSearch.hide()
        isLoading=false
    }

    private var genreObserver : Observer<List<Genre>> = Observer {
        if (it.isNotEmpty())
            searchTitleMovieAdapter.addGenres(it)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_searching)

        setUpActionBar(getString(R.string.searchTitleFragment))

        titleSearchingViewModel = ViewModelProvider(this, ViewModelFactory()).get(
            TitleSearchingViewModel::class.java)

        titleSearchingViewModel.genresListLive.observe(this,genreObserver)

        searchTitleMovieAdapter = TitleSearchMovieAdapter(arrayListOf(), arrayListOf(),this,this)

        recyclerResultTitleSearch.apply {
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL,false)
            addItemDecoration(DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation))
            adapter = searchTitleMovieAdapter
        }

        editSearchActionBar.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?)
            {
                //si l'utilisateur a au moins rentré 3 caractères et que l'on est pas en attente
                //du résultat d'une requête
                if (editSearchActionBar.text.toString().length >= 3 && !isLoading)
                {
                    //si l'utilisateur ne tape rien pendant 1 seconde et demi, on lance la recherche automatiquement
                    handler.postDelayed({ launchRequest() },1000)
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //En passant null pour le token, tous les callbacks et messages vont être supprimés
                handler.removeCallbacksAndMessages(null)
            }
        })
    }

    override fun onClickOnMovie(movie: Movie, imageView: ImageView)
    {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
            androidx.core.util.Pair<View,String>(imageView,getString(R.string.transitionMovieListToDetail)))

        startActivity(Intent(this, DetailMovieActivity::class.java)
            .putExtra(EXTRA_MOVIE_ID,movie.id),options.toBundle())
    }

    override fun onBottomReached()
    {
        //Si on a atteint la dernière page, on arrête d'écouter la page en cours et on s'abonne à la page suivante
        if (currentPage-1<titleSearchingViewModel.totalPagesSearch && !isLoading)
        {
            isLoading=true
            loadingMoreAnimationResultSearch.show()
            currentPage++
            titleSearchingViewModel.downloadSearchingMoviesFromQuery()
        }
    }

    private fun launchRequest()
    {
        isLoading=true
        loadingMoreAnimationResultSearch.show()
        searchTitleMovieAdapter.clearAllMovies()
        currentPage = 1
        titleSearchingViewModel.query.value =editSearchActionBar.text.toString()
        titleSearchingViewModel.page = 1
        titleSearchingViewModel.downloadSearchingMoviesFromQuery()
        hideKeyboard()
        //si l'observer est déjà placé, refaire observe dessus ne changera rien
        titleSearchingViewModel.searchMovieByTitle.observe(this@TitleSearchingActivity,movieObserver)

    }
}
