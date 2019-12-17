package com.sakharu.queregardercesoir.ui.home

import android.content.Intent
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
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.ui.home.category.CategoryMovieAdapter
import com.sakharu.queregardercesoir.ui.movieList.MovieListActivity
import com.sakharu.queregardercesoir.util.*
import org.jetbrains.anko.doAsync


class HomeFragment : Fragment()
{
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var categoryMovieAdapter : CategoryMovieAdapter
    private lateinit var recyclerCategory : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        categoryMovieAdapter = CategoryMovieAdapter()
        recyclerCategory = root.findViewById(R.id.recyclerHomePopularMovies)
        recyclerCategory.apply {
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

        //on ajoute le listener sur le recyclerview afin d'ouvrir le détail d'une catégorie au clic
        homeViewModel.categoriesLiveList.observe(viewLifecycleOwner, Observer<List<Category>> {
            //on recupère l'exception si jamais le context est null ou qu'il y a un problème avec la liste
            try
            {
                recyclerCategory.addOnItemTouchListener(RecyclerItemClickListener(context!!,
                    recyclerCategory,object : RecyclerItemClickListener.OnItemClickListener
                    {
                        override fun onItemClick(view: View, position: Int)
                        {
                            startActivity(Intent(context!!,MovieListActivity::class.java).putExtra(
                            EXTRA_CATEGORY,it[position]))
                        }

                        override fun onLongItemClick(view: View?, position: Int) {}
                    })
                )

            }
            catch (e:Exception)
            {
                //TODO GERER ERREUR
            }

        })

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

            //on fait tout le traitement dans un thread secondaire pour ne pas affecter l'ux
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
}