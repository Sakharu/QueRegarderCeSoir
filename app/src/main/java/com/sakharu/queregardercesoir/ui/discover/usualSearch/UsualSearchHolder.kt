package com.sakharu.queregardercesoir.ui.discover.usualSearch

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R

class UsualSearchHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
{
    var titleSearch : TextView = itemView.findViewById(R.id.titleUsualSearch)
    var subTitleSearch : TextView = itemView.findViewById(R.id.subtitleUsualSearch)
}