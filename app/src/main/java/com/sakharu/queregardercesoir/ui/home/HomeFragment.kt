package com.sakharu.queregardercesoir.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.ui.home.category.CategoryMovieAdapter
import com.sakharu.queregardercesoir.util.ViewModelFactory


class HomeFragment : Fragment()
{
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var categoryMovieAdapter : CategoryMovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel = ViewModelProvider(this, ViewModelFactory()).get(HomeViewModel::class.java)
        categoryMovieAdapter = CategoryMovieAdapter(context!!, arrayListOf())
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerHomePopularMovies)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            adapter = categoryMovieAdapter
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        //on ajoute l'observer sinon le livedata n'est jamais appelé
        homeViewModel.categoriesLiveList.observe(viewLifecycleOwner, Observer<List<Category>> {})

        //on charge les films populaires lorsqu'on en a en BD
        homeViewModel.popularMoviesLiveList.observe(viewLifecycleOwner, Observer<List<Movie>> {
            if (homeViewModel.categoriesLiveList.value!=null)
                categoryMovieAdapter.refreshOrAddACategory(homeViewModel.categoriesLiveList.value!![0],0,it)
        })

        //on charge les films les mieux notés lorsqu'on en a en BD
        homeViewModel.topRatedMoviesLiveList.observe(viewLifecycleOwner, Observer<List<Movie>> {
            if (homeViewModel.categoriesLiveList.value!=null)
                categoryMovieAdapter.refreshOrAddACategory(homeViewModel.categoriesLiveList.value!![1],1,it)
        })

        //on charge les films actuellement au cinéma
        homeViewModel.nowPlayingMoviesLiveList.observe(viewLifecycleOwner, Observer<List<Movie>> {
            if (homeViewModel.categoriesLiveList.value!=null)
                categoryMovieAdapter.refreshOrAddACategory(homeViewModel.categoriesLiveList.value!![2],2,it)
        })

        //on charge les films bientôt en salle
        homeViewModel.upcomingMoviesLiveList.observe(viewLifecycleOwner, Observer<List<Movie>> {
            if (homeViewModel.categoriesLiveList.value!=null)
                categoryMovieAdapter.refreshOrAddACategory(homeViewModel.categoriesLiveList.value!![3],3,it)
        })
    }
}