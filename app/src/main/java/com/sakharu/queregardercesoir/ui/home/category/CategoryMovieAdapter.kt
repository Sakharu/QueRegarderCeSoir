package com.sakharu.queregardercesoir.ui.home.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sakharu.queregardercesoir.data.locale.movie.Movie
import com.sakharu.queregardercesoir.data.remote.MovieService
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.category.Category
import com.sakharu.queregardercesoir.ui.home.BigMovieHolder
import com.sakharu.queregardercesoir.ui.home.category.littleMovie.LittleMovieAdapter
import java.util.concurrent.CancellationException

class CategoryMovieAdapter(context: Context, private var categoryList:List<Category>,
                           private var movieList:List<List<Movie>>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var inflater : LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CategoryMovieHolder(inflater.inflate(R.layout.item_category_home, parent, false))

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        holder as CategoryMovieHolder
        val category = categoryList[position]
        holder.titleCategory.text = category.name
        holder.recyclerLittleMovie.apply {
            layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
            setHasFixedSize(true)
            adapter = LittleMovieAdapter(context,movieList[position])
        }

    }

    fun setData(categoryList:List<Category>, movieList:List<List<Movie>>)
    {
        this.categoryList = categoryList
        this.movieList = movieList
        notifyDataSetChanged()
    }
}