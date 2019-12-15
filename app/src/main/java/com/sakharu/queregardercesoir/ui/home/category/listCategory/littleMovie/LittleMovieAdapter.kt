package com.sakharu.queregardercesoir.ui.home.category.listCategory.littleMovie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.MovieService
import com.sakharu.queregardercesoir.data.remote.MovieService.Companion.NUMBER_MOVIES_RETRIEVE_BY_REQUEST
import com.sakharu.queregardercesoir.ui.detailMovie.DetailMovieActivity
import com.sakharu.queregardercesoir.ui.home.category.detail.DetailCategoryFragment
import com.sakharu.queregardercesoir.util.ACTION_LOAD_MORE_CATEGORY_DETAIL
import com.sakharu.queregardercesoir.util.EXTRA_MOVIE_ID
import com.sakharu.queregardercesoir.util.EXTRA_PAGE
import kotlin.math.ceil


class LittleMovieAdapter(var context: Context, private var listeMovie: MutableList<Movie>,
                         private var isForDetailCategory:Boolean=false)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        LittleMovieHolder(inflater.inflate(R.layout.item_movie_home, parent, false))

    override fun getItemCount(): Int = listeMovie.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        holder as LittleMovieHolder
        val film = listeMovie[position]
        if (!film.posterImg.isNullOrEmpty())
            Glide.with(holder.itemView)
                .load(MovieService.IMAGE_PREFIX + film.posterImg)
                .placeholder(R.drawable.film_poster_placeholder)
                .fallback(R.drawable.film_poster_placeholder)
                .error(R.drawable.film_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.posterImgIV)
        else
            Glide.with(holder.itemView).load(R.drawable.film_poster_placeholder).into(holder.posterImgIV)

        /*
        On informe le fragment lorsque l'on a affiché l'équivalent d'une page afin qu'il puisse
        lancer la récupération de la page suivante pour que ça soit transparent pour l'utilisateur
         */

        if (isForDetailCategory && (position+1) %NUMBER_MOVIES_RETRIEVE_BY_REQUEST == 0 && !DetailCategoryFragment.isLoading )
        {
            val page = ceil(position.toDouble() / NUMBER_MOVIES_RETRIEVE_BY_REQUEST).toInt()+1
            if (!DetailCategoryFragment.listePageChargee.contains(page))
            {
                DetailCategoryFragment.isLoading = true
                LocalBroadcastManager.getInstance(holder.itemView.context)
                    .sendBroadcast(Intent(ACTION_LOAD_MORE_CATEGORY_DETAIL).putExtra(EXTRA_PAGE,page))
            }
        }

        holder.itemView.setOnClickListener{
            holder.itemView.context.startActivity(Intent(holder.itemView.context, DetailMovieActivity::class.java)
                .putExtra(EXTRA_MOVIE_ID, film.id))
        }
    }

    fun addData(newMovies:List<Movie>)
    {
        /*
        On ajoute à la liste actuelle les nouveaux films récupérés
         */
        if (newMovies.isNotEmpty())
        {
            val oldPosition = listeMovie.size
            this.listeMovie.addAll(newMovies.filter { !listeMovie.contains(it) })
            notifyItemRangeInserted(oldPosition, listeMovie.size)
        }
    }
}