package com.sakharu.queregardercesoir.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.data.locale.movie.Movie
import com.sakharu.queregardercesoir.util.ViewModelFactory
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.ui.home.category.CategoryMovieAdapter

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel = ViewModelProvider(this, ViewModelFactory()).get(HomeViewModel::class.java)
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerHomePopularMovies)

        val categoryMovieAdapter = CategoryMovieAdapter(context!!, listOf(), listOf())
        recyclerView.apply {
            layoutManager = GridLayoutManager(context!!,2)
            setHasFixedSize(true)
            adapter = categoryMovieAdapter
        }

        homeViewModel.popularMoviesLiveList.observe(viewLifecycleOwner, Observer<List<Movie>> {
            categoryMovieAdapter.setData(homeViewModel.categoriesLiveList.value.orEmpty(),
                homeViewModel.allMoviesInAllCategories)
        })


        return root
    }
}