package com.sakharu.queregardercesoir.ui.search.title

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.util.hide
import com.sakharu.queregardercesoir.util.setInvisible
import com.sakharu.queregardercesoir.util.show
import java.text.SimpleDateFormat
import java.util.*


class MovieResultTitleSearchAdapter(private var listeMovie: MutableList<Movie>,
                                    private var onMovieClickListener: OnMovieClickListener?=null)
    : RecyclerView.Adapter<MovieResultTitleSearchHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieResultTitleSearchHolder =
        MovieResultTitleSearchHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movie_result_search,
                parent,
                false
            )
        )

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
            holder.voteAverage.text = holder.voteAverage.context.getString(R.string.averageVoteOnTotalVote,
                movie.vote_average,movie.vote_count)
            holder.voteAverage.show()
        }
        else
            holder.voteAverage.setInvisible()

        holder.titleMovie.text = movie.title

        if (movie.releaseDate!=null)
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
                holder.yearMovie.hide()
            }
        }
        else
            holder.yearMovie.setInvisible()


        holder.itemView.setOnClickListener{
            onMovieClickListener?.onClickOnMovie(listeMovie[position],holder.posterImg)
        }

        //TODO PAGINATION ET TRI
    }

    fun addData(newMovies:List<Movie>)
    {
        /*
        On ajoute à la liste actuelle les nouveaux films récupérés
         */
        //this.listeMovie = ArrayList(newMovies)
        //notifyDataSetChanged()

        if (newMovies.isNotEmpty())
        {
            val oldPosition = listeMovie.size
            this.listeMovie.addAll(newMovies.filter { !listeMovie.contains(it) })
            notifyItemRangeInserted(oldPosition, listeMovie.size)
        }
    }
}
