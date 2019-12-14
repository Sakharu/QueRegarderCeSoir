package com.sakharu.queregardercesoir.ui.home.category.listCategory.littleMovie

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.MovieService

class LittleMovieHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
{
    var posterImgIV : ImageView = itemView.findViewById(R.id.posterItemMovieHome)

    /*
    fun bindMovie(movie: Movie)
    {
        with(movie)
        {
            if (movie.posterImg.isNotEmpty())
                Glide.with(itemView)
                    .load(MovieService.IMAGE_PREFIX + movie.posterImg)
                    .placeholder(R.drawable.film_poster_placeholder)
                    .fallback(R.drawable.film_poster_placeholder)
                    .error(R.drawable.film_poster_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(posterImgIV)
            else
                Glide.with(itemView).load(R.drawable.film_poster_placeholder).into(posterImg)
        }
    }
     */
}