package com.sakharu.queregardercesoir.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.R

class BigMovieAdapter(private var context: Context, private var listeMovie:List<Movie> = listOf())
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var inflater : LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = BigMovieHolder(
        inflater.inflate(
            R.layout.item_big_movie,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = listeMovie.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        holder as BigMovieHolder
        val film = listeMovie[position]
        holder.name.text = film.title
        holder.overview.text = "#Action, #Aventure, #Combat, #Violence, #Science-fiction, #Action, #Aventure"
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w185/"+film.posterImg)
            .placeholder(R.drawable.film_poster_placeholder)
            .fallback(R.drawable.film_poster_placeholder)
            .error(R.drawable.film_poster_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.poster)

    }

    fun setData(newMovies : List<Movie>)
    {
        this.listeMovie = newMovies
        notifyDataSetChanged()
    }
}