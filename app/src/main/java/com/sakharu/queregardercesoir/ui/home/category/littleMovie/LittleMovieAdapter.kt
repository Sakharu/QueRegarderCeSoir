package com.sakharu.queregardercesoir.ui.home.category.littleMovie

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.MovieService
import com.sakharu.queregardercesoir.R

class LittleMovieAdapter(context: Context, private var listeMovie: List<Movie>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        LittleMovieHolder(inflater.inflate(R.layout.item_movie_home, parent, false))

    override fun getItemCount(): Int = listeMovie.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as LittleMovieHolder
        val film = listeMovie[position]
        if (film.posterImg.isNotEmpty())
            Glide.with(holder.itemView)
                .load(MovieService.IMAGE_PREFIX + film.posterImg)
                .placeholder(R.drawable.film_poster_placeholder)
                .fallback(R.drawable.film_poster_placeholder)
                .error(R.drawable.film_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.posterImg)
        else
            Glide.with(holder.itemView).load(R.drawable.film_poster_placeholder).into(holder.posterImg)
    }

    fun setData(newMovies: List<Movie>)
    {
        this.listeMovie = newMovies
        notifyDataSetChanged()
    }
}