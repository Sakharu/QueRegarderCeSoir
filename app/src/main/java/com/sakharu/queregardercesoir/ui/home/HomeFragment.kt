package com.sakharu.queregardercesoir.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
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
import com.sakharu.queregardercesoir.ui.base.BaseActivity
import com.sakharu.queregardercesoir.ui.detailMovie.DetailMovieActivity
import com.sakharu.queregardercesoir.ui.home.category.CategoryMovieAdapter
import com.sakharu.queregardercesoir.ui.movieList.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.doAsync


class HomeFragment : Fragment(), OnMovieClickListener
{
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var categoryMovieAdapter : CategoryMovieAdapter
    private lateinit var recyclerCategory : RecyclerView
    private var listCategoriesLoaded= arrayListOf(false,false,false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        categoryMovieAdapter = CategoryMovieAdapter(onMovieClickListener = this)
        recyclerCategory = root.findViewById(R.id.recyclerHomePopularMovies)
        recyclerCategory.apply {
            layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            adapter = categoryMovieAdapter
        }

        (activity as BaseActivity).setUpActionBar(getString(R.string.title_home),false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        homeViewModel = ViewModelProvider(this, ViewModelFactory()).get(HomeViewModel::class.java)

        //on ajoute le listener sur le recyclerview afin d'ouvrir le détail d'une catégorie au clic
        homeViewModel.categoriesLiveList.observe(viewLifecycleOwner, Observer<List<Category>> {})

        //Si on a pas refresh les données aujourd'hui
        if (DateUtils.isToday(PreferenceUtil.getLong(context!!,
                PREFERENCE_LAST_TIMESTAMP_HOME,0)))
            homeViewModel.refreshData=false

        //on charge les films les mieux notés
        homeViewModel.topRatedMoviesLiveList.observe(viewLifecycleOwner, Observer<List<Movie>> {
            passDataToAdapterAndRefreshLoading(1,it)
        })

        //on charge les films actuellement au cinéma
        homeViewModel.nowPlayingMoviesLiveList.observe(viewLifecycleOwner, Observer<List<Movie>> {
            passDataToAdapterAndRefreshLoading(2,it)
        })

        //on charge les films à la mode en ce moment
        homeViewModel.trendingMoviesLiveList.observe(viewLifecycleOwner, Observer<List<Movie>> {
            passDataToAdapterAndRefreshLoading(0,it)
        })

        //on lance la suppression de toutes les liaisons entre les movies et les category lorsqu'elles
        //datent de plus de 3 jours
        homeViewModel.getAllMoviesInCategory.observe(viewLifecycleOwner, Observer<List<MovieInCategory>>
        {

            //on fait tout le traitement dans un thread secondaire pour ne pas affecter l'ux
            doAsync {
                val listMovieInCategoryDeprecated =
                    it.filter { movieInCategory ->
                        Utils.differenceInDayFromActualTimestamp(movieInCategory.addedTimestamp)> NUMBER_OF_DAYS_DEPRECATED_MOVIEINCATEGORY
                                && movieInCategory.id!=null}
                        .map { it.id!! }

                MovieRepository.deleteDeprecatedMoviesInCategory(listMovieInCategoryDeprecated)
            }
        })
    }

    private fun passDataToAdapterAndRefreshLoading(position: Int, movieList:List<Movie>)
    {
        categoryMovieAdapter.refreshOrAddACategory(homeViewModel.categoriesLiveList.value!![position],position,movieList)
        listCategoriesLoaded[position]=true
        checkIfLoadingIsFinish()
    }

    private fun checkIfLoadingIsFinish()
    {
        if (!listCategoriesLoaded.any{ !it })
        {
            loadingMoreAnimationHome.hide()
            //si les données ont été refresh par rapport à l'api, on note dans les préférences le timestamp actuel
            if (homeViewModel.refreshData)
                PreferenceUtil.setLong(context!!, PREFERENCE_LAST_TIMESTAMP_HOME,System.currentTimeMillis())
        }
    }

    /*
    Si l'adapter notifie l'activite qu'un film a été touché, on ouvre l'activite de tail
    et on lui donne en transition l'imageview pour avoir l'animation du poster et éviter de
    recharger l'image dans l'activite suivante
     */
    override fun onClickOnMovie(movie: Movie, imageView: ImageView)
    {
        if (activity!=null)
        {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!,
                androidx.core.util.Pair<View,String>(imageView,getString(R.string.transitionMovieListToDetail)))

            startActivity(Intent(activity, DetailMovieActivity::class.java).putExtra(EXTRA_MOVIE_ID,movie.id),options.toBundle())
        }

    }

}