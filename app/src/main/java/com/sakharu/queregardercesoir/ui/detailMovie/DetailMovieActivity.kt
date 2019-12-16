package com.sakharu.queregardercesoir.ui.detailMovie

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.sakharu.queregardercesoir.ui.FullscreenActivity
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.activity_detail_movie.*
import java.text.SimpleDateFormat
import java.util.*

class DetailMovieActivity : AppCompatActivity()
{
    private lateinit var detailMovieViewModel: DetailMovieViewModel
    private lateinit var observer:Observer<List<Genre>>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        detailMovieViewModel = ViewModelProvider(this, ViewModelFactory()).get(DetailMovieViewModel::class.java)

        observer = Observer {
            afficherGenres(it)
        }

        val id = intent.getLongExtra(EXTRA_MOVIE_ID,-1)
        if (id==-1L)
        //TODO FERMER ACTIVITE AFFICHER ERREUR
            finish()
        else
        {
            detailMovieViewModel.getMovieById(id).observe(this, Observer<Movie> {
                afficherMovie(this,it)
                //detailMovieViewModel.getGenresFromMovie(it).removeObserver(observer)
                detailMovieViewModel.getGenresFromMovie(it).observe(this,observer)
            })

        }
        backButtonMovieDetail.setOnClickListener{finish()}
    }

    private fun afficherMovie(context: Context, movie: Movie)
    {
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
                .putExtra(EXTRA_IMAGE_URL,movie.posterImg).putExtra(EXTRA_TYPE_IMAGE, TYPE_POSTER))
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
                .putExtra(EXTRA_IMAGE_URL,movie.backdropImg).putExtra(EXTRA_TYPE_IMAGE, TYPE_BACKDROP))
        }

        titleMovieDetail.text = movie.title

        overviewMovieDetail.text = getString(R.string.overviewPrefix,movie.overview)

        popularityMovieDetail.text = getString(R.string.popularityPrefix,movie.popularity)

        if (movie.vote_average!=null && movie.vote_average!=0.0)
        {
            textAverageVoteMovieDetail.text = getString(R.string.voteAveragePrefix,movie.vote_average!!)
            progressBarAverageVoteMovieDetail.progress = movie.vote_average!!.toInt()
            progressBarAverageVoteMovieDetail.max = 10
            textAverageVoteMovieDetail.show()
            progressBarAverageVoteMovieDetail.show()
        }
        else
        {
            textAverageVoteMovieDetail.hide()
            progressBarAverageVoteMovieDetail.hide()
        }

        if (movie.vote_count!=null && movie.vote_count!=0)
        {
            voteCountMovieDetail.show()
            voteCountMovieDetail.text = getString(R.string.voteCountPrefix,movie.vote_count)
        }
        else
            voteCountMovieDetail.hide()

        if (!movie.original_title.isNullOrEmpty())
        {
            originalTitleMovieDetail.show()
            originalTitleMovieDetail.text = getString(R.string.originalTitlePrefix,movie.original_title)
        }
        else
            originalTitleMovieDetail.hide()

        if (!movie.releaseDate.isNullOrEmpty())
        {
            val americanDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(movie.releaseDate!!)
            val date = SimpleDateFormat("dd MMMM yyyy",Locale.getDefault()).format(americanDate!!)
            releaseDateMovieDetail.text = getString(R.string.releaseDatePrefix,date)
            releaseDateMovieDetail.show()
        }
        else
            releaseDateMovieDetail.hide()

    }

    private fun afficherGenres(listGenre:List<Genre>)
    {
        val names = listGenre.joinToString { it.name }
        if (names.isNotEmpty())
            genresMovieDetail.text = getString(R.string.genrePrefix,names)
        else
            genresMovieDetail.hide()
    }
}
