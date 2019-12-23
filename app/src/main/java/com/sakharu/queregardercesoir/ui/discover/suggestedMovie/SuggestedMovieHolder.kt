package com.sakharu.queregardercesoir.ui.discover.suggestedMovie

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R

class SuggestedMovieHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
{
    var posterMovie : ImageView = itemView.findViewById(R.id.posterSuggestedMovie)
    var genreMovie : TextView = itemView.findViewById(R.id.genreSuggestedMovie)
    var overviewMovie : TextView = itemView.findViewById(R.id.overviewSuggestedMovie)
    var voteMovie : TextView = itemView.findViewById(R.id.voteSuggestedMovie)
    var titleMovie : TextView = itemView.findViewById(R.id.titleSuggestedMovie)
}