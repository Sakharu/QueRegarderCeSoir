package com.sakharu.queregardercesoir.ui.discover.suggestedMovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService
import com.sakharu.queregardercesoir.ui.movieGridCategory.littleMovie.OnMovieClickListener

class SuggestedMovieAdapter(private var movieList: MutableList<Movie>,
                            private var genresList:List<Genre>,
                            private var onMovieClickListener: OnMovieClickListener?=null)
    : RecyclerView.Adapter<SuggestedMovieHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedMovieHolder =
        SuggestedMovieHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_suggestion_movie, parent, false))

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: SuggestedMovieHolder, position: Int)
    {
        if (movieList.isNotEmpty() && genresList.isNotEmpty())
        {
            val movie = movieList[position]
            if (!movie.posterImg.isNullOrEmpty())
                Glide.with(holder.itemView)
                    .load(MovieService.IMAGE_PREFIX_POSTER + movie.posterImg)
                    .placeholder(R.drawable.film_poster_placeholder)
                    .fallback(R.drawable.film_poster_placeholder)
                    .error(R.drawable.film_poster_placeholder)
                    .into(holder.posterMovie)
            else
                Glide.with(holder.itemView).load(R.drawable.film_poster_placeholder).into(holder.posterMovie)

            holder.overviewMovie.text = movie.overview

            holder.genreMovie.text = genresList.filter { movie.genresId.contains(it.id) }.joinToString { it.name }

            holder.titleMovie.text = movie.title

            if (movie.vote_average!=null)
                holder.voteMovie.text = holder.itemView.context.getString(R.string.percent,(movie.vote_average!! *10).toInt())

            holder.itemView.setOnClickListener{
                onMovieClickListener?.onClickOnMovie(movie,holder.posterMovie)
            }
        }

    }

    fun addGenres(genres:List<Genre>)
    {
        this.genresList=genres
        if (this.movieList.isNotEmpty())
            notifyDataSetChanged()
    }

    /*
    On ajoute à la liste actuelle les nouveaux films récupérés
    */
    fun addMovie(newMovies:List<Movie>)
    {
        if (newMovies.isNotEmpty())
        {
            val oldPosition = movieList.size
            this.movieList.addAll(newMovies.filter { !movieList.contains(it) })
            /*
                si on notifie que des éléments ont été insérés alors qu'il n'y avait aucun élément
                si on notifie que des éléments ont été insérés alors qu'il n'y avait aucun élément
                auparavant, le recyclerview ne se rafraîchit pas, on recharge donc toute la liste dans
                ces cas là
            */

            notifyItemRangeInserted(oldPosition, movieList.size)
        }
    }


}