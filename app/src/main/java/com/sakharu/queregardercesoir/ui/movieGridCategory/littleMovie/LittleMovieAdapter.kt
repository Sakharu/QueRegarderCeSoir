package com.sakharu.queregardercesoir.ui.movieGridCategory.littleMovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService
import com.sakharu.queregardercesoir.ui.base.OnBottomReachedListener
import com.sakharu.queregardercesoir.ui.userList.OnFavoriteClickListener
import com.sakharu.queregardercesoir.util.show


class LittleMovieAdapter(
    private var movieList: MutableList<Movie>,
    private var favoriteMovieListId : MutableList<Long>? = null,
    private var onMovieClickListener: OnMovieClickListener? = null,
    private var onBottomReachedListener: OnBottomReachedListener? = null,
    private var onFavoriteClickListener: OnFavoriteClickListener? = null)

    : RecyclerView.Adapter<LittleMovieHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LittleMovieHolder =
        LittleMovieHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_little_movie, parent, false))

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: LittleMovieHolder, position: Int)
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

        if (position==movieList.size-1)
            onBottomReachedListener?.onBottomReached()

        ViewCompat.setTransitionName(holder.posterImgIV,movie.id.toString())

        holder.itemView.setOnClickListener{
            onMovieClickListener?.onClickOnMovie(movie,holder.posterImgIV)
        }

        if (favoriteMovieListId!=null)
        {
            if (favoriteMovieListId!!.contains(movie.id))
                holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_red_24dp)
            else
                holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border_red_24dp)

            holder.favoriteIcon.show()
            holder.favoriteIcon.setOnClickListener{
                onFavoriteClickListener?.onFavoriteClick(movie)
            }
        }
    }

    fun refreshFavoriteIcon(idMovie: Long,isFavorite:Boolean)
    {
        val position = movieList.indexOfFirst { it.id==idMovie }
        if (position!=-1)
        {
            if (isFavorite)
                this.favoriteMovieListId!!.add(idMovie)
            else
                this.favoriteMovieListId!!.remove(idMovie)
            notifyItemChanged(position)
        }
    }

    fun setData(newMovies:List<Movie>)
    {
        this.movieList.addAll(newMovies.filter { !movieList.contains(it) })
        notifyDataSetChanged()
    }

    fun setFavoriteMoviesId(favoriteMovieListId: MutableList<Long>)
    {
        this.favoriteMovieListId=favoriteMovieListId
        notifyDataSetChanged()
    }

    fun addData(newMovies:List<Movie>) : Int
    {
        /*
        On ajoute à la liste actuelle les nouveaux films récupérés
         */
        val oldPosition = movieList.size
        if (newMovies.isNotEmpty())
        {
            this.movieList.addAll(newMovies.filter { !movieList.contains(it) })
            if (oldPosition!=movieList.size)
                notifyItemRangeInserted(oldPosition, movieList.size)
        }
        return oldPosition
    }
}