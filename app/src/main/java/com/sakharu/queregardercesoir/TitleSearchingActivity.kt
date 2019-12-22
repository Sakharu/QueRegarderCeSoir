package com.sakharu.queregardercesoir

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
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService
import com.sakharu.queregardercesoir.ui.base.BaseActivity
import com.sakharu.queregardercesoir.ui.detailMovie.DetailMovieActivity
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.ui.search.title.MovieResultTitleSearchAdapter
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.activity_title_searching.*


class TitleSearchingActivity : BaseActivity(), OnMovieClickListener, OnBottomReachedListener
{
    private lateinit var searchTitleAdapter : MovieResultTitleSearchAdapter
    private lateinit var titleSearchingViewModel : TitleSearchingViewModel
    private var currentPage=1
    private var isLoading=false
    private var isWaitingForResult=false
    private var handler = Handler()
    private var oldTitleSearched=""

    private var movieObserver : Observer<List<Movie>> = Observer {
        //Si la liste contient plus d'éléments que ceux récupérés via l'api dans cette session
        //on appelle onBottomReached afin de récupérer les pages suivantes et potentiellement corriger les positions
        if (it.size>currentPage* MovieService.NUMBER_MOVIES_RETRIEVE_BY_REQUEST)
            onBottomReached()

        if (it.isEmpty())
            if (titleSearchingViewModel.totalPagesSearch==0)
                noMovieFromTitleSearch.show()
            else
                onBottomReached()
        else
        {
            searchTitleAdapter.addMovie(it)
            noMovieFromTitleSearch.hide()
        }
        loadingMoreAnimationResultSearch.hide()
        isLoading=false
        isWaitingForResult=false
    }

    private var genreObserver : Observer<List<Genre>> = Observer {
        if (it.isNotEmpty())
            searchTitleAdapter.addGenres(it)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_searching)

        setUpActionBar(getString(R.string.searchTitleFragment))

        titleSearchingViewModel = ViewModelProvider(this, ViewModelFactory()).get(TitleSearchingViewModel::class.java)

        searchTitleAdapter = MovieResultTitleSearchAdapter(arrayListOf(), arrayListOf(),this,this)

        recyclerResultTitleSearch.apply {
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation))
            adapter = searchTitleAdapter
        }

        titleSearchingViewModel.genresListLive.observe(this,genreObserver)

        editSearchActionBar.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?)
            {
                //si l'utilisateur a au moins rentré 3 caractères et que l'on est pas en attente
                //du résultat d'une requête
                if (editSearchActionBar.text.toString().length >= 3 && !isWaitingForResult)
                {
                    //si l'utilisateur ne tape rien pendant 1 seconde et demi, on lance la recherche automatiquement
                    handler.postDelayed({ launchRequest() },1000)
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //En passant null pour le token, tous les callbacks et messages vont être supprimés
                handler.removeCallbacksAndMessages(null)
                titleSearchingViewModel.searchMovieByTitle(oldTitleSearched,currentPage).removeObservers(this@TitleSearchingActivity)
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
            currentPage++
            //si on est dans le cas d'une recherche par titre

            titleSearchingViewModel.searchMovieByTitle(oldTitleSearched).removeObserver(movieObserver)
            titleSearchingViewModel.searchMovieByTitle(editSearchActionBar.text.toString(),currentPage).observe(this,movieObserver)

            loadingMoreAnimationResultSearch.show()
        }
    }

    private fun launchRequest()
    {
        isLoading=true
        titleSearchingViewModel.searchMovieByTitle(oldTitleSearched,currentPage).removeObserver(movieObserver)
        searchTitleAdapter.clearAllMovies()
        currentPage = 1
        titleSearchingViewModel.searchMovieByTitle(editSearchActionBar.text.toString()).observe(this@TitleSearchingActivity,movieObserver)
        isWaitingForResult = true
        oldTitleSearched = editSearchActionBar.text.toString()
        loadingMoreAnimationResultSearch.show()
        hideKeyboard()
    }
}
