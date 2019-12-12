package com.sakharu.queregardercesoir.ui.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R

class BigMovieHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
{
    var name : TextView = itemView.findViewById(R.id.nameMoviePopHome)
    var overview : TextView = itemView.findViewById(R.id.genreMoviePopome)
    var poster : ImageView = itemView.findViewById(R.id.posterMoviePopHome)
}