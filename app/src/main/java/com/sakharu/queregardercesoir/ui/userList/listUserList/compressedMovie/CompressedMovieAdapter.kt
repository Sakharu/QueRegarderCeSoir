package com.sakharu.queregardercesoir.ui.userList.listUserList.compressedMovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService

class CompressedMovieAdapter(private var movieList: MutableList<Movie>)

    : RecyclerView.Adapter<CompressedMovieHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompressedMovieHolder =
        CompressedMovieHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_compressed_movie, parent, false))

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: CompressedMovieHolder, position: Int)
    {
        val movie = movieList[position]
        if (!movie.posterImg.isNullOrEmpty())
            Glide.with(holder.itemView)
                .load(MovieService.IMAGE_PREFIX_POSTER + movie.posterImg)
                .placeholder(R.drawable.film_poster_placeholder)
                .fallback(R.drawable.film_poster_placeholder)
                .error(R.drawable.film_poster_placeholder)
                .into(holder.posterImgIV)
        else
            Glide.with(holder.itemView).load(R.drawable.film_poster_placeholder).into(holder.posterImgIV)
    }

    fun setData(newMovies:List<Movie>)
    {
        this.movieList.addAll(newMovies.filter { !movieList.contains(it) })
        notifyDataSetChanged()
    }
}