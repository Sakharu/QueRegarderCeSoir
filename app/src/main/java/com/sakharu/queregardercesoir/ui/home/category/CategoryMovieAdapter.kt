package com.sakharu.queregardercesoir.ui.home.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.LittleMovieAdapter

class CategoryMovieAdapter(private var movieListInArrayList:ArrayList<CategoryAndList> = arrayListOf())
    : RecyclerView.Adapter<CategoryMovieHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMovieHolder =
        CategoryMovieHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category_home, parent, false))

    override fun getItemCount(): Int = movieListInArrayList.size

    override fun onBindViewHolder(holder: CategoryMovieHolder, position: Int)
    {
        val category = movieListInArrayList[position].category
        holder.titleCategory.text = category.name
        holder.recyclerLittleMovie.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            setHasFixedSize(true)
            adapter = LittleMovieAdapter(movieListInArrayList[position].movieList)
        }
    }


    fun refreshOrAddACategory(category: Category, position: Int, newList:List<Movie>)
    {
        //ajout de la catégorie
        if (this.movieListInArrayList.isEmpty() || position>=movieListInArrayList.size)
        {
            this.movieListInArrayList.add(CategoryAndList(category, newList.toMutableList()))
            movieListInArrayList.sortBy { it.category.id }
            notifyItemInserted(position)
        }
        //refresh de la catégorie
        else
        {
            //on considère que si le premier id de chaque liste
            if (newList.isNotEmpty() && this.movieListInArrayList[position].movieList.last().id != newList.last().id)
            {
                this.movieListInArrayList[position] = CategoryAndList(category, newList.toMutableList())
                notifyItemChanged(position)
            }
        }
    }

    data class CategoryAndList(var category: Category,var movieList: MutableList<Movie>)
}