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

class UserListFragment : Fragment() {

    private lateinit var userListViewModel: UserListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userListViewModel =
            ViewModelProviders.of(this).get(UserListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        userListViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}