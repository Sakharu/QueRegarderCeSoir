package com.sakharu.queregardercesoir.ui.userList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.model.UserList
import com.sakharu.queregardercesoir.ui.base.BaseActivity
import com.sakharu.queregardercesoir.ui.userList.listUserList.UserListAdapter
import com.sakharu.queregardercesoir.util.NUMBER_OF_CATEGORIES
import com.sakharu.queregardercesoir.util.ViewModelFactory
import com.sakharu.queregardercesoir.util.hide

class UserListFragment : Fragment() {

    private lateinit var userListViewModel: UserListViewModel
    private lateinit var userListAdapter : UserListAdapter
    private lateinit var recyclerUserList : RecyclerView
    private lateinit var loadingAnimationUserList : ProgressBar
    private var listCategoriesLoaded= arrayListOf(false,false,false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        (activity as BaseActivity).setUpActionBar(getString(R.string.title_lists),false)

        userListAdapter = UserListAdapter()
        recyclerUserList = root.findViewById(R.id.recyclerUserList)
        loadingAnimationUserList = root.findViewById(R.id.loadingMoreAnimationFragmentList)
        recyclerUserList.apply {
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            adapter = userListAdapter

        }

        userListViewModel = ViewModelProvider(this, ViewModelFactory()).get(UserListViewModel::class.java)

        userListViewModel.arrayBaseUserListNames = resources.getStringArray(R.array.baseListNames)
        userListViewModel.arrayBaseUserListResourceName = resources.getStringArray(R.array.baseListResourceName)

        //on ajoute le listener sur le recyclerview afin d'ouvrir le détail d'une catégorie au clic
        userListViewModel.userListLiveList.observe(viewLifecycleOwner, Observer<List<UserList>> {
            //on charge les films les mieux notés
            if (it.size == NUMBER_OF_CATEGORIES)
            {
                userListViewModel.firstFavoriteMovieList.observe(viewLifecycleOwner, Observer { favoriteMovies ->
                    passDataToAdapterAndRefreshLoading(0,favoriteMovies)
                })

                userListViewModel.firstToWatchMovieList.observe(viewLifecycleOwner, Observer { moviesToWatch ->
                    passDataToAdapterAndRefreshLoading(1,moviesToWatch)
                })

                userListViewModel.firstWatchedMovieList.observe(viewLifecycleOwner, Observer { moviesWatched ->
                    passDataToAdapterAndRefreshLoading(2,moviesWatched)
                })
            }
        })

        return root
    }

    private fun passDataToAdapterAndRefreshLoading(position: Int, movieList:List<Movie>)
    {
        userListAdapter.refreshOrAddACategory(userListViewModel.userListLiveList.value!![position],position,movieList)
        listCategoriesLoaded[position]=true
        checkIfLoadingIsFinish()
    }

    private fun checkIfLoadingIsFinish()
    {
        if (!listCategoriesLoaded.any{ !it })
            loadingAnimationUserList.hide()
    }

}