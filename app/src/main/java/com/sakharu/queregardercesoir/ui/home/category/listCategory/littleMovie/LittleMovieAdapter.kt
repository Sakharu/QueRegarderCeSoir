package com.sakharu.queregardercesoir.ui.home.category.listCategory.littleMovie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.MovieService
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.ui.home.category.detail.DetailCategoryFragment
import com.sakharu.queregardercesoir.util.ACTION_LOAD_MORE_CATEGORY_DETAIL

class LittleMovieAdapter(var context: Context, private var listeMovie: List<Movie>,
                         private var isForDetailCategory:Boolean=false)
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
                .into(holder.posterImgIV)
        else
            Glide.with(holder.itemView).load(R.drawable.film_poster_placeholder).into(holder.posterImgIV)

        //On lance le chargement nouveaux films lorsque l'on arrive au bout de la liste
        if (isForDetailCategory && position == listeMovie.size-1 && !DetailCategoryFragment.isLoading)
        {
            DetailCategoryFragment.isLoading = true
            LocalBroadcastManager.getInstance(holder.itemView.context).sendBroadcast(Intent(ACTION_LOAD_MORE_CATEGORY_DETAIL))
        }
    }

    fun setData(newMovies: List<Movie>)
    {
        this.listeMovie = newMovies
        notifyDataSetChanged()
    }
}