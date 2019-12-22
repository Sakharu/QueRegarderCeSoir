package com.sakharu.queregardercesoir.ui.detailMovie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService
import com.sakharu.queregardercesoir.ui.base.BaseActivity
import com.sakharu.queregardercesoir.ui.FullscreenActivity
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.LittleMovieAdapter
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.activity_detail_movie.*
import java.text.SimpleDateFormat
import java.util.*

class DetailMovieActivity : BaseActivity(), OnMovieClickListener
{
    private lateinit var detailMovieViewModel: DetailMovieViewModel
    private lateinit var genresObserver:Observer<List<Genre>>
    private lateinit var similarMoviesAdapter : LittleMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        detailMovieViewModel = ViewModelProvider(this, ViewModelFactory()).get(DetailMovieViewModel::class.java)

        genresObserver = Observer {
            afficherGenres(it)
        }

        val id = intent.getLongExtra(EXTRA_MOVIE_ID,-1)
        if (id==-1L)
        //TODO FERMER ACTIVITE AFFICHER ERREUR
            finish()
        else
        {
            detailMovieViewModel.getMovieById(id).observe(this, Observer<Movie> {
                showMovieInformations(this,it)
                detailMovieViewModel.getGenresFromMovie(it).removeObserver(genresObserver)
                detailMovieViewModel.getGenresFromMovie(it).observe(this,genresObserver)
            })

        }

        similarMoviesAdapter = LittleMovieAdapter(arrayListOf(),this)

        recyclerSimilarMovies.apply {
            layoutManager = GridLayoutManager(this@DetailMovieActivity,3)
            adapter = similarMoviesAdapter
        }

        detailMovieViewModel.getSimilarMovies(id).observe(this, Observer {
            if (it.isNotEmpty())
                similarMoviesAdapter.setData(it)
            else
                titleSimilarMoviesDetail.hide()
        })


    }

    private fun showMovieInformations(context: Context, movie: Movie)
    {

        setUpActionBar(movie.title)

        //Affichage du poster
        if (!movie.posterImg.isNullOrEmpty())
            Glide.with(context)
                .load(MovieService.IMAGE_PREFIX_POSTER + movie.posterImg)
                .error(R.drawable.film_poster_placeholder)
                .dontAnimate()
                .into(imgPosterMovieDetail)
        else
            Glide.with(context).load(R.drawable.film_poster_placeholder).into(imgPosterMovieDetail)

        imgPosterMovieDetail.setOnClickListener{
            startActivity(Intent(this@DetailMovieActivity, FullscreenActivity::class.java)
                .putExtra(EXTRA_IMAGE_URL,movie.posterImg).putExtra(EXTRA_TYPE_IMAGE, TYPE_POSTER)
                .putExtra(EXTRA_MOVIE_NAME,movie.title))
        }

        //Affichage de l'image de fond
        if (!movie.backdropImg.isNullOrEmpty())
            Glide.with(context)
                .load(MovieService.IMAGE_PREFIX_BACKDROP + movie.backdropImg)
                .fallback(R.drawable.large_movie_placeholder)
                .error(R.drawable.large_movie_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgBackdropMovieDetail)
        else
            Glide.with(context).load(R.drawable.large_movie_placeholder).into(imgBackdropMovieDetail)

        imgBackdropMovieDetail.setOnClickListener{
            startActivity(Intent(this@DetailMovieActivity, FullscreenActivity::class.java)
                .putExtra(EXTRA_IMAGE_URL,movie.backdropImg).putExtra(EXTRA_TYPE_IMAGE, TYPE_BACKDROP)
                .putExtra(EXTRA_MOVIE_NAME,movie.title))
        }

        titleMovieDetail.text = movie.title

        if (movie.overview.isNotEmpty())
            overviewMovieDetail.text = movie.overview
        else
            overviewMovieDetail.setInvisible()

        popularityMovieDetail.text = getString(R.string.popularityPrefix,movie.popularity)

        if (movie.vote_average!=null && movie.vote_average!=0.0)
        {
            progressBarAverageVoteMovieDetail.progress = movie.vote_average!!.toInt()*10
            textVoteMovieDetail.text = movie.vote_average.toString()
            progressBarAverageVoteMovieDetail.max = 100
            textAverageVoteMovieDetail.show()
            progressBarAverageVoteMovieDetail.show()
            peopleIconDetailMovie.show()
        }
        else
        {
            textAverageVoteMovieDetail.hide()
            progressBarAverageVoteMovieDetail.hide()
            peopleIconDetailMovie.hide()
        }

        if (movie.vote_count!=null && movie.vote_count!=0)
        {
            voteCountMovieDetail.show()
            voteCountMovieDetail.text = getString(R.string.voteCountPrefix,movie.vote_count)
        }
        else
            voteCountMovieDetail.hide()

        if (!movie.original_title.isNullOrEmpty() && movie.original_title!=movie.title)
        {
            originalTitleMovieDetail.show()
            originalTitleMovieDetail.text = getString(R.string.originalTitlePrefix,movie.original_title)
            iconTranslateMovieDetail.show()
        }
        else
        {
            iconTranslateMovieDetail.hide()
            originalTitleMovieDetail.hide()
        }

        if (!movie.releaseDate.isNullOrEmpty())
        {
            try
            {
                val americanDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(movie.releaseDate!!)
                val date = SimpleDateFormat("dd MMMM yyyy",Locale.getDefault()).format(americanDate!!)
                releaseDateMovieDetail.text = getString(R.string.releaseDatePrefix,date)
                releaseDateMovieDetail.show()
                iconDateMovieDetail.show()

            }
            catch (e:Exception)
            {
                e.printStackTrace()
                releaseDateMovieDetail.hide()
                iconDateMovieDetail.hide()
            }
        }
        else
        {
            releaseDateMovieDetail.hide()
            iconDateMovieDetail.hide()
        }
    }

    private fun afficherGenres(listGenre:List<Genre>)
    {
        val names = listGenre.joinToString { it.name }
        if (names.isNotEmpty())
            genresMovieDetail.text = getString(R.string.genrePrefix,names)
        else
            genresMovieDetail.hide()
    }

    override fun onClickOnMovie(movie: Movie, imageView: ImageView) {
    }
}

