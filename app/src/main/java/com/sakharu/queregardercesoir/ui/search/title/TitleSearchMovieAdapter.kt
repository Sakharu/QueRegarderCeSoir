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
import com.sakharu.queregardercesoir.ui.movieGridCategory.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.ui.base.OnBottomReachedListener
import com.sakharu.queregardercesoir.util.setInvisible
import com.sakharu.queregardercesoir.util.show
import java.text.SimpleDateFormat
import java.util.*


class TitleSearchMovieAdapter(private var listeMovie: MutableList<Movie>,
                              private var genresList : List<Genre>,
                              private var onMovieClickListener: OnMovieClickListener?=null,
                              private var onBottomReachedListener: OnBottomReachedListener?=null)
    : RecyclerView.Adapter<TitleSearchMovieHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleSearchMovieHolder =
        TitleSearchMovieHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie_result_search, parent, false))

    override fun getItemCount(): Int = listeMovie.size

    override fun onBindViewHolder(movieHolder: TitleSearchMovieHolder, position: Int)
    {
        val movie = listeMovie[position]
        if (!movie.posterImg.isNullOrEmpty())
            Glide.with(movieHolder.itemView)
                .load(MovieService.IMAGE_PREFIX_POSTER + movie.posterImg)
                .placeholder(R.drawable.film_poster_placeholder)
                .fallback(R.drawable.film_poster_placeholder)
                .error(R.drawable.film_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(movieHolder.posterImg)
        else
            Glide.with(movieHolder.itemView).load(R.drawable.film_poster_placeholder).into(movieHolder.posterImg)

        if (movie.vote_average!=null && movie.vote_average!=0.0 && movie.vote_count!=null && movie.vote_count!=0)
        {
            //pour l'orthographe
            if (movie.vote_count!!>1)
                movieHolder.voteAverage.text = movieHolder.voteAverage.context.getString(R.string.averageVoteOnTotalVotes,
                    (movie.vote_average!!*10).toInt(),movie.vote_count)
            else
                movieHolder.voteAverage.text = movieHolder.voteAverage.context.getString(R.string.averageVoteOnTotalVote,
                    (movie.vote_average!!*10).toInt(),movie.vote_count)
            movieHolder.voteAverage.show()
        }
        else
            movieHolder.voteAverage.setInvisible()

        movieHolder.titleMovie.text = movie.title

        movieHolder.genresMovie.text = genresList.filter { movie.genresId.contains(it.id) }.joinToString { it.name }

        if (!movie.releaseDate.isNullOrEmpty())
        {
            try
            {
                val americanDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(movie.releaseDate!!)
                val date = SimpleDateFormat("yyyy", Locale.getDefault()).format(americanDate!!)
                movieHolder.yearMovie.text = date
                movieHolder.yearMovie.show()
            }
            catch (e:Exception)
            {
                e.printStackTrace()
                movieHolder.yearMovie.setInvisible()
            }
        }
        else
            movieHolder.yearMovie.setInvisible()

        //on active la methode du listener afin de le prévenir qu'on a atteint le bas de la page
        if (position==listeMovie.size-1)
            onBottomReachedListener?.onBottomReached()

        movieHolder.itemView.setOnClickListener{
            onMovieClickListener?.onClickOnMovie(listeMovie[position],movieHolder.posterImg)
        }
    }

    fun addGenres(genres:List<Genre>)
    {
        this.genresList=genres
    }

    /*
    On ajoute à la liste actuelle les nouveaux films récupérés
    */
    //on retourne true si on a pas assez ajouté d'élement sinon false
    fun addMovie(newMovies:List<Movie>) : Boolean
    {
        val oldPosition = listeMovie.size
        if (newMovies.isNotEmpty())
        {
            this.listeMovie.addAll(newMovies.filter { !listeMovie.contains(it) })
            /*
                si on notifie que des éléments ont été insérés alors qu'il n'y avait aucun élément
                auparavant, le recyclerview ne se rafraîchit pas, on recharge donc toute la liste dans
                ces cas là
            */
            if (oldPosition!=listeMovie.size)
                notifyItemRangeInserted(oldPosition, listeMovie.size)

        }
        //si on a chargé moins de 10 nouveaux éléments, on retourne true afin de charger la page suivante si possible
        return oldPosition+10>listeMovie.size
    }

    fun clearAllMovies()
    {
        val oldSize = listeMovie.size
        this.listeMovie.clear()
        notifyItemRangeRemoved(0,oldSize)
    }
}
