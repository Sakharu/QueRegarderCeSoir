package com.sakharu.queregardercesoir.ui.search.title

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R

class TitleSearchMovieHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
{
    var posterImg : ImageView = itemView.findViewById(R.id.posterItemMovieResultSearch)
    var titleMovie : TextView = itemView.findViewById(R.id.titleMovieResultSearch)
    var yearMovie : TextView = itemView.findViewById(R.id.releaseYearMovieResultSearch)
    var voteAverage : TextView = itemView.findViewById(R.id.voteAverageMovieResultSearch)
    var genresMovie : TextView = itemView.findViewById(R.id.genresMovieResultSearch)
}
