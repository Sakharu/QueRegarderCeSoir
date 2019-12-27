package com.sakharu.queregardercesoir.ui.userList.listUserList

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R

class UserListHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
{
    var name : TextView = itemView.findViewById(R.id.nameUserList)
    var noMovieInList : TextView = itemView.findViewById(R.id.noMovieInUserList)
    var recyclerMovie : RecyclerView = itemView.findViewById(R.id.recyclerMovieUserList)
    var iconList : ImageView = itemView.findViewById(R.id.iconUserList)
}