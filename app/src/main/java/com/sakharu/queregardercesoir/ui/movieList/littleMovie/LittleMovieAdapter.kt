package com.sakharu.queregardercesoir.ui.movieList.littleMovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService
import com.sakharu.queregardercesoir.util.OnBottomReachedListener


class LittleMovieAdapter(
    private var listeMovie: MutableList<Movie>,
    private var onMovieClickListener: OnMovieClickListener? = null,
    private var onBottomReachedListener: OnBottomReachedListener? = null)

    : RecyclerView.Adapter<LittleMovieHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LittleMovieHolder =
        LittleMovieHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_little_movie, parent, false))

    override fun getItemCount(): Int = listeMovie.size

    override fun onBindViewHolder(holder: LittleMovieHolder, position: Int)
    {
        val film = listeMovie[position]
        if (!film.posterImg.isNullOrEmpty())
            Glide.with(holder.itemView)
                .load(MovieService.IMAGE_PREFIX_POSTER + film.posterImg)
                .placeholder(R.drawable.film_poster_placeholder)
                .fallback(R.drawable.film_poster_placeholder)
                .error(R.drawable.film_poster_placeholder)
                //.transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.posterImgIV)
        else
            Glide.with(holder.itemView).load(R.drawable.film_poster_placeholder).into(holder.posterImgIV)

        if (position==listeMovie.size-1)
            onBottomReachedListener?.onBottomReached()

        holder.itemView.setOnClickListener{
            onMovieClickListener?.onClickOnMovie(listeMovie[position],holder.posterImgIV)
        }
    }

    fun setData(newMovies:List<Movie>)
    {
        this.listeMovie.addAll(newMovies.filter { !listeMovie.contains(it) })
        notifyDataSetChanged()
    }

    fun addData(newMovies:List<Movie>) : Int
    {
        /*
        On ajoute à la liste actuelle les nouveaux films récupérés
         */
        val oldPosition = listeMovie.size
        if (newMovies.isNotEmpty())
        {
            this.listeMovie.addAll(newMovies.filter { !listeMovie.contains(it) })
            if (oldPosition!=listeMovie.size)
                notifyItemRangeInserted(oldPosition, listeMovie.size)
        }
        return oldPosition
    }
}