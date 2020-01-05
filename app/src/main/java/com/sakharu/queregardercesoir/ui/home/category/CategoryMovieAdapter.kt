package com.sakharu.queregardercesoir.ui.home.category

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.ui.movieGridCategory.littleMovie.LittleMovieAdapter
import com.sakharu.queregardercesoir.ui.movieGridCategory.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.util.NUMBER_OF_CATEGORIES

class CategoryMovieAdapter(private var movieListInArrayList: ArrayList<CategoryAndList> = ArrayList(NUMBER_OF_CATEGORIES),
                           private var onMovieClickListener: OnMovieClickListener?=null,
                           private var onCategoryMovieClickListener: OnCategoryMovieClickListener?=null)
    : RecyclerView.Adapter<CategoryMovieHolder>()
{
    //on rempli la liste avec 4 catégories et films vides afin de pouvoir manipuler
    //plus facilement les positions ensuite
    init
    {
        for (i in 0 until NUMBER_OF_CATEGORIES)
            movieListInArrayList.add(CategoryAndList(Category(null,""), arrayListOf()))
    }

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
            layoutAnimation = AnimationUtils.loadLayoutAnimation(holder.itemView.context, R.anim.layout_animation_slide_from_right)
            scheduleLayoutAnimation()
            adapter = LittleMovieAdapter(movieListInArrayList[position].movieList.toMutableList(), onMovieClickListener=onMovieClickListener)
        }

        holder.itemView.setOnClickListener{
           onCategoryMovieClickListener?.onCategoryMovieClick(category)
        }

        holder.seeMoreIB.setOnClickListener{
            onCategoryMovieClickListener?.onCategoryMovieClick(category)
        }
    }

    fun refreshOrAddACategory(category: Category, position: Int, newList:List<Movie>)
    {
        //ajout de la catégorie et des films
        if (this.movieListInArrayList[position].movieList.isEmpty())
        {
            this.movieListInArrayList[position] = CategoryAndList(category,newList)
            notifyItemChanged(position)
        }
        //refresh de la catégorie
        else
        {
            if (newList.isNotEmpty())
            {
                //si la taille de la liste des films à changé ou que les derniers id sont pas les mêmes
                //on part du principe que la liste a changé et qu'l faut refresh
                if (position<this.movieListInArrayList.size ||
                    this.movieListInArrayList[position].movieList.isNotEmpty() ||
                    this.movieListInArrayList[position].movieList.last().id != newList.last().id)
                {
                    this.movieListInArrayList[position].movieList = newList.toMutableList()
                    notifyItemChanged(position)
                }
            }
        }
    }

    data class CategoryAndList(var category: Category,var movieList: List<Movie>)
}