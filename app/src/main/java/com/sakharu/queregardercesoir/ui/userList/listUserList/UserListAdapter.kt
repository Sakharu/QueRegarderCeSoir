package com.sakharu.queregardercesoir.ui.userList.listUserList

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.model.UserList
import com.sakharu.queregardercesoir.ui.userList.listUserList.compressedMovie.CompressedMovieAdapter
import com.sakharu.queregardercesoir.util.NUMBER_OF_BASE_LIST
import com.sakharu.queregardercesoir.util.NUMBER_OF_CATEGORIES
import com.sakharu.queregardercesoir.util.hide
import com.sakharu.queregardercesoir.util.show

class UserListAdapter(private var listUserListAndMovie: MutableList<UserListAndMovieList> = arrayListOf())
    : RecyclerView.Adapter<UserListHolder>()
{

    init
    {
        for (i in 0 until NUMBER_OF_BASE_LIST)
            listUserListAndMovie.add(UserListAndMovieList(UserList(null), arrayListOf()))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListHolder =
        UserListHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_userlist, parent, false)
        )

    override fun getItemCount(): Int = listUserListAndMovie.size

    override fun onBindViewHolder(holder: UserListHolder, position: Int)
    {
        val userListAndMovie = listUserListAndMovie[position]

        holder.name.text = userListAndMovie.userList.name
        holder.recyclerMovie.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            setHasFixedSize(true)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(holder.itemView.context, R.anim.layout_animation_slide_from_right)
            scheduleLayoutAnimation()
            adapter = CompressedMovieAdapter(listUserListAndMovie[position].movieList.toMutableList())
        }

        if (listUserListAndMovie[position].movieList.isEmpty())
            holder.noMovieInList.show()
        else
            holder.noMovieInList.hide()

        holder.itemView.setOnClickListener{
            //TODO
            /*
            holder.itemView.context.startActivity(
                Intent(holder.itemView.context, MovieGridCategoryActivity::class.java)
                .putExtra(EXTRA_CATEGORY,category))
             */
        }
    }

    fun refreshOrAddACategory(userlist: UserList, position: Int, newList:List<Movie>)
    {
        //ajout de la catégorie et des films
        if (this.listUserListAndMovie[position].movieList.isEmpty())
        {
            this.listUserListAndMovie[position] = UserListAndMovieList(userlist, newList)
            notifyItemChanged(position)
        }
        //refresh de la catégorie
        else
        {
            if (newList.isNotEmpty())
            {
                //si la taille de la liste des films à changé ou que les derniers id sont pas les mêmes
                //on part du principe que la liste a changé et qu'l faut refresh
                if (position<this.listUserListAndMovie.size ||
                    this.listUserListAndMovie[position].movieList.isNotEmpty() ||
                    this.listUserListAndMovie[position].movieList.last().id != newList.last().id)
                {
                    this.listUserListAndMovie[position].movieList = newList.toMutableList()
                    notifyItemChanged(position)
                }
            }
        }
    }

    data class UserListAndMovieList(var userList: UserList, var movieList: List<Movie>)

}