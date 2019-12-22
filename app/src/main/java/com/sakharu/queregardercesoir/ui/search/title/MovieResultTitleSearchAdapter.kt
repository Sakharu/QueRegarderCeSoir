package com.sakharu.queregardercesoir.ui.search.title

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.util.OnBottomReachedListener
import com.sakharu.queregardercesoir.util.setInvisible
import com.sakharu.queregardercesoir.util.show
import java.text.SimpleDateFormat
import java.util.*


class MovieResultTitleSearchAdapter(private var listeMovie: MutableList<Movie>,
                                    private var genresList : List<Genre>,
                                    private var onMovieClickListener: OnMovieClickListener?=null,
                                    private var onBottomReachedListener: OnBottomReachedListener?=null)
    : RecyclerView.Adapter<MovieResultTitleSearchHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieResultTitleSearchHolder =
        MovieResultTitleSearchHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie_result_search, parent, false))

    override fun getItemCount(): Int = listeMovie.size

    override fun onBindViewHolder(holder: MovieResultTitleSearchHolder, position: Int)
    {
        val movie = listeMovie[position]
        if (!movie.posterImg.isNullOrEmpty())
            Glide.with(holder.itemView)
                .load(MovieService.IMAGE_PREFIX_POSTER + movie.posterImg)
                .placeholder(R.drawable.film_poster_placeholder)
                .fallback(R.drawable.film_poster_placeholder)
                .error(R.drawable.film_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.posterImg)
        else
            Glide.with(holder.itemView).load(R.drawable.film_poster_placeholder).into(holder.posterImg)

        if (movie.vote_average!=null && movie.vote_average!=0.0 && movie.vote_count!=null && movie.vote_count!=0)
        {

            //pour l'orthographe
            if (movie.vote_count!!>1)
                holder.voteAverage.text = holder.voteAverage.context.getString(R.string.averageVoteOnTotalVotes,
                    movie.vote_average,movie.vote_count)
            else
                holder.voteAverage.text = holder.voteAverage.context.getString(R.string.averageVoteOnTotalVote,
                    movie.vote_average,movie.vote_count)
            holder.voteAverage.show()
        }
        else
            holder.voteAverage.setInvisible()

        holder.titleMovie.text = movie.title

        val genresNameList = genresList.filter { movie.genresId.contains(it.id) }.joinToString { it.name }
        holder.genresMovie.text = genresNameList

        if (!movie.releaseDate.isNullOrEmpty())
        {
            try
            {
                val americanDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(movie.releaseDate!!)
                val date = SimpleDateFormat("yyyy", Locale.getDefault()).format(americanDate!!)
                holder.yearMovie.text = date
                holder.yearMovie.show()
            }
            catch (e:Exception)
            {
                e.printStackTrace()
                holder.yearMovie.setInvisible()
            }
        }
        else
            holder.yearMovie.setInvisible()

        //on active la methode du listener afin de le prévenir qu'on a atteint le bas de la page
        if (position==listeMovie.size-1)
            onBottomReachedListener?.onBottomReached()

        holder.itemView.setOnClickListener{
            onMovieClickListener?.onClickOnMovie(listeMovie[position],holder.posterImg)
        }
    }

    fun addGenres(genres:List<Genre>)
    {
        this.genresList=genres
    }

    /*
    On ajoute à la liste actuelle les nouveaux films récupérés
    */
    fun addMovie(newMovies:List<Movie>)
    {
        if (newMovies.isNotEmpty())
        {
            val oldPosition = listeMovie.size
            this.listeMovie.addAll(newMovies.filter { !listeMovie.contains(it) })
            /*
                si on notifie que des éléments ont été insérés alors qu'il n'y avait aucun élément
                si on notifie que des éléments ont été insérés alors qu'il n'y avait aucun élément
                auparavant, le recyclerview ne se rafraîchit pas, on recharge donc toute la liste dans
                ces cas là
            */
            if (oldPosition==0)
                notifyDataSetChanged()
            else
                notifyItemRangeInserted(oldPosition, listeMovie.size)
        }
    }

    fun clearAllMovies()
    {
        this.listeMovie.clear()
        notifyDataSetChanged()
    }
}
