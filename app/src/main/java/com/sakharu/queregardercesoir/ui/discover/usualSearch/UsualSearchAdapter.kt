package com.sakharu.queregardercesoir.ui.discover.usualSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.UsualSearch


class UsualSearchAdapter(private var listSearch: MutableList<UsualSearch>,
    private var onUsualSearchClickListener: OnUsualSearchClickListener? = null)
    : RecyclerView.Adapter<UsualSearchHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsualSearchHolder =
        UsualSearchHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_usual_search, parent, false))

    override fun getItemCount(): Int = listSearch.size

    override fun onBindViewHolder(holder: UsualSearchHolder, position: Int)
    {
        holder.titleSearch.text = listSearch[position].title
        holder.subTitleSearch.text = listSearch[position].subtitle
        holder.nextImgButton.setOnClickListener{
            this.onUsualSearchClickListener?.onUsualSearchClick(listSearch[position].id)
        }
    }

    fun setData(usualSearchList:List<UsualSearch>)
    {
        this.listSearch.addAll(usualSearchList.filter { !listSearch.contains(it) })
        notifyDataSetChanged()
    }
}