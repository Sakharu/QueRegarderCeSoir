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
import com.fspt.android_monclubetmoi.`object`.RecyclerItemClickListener
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.ui.home.category.detail.DetailCategoryFragment
import com.sakharu.queregardercesoir.ui.home.category.listCategory.CategoryMovieAdapter
import com.sakharu.queregardercesoir.util.NUMBER_OF_DAYS_DEPRECATED_MOVIEINCATEGORY
import com.sakharu.queregardercesoir.util.Util
import com.sakharu.queregardercesoir.util.ViewModelFactory
import org.jetbrains.anko.doAsync


class HomeFragment : Fragment()
{
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var categoryMovieAdapter : CategoryMovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        categoryMovieAdapter = CategoryMovieAdapter()
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

        homeViewModel = ViewModelProvider(this, ViewModelFactory()).get(HomeViewModel::class.java)

        //on ajoute l'observer sinon le livedata n'est jamais appelé
        homeViewModel.categoriesLiveList.observe(viewLifecycleOwner, Observer<List<Category>> {})

        //on charge les films populaires lorsqu'on en a en BD
        homeViewModel.popularMoviesLiveList.observe(viewLifecycleOwner, Observer<List<Movie>> {
            if (homeViewModel.categoriesLiveList.value.orEmpty().isNotEmpty())
                categoryMovieAdapter.refreshOrAddACategory(homeViewModel.categoriesLiveList.value!![0],0,it)
        })

        //on charge les films les mieux notés lorsqu'on en a en BD
        homeViewModel.topRatedMoviesLiveList.observe(viewLifecycleOwner, Observer<List<Movie>> {
            if (homeViewModel.categoriesLiveList.value.orEmpty().size>1)
                categoryMovieAdapter.refreshOrAddACategory(homeViewModel.categoriesLiveList.value!![1],1,it)
        })

        //on charge les films actuellement au cinéma
        homeViewModel.nowPlayingMoviesLiveList.observe(viewLifecycleOwner, Observer<List<Movie>> {
            if (homeViewModel.categoriesLiveList.value.orEmpty().size>2)
                categoryMovieAdapter.refreshOrAddACategory(homeViewModel.categoriesLiveList.value!![2],2,it)
        })

        //on charge les films bientôt en salle
        homeViewModel.upcomingMoviesLiveList.observe(viewLifecycleOwner, Observer<List<Movie>> {
            if (homeViewModel.categoriesLiveList.value.orEmpty().size>3)
                categoryMovieAdapter.refreshOrAddACategory(homeViewModel.categoriesLiveList.value!![3],3,it)
        })

        //on lance la suppression de toutes les liaisons entre les movies et les category lorsqu'elles
        //datent de plus de 3 jours
        homeViewModel.getAllMoviesInCategory.observe(viewLifecycleOwner, Observer<List<MovieInCategory>>
        {
            //on fait tout le traitement dans un thread secondaire
            doAsync {
                val listMovieInCategoryDeprecated =
                    it.filter { movieInCategory ->
                        Util.differenceInDayFromActualTimestamp(movieInCategory.addedTimestamp)> NUMBER_OF_DAYS_DEPRECATED_MOVIEINCATEGORY
                                && movieInCategory.id!=null}
                        .map { it.id!! }

                MovieRepository.deleteDeprecatedMoviesInCategory(listMovieInCategoryDeprecated)
            }
        })
    }

    private fun openDetailCategoryFragment(category: Category)
    {
        val ftt = activity!!.supportFragmentManager.beginTransaction()
        ftt.setCustomAnimations(R.anim.in_from_left, R.anim.out_to_right, R.anim.in_from_left, R.anim.out_to_right)
        ftt.replace(R.id.nav_host_fragment, DetailCategoryFragment.newInstance(category), DetailCategoryFragment::class.java.simpleName)
        ftt.commitAllowingStateLoss()
    }


}