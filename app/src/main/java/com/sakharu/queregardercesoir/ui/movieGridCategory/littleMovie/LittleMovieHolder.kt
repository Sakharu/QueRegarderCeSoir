package com.sakharu.queregardercesoir.ui.movieGridCategory.littleMovie

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R

class LittleMovieHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
{
    var posterImgIV : ImageView = itemView.findViewById(R.id.posterItemMovieHome)
    var favoriteIcon : ImageButton = itemView.findViewById(R.id.addToFavoriteLittleMovie)
}