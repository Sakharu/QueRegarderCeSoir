package com.sakharu.queregardercesoir.ui.userList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.ui.base.BaseActivity

class UserListFragment : Fragment() {

    private lateinit var userListViewModel: UserListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        (activity as BaseActivity).changeTitleFromActionbar(getString(R.string.searchTitleFragment))

        return root
    }
}