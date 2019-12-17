package com.sakharu.queregardercesoir.ui.search.title

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.ui.detailMovie.DetailMovieActivity
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.ui.search.SearchViewModel
import com.sakharu.queregardercesoir.util.EXTRA_MOVIE_ID
import com.sakharu.queregardercesoir.util.EXTRA_TITLE_SEARCH
import com.sakharu.queregardercesoir.util.ViewModelFactory
import kotlinx.android.synthetic.main.activity_result_search.*


class ResultSearchActivity : AppCompatActivity(), OnMovieClickListener
{
    private lateinit var searchTitleAdapter : MovieResultTitleSearchAdapter
    private var titleSearch = ""
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_search)

        searchViewModel = ViewModelProvider(this, ViewModelFactory()).get(SearchViewModel::class.java)

        backButtonResultSearchTitle.setOnClickListener{onBackPressed()}

        searchTitleAdapter = MovieResultTitleSearchAdapter(arrayListOf(), this)

        recyclerRechercheTitleMovie.apply {
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation))
            adapter = searchTitleAdapter
        }

        titleSearch = intent.getStringExtra(EXTRA_TITLE_SEARCH).orEmpty()
        if (titleSearch.isNotEmpty())
        {
            searchViewModel.searchMovieByTitle(titleSearch).observe(this, Observer {
                searchTitleAdapter.addData(it)
                resultSearchInformation.text = getString(R.string.resultSearchTitle,titleSearch)
            })
        }
        else
        {
            //TODO AFFICHER ERREUR FERMER ACTIVITE
        }



    }

    override fun onClickOnMovie(movie: Movie, imageView: ImageView)
    {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
            androidx.core.util.Pair<View,String>(imageView,getString(R.string.transitionMovieListToDetail)))

        startActivity(Intent(this, DetailMovieActivity::class.java).
                putExtra(EXTRA_MOVIE_ID,movie.id),options.toBundle())
    }
}
