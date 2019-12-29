package com.sakharu.queregardercesoir.ui.detailMovie.chooseUserList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.UserList

class ChooseUserListAdapter(private var userList: MutableList<UserList>,
                            private var selectedUserList:MutableList<UserList>) : RecyclerView.Adapter<ChooseUserListHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseUserListHolder =
        ChooseUserListHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_choose_userlist, parent, false))

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: ChooseUserListHolder, position: Int)
    {
        val list = userList[position]
        holder.listNameTV.text = list.name
        holder.listNameTV.isChecked = false
    }
}

class ChooseUserListHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
{
    val listNameTV = itemView.findViewById<CheckedTextView>(R.id.checkedTextView)
}