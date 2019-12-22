package com.sakharu.queregardercesoir.ui.home.category

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R

class CategoryMovieHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
{
    var recyclerLittleMovie : RecyclerView = itemView.findViewById(R.id.recyclerCategoryHome)
    var titleCategory : TextView = itemView.findViewById(R.id.titleCategoryHome)
    var seeMoreIB : ImageButton = itemView.findViewById(R.id.seeMoreImageButton)

}