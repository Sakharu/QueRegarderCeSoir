package com.sakharu.queregardercesoir.ui.discover

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.ui.base.BaseActivity
import com.sakharu.queregardercesoir.ui.home.HomeViewModel
import com.sakharu.queregardercesoir.ui.search.advanced.AdvancedSearchActivity
import com.sakharu.queregardercesoir.util.PREFERENCE_IDS_FAVORITES_GENRES
import com.sakharu.queregardercesoir.util.PreferenceUtil
import com.sakharu.queregardercesoir.util.ViewModelFactory


class DiscoverFragment : Fragment()
{
    private lateinit var discoverViewModel : DiscoverViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_discover, container, false)

        discoverViewModel = ViewModelProvider(this, ViewModelFactory()).get(DiscoverViewModel::class.java)


        if (PreferenceUtil.getString(root.context, PREFERENCE_IDS_FAVORITES_GENRES,"").isEmpty())
        {
            discoverViewModel.genresListLive.observe(viewLifecycleOwner, Observer {
                AskForFavoriteGenreDialog().showDialog(activity!!,it)
            })
        }

        root.findViewById<TextView>(R.id.advancedSearchTV).setOnClickListener{
            startActivity(Intent(root.context, AdvancedSearchActivity::class.java))
        }

        (activity as BaseActivity).changeTitleFromActionbar(getString(R.string.searchTitleFragment))

        return root
    }

}