package com.sakharu.queregardercesoir.ui.detailMovie

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.MovieService
import com.sakharu.queregardercesoir.util.EXTRA_MOVIE_ID
import com.sakharu.queregardercesoir.util.ViewModelFactory
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovieActivity : AppCompatActivity()
{
    private lateinit var detailMovieViewModel: DetailMovieViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        detailMovieViewModel = ViewModelProvider(this, ViewModelFactory()).get(DetailMovieViewModel::class.java)


        val id = intent.getLongExtra(EXTRA_MOVIE_ID,-1)
        if (id==-1L)
        //TODO FERMER ACTIVITE AFFICHER ERREUR
        else
            detailMovieViewModel.getMovieById(id).observe(this, Observer<Movie> {
                afficherMovie(this,it)
            })

        backButtonMovieDetail.setOnClickListener{finish()}
    }

    private fun afficherMovie(context: Context, movie: Movie)
    {
        //Affichage du poster
        if (!movie.posterImg.isNullOrEmpty())
            Glide.with(context)
                .load(MovieService.IMAGE_PREFIX + movie.posterImg)
                .placeholder(R.drawable.film_poster_placeholder)
                .fallback(R.drawable.film_poster_placeholder)
                .error(R.drawable.film_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgPosterMovieDetail)
        else
            Glide.with(context).load(R.drawable.film_poster_placeholder).into(imgPosterMovieDetail)

        //Affichage de l'image de fond
        if (!movie.posterImg.isNullOrEmpty())
            Glide.with(context)
                .load(MovieService.IMAGE_PREFIX + movie.backdropImg)
                .placeholder(R.drawable.large_movie_placeholder)
                .fallback(R.drawable.large_movie_placeholder)
                .error(R.drawable.large_movie_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgBackdropMovieDetail)
        else
            Glide.with(context).load(R.drawable.large_movie_placeholder).into(imgBackdropMovieDetail)


        titleMovieDetail.text = movie.title

        overviewMovieDetail.text = movie.overview

        //genresMovieDetail.text = movie.genresId

        popularityMovieDetail.text = movie.popularity.toString()

        if (movie.vote_average!=null)
        {
            textAverageVoteMovieDetail.text = movie.vote_average!!.toString()
            progressBarAverageVoteMovieDetail.progress = movie.vote_average!!.toInt()
            progressBarAverageVoteMovieDetail.max = 10
        }

        if (movie.vote_count!=null)
            voteCountMovieDetail.text = movie.vote_count.toString()

        if (movie.original_title!=null)
            originalTitleMovieDetail.text = movie.original_title

        if (!movie.releaseDate.isNullOrEmpty())
            releaseDateMovieDetail.text = movie.releaseDate



    }
}
