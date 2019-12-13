package com.sakharu.queregardercesoir.ui.home.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.ui.home.category.littleMovie.LittleMovieAdapter

class CategoryMovieAdapter(context: Context,
                           private var movieListInArrayList:ArrayList<CategoryAndList>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var inflater : LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CategoryMovieHolder(inflater.inflate(R.layout.item_category_home, parent, false))

    override fun getItemCount(): Int = movieListInArrayList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        holder as CategoryMovieHolder
        val category = movieListInArrayList[position].category
        holder.titleCategory.text = category.name
        holder.recyclerLittleMovie.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            setHasFixedSize(true)
            adapter = LittleMovieAdapter(context,movieListInArrayList[position].movieList)
        }
    }


    fun refreshOrAddACategory(category: Category, position: Int, newList:List<Movie>)
    {
        if (this.movieListInArrayList.isEmpty() || position>=movieListInArrayList.size)
        {
            this.movieListInArrayList.add(CategoryAndList(category,newList))
            movieListInArrayList.sortBy { it.category.id }
            notifyItemInserted(position)
        }
        else
        {
            this.movieListInArrayList[position] = CategoryAndList(category,newList)
            notifyItemChanged(position)
        }
    }

    data class CategoryAndList(var category: Category,var movieList: List<Movie>)
}