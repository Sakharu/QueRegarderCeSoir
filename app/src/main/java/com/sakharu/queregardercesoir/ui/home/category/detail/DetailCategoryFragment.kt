package com.sakharu.queregardercesoir.ui.home.category.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.base.BaseFragment
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.ui.home.category.listCategory.littleMovie.LittleMovieAdapter
import com.sakharu.queregardercesoir.util.*


class DetailCategoryFragment : BaseFragment()
{

    private lateinit var detailCategoryViewModel: DetailCategoryViewModel
    private lateinit var littleMoviePagingAdapter: LittleMovieAdapter
    private var isLoading = false
    private val titleCategoryTV by lazy { view!!.findViewById<TextView>(R.id.nameCategoryDetail) }
    private val overviewCategoryTV by lazy { view!!.findViewById<TextView>(R.id.overviewCategoryDetail) }
    private val loadingMoreAnimation by lazy { view!!.findViewById<ProgressBar>(R.id.loadingMoreAnimationDetailCategory) }
    private lateinit var observer : Observer<List<Movie>>
    private var page=1

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        ajouterActionAIntentFilter(ACTION_LOAD_MORE_CATEGORY_DETAIL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_detail_category, container, false)
        littleMoviePagingAdapter = LittleMovieAdapter(context!!, arrayListOf(),true)
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerCategoryDetails)
        recyclerView.apply {
            layoutManager = GridLayoutManager(context!!,3)
            setHasFixedSize(true)
            adapter = littleMoviePagingAdapter
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        detailCategoryViewModel = ViewModelProvider(this, ViewModelFactory()).get(DetailCategoryViewModel::class.java)
        detailCategoryViewModel.category = arguments?.getSerializable(CATEGORY) as Category

        titleCategoryTV.text = detailCategoryViewModel.category.name
        overviewCategoryTV.text = detailCategoryViewModel.category.overview

        observer = Observer {
                    littleMoviePagingAdapter.setData(it)
                    loadingMoreAnimation.hide()
                    isLoading=false
                }

        //on charge les films populaires lorsqu'on en a en BD
        detailCategoryViewModel.getMoviesLiveList(page).observe(viewLifecycleOwner, observer)
    }

    override fun doOnReceive(intent: Intent)
    {
        super.doOnReceive(intent)
        /*
        Si l'adapter notifie le fragment qu'il a chargé les derniers items de la liste,
        on va télécharger et afficher les films de la page suivante
         */
        if(intent.action == ACTION_LOAD_MORE_CATEGORY_DETAIL)
        {
            detailCategoryViewModel.getMoviesLiveList(page).removeObserver(observer)
            detailCategoryViewModel.getMoviesLiveList(page).observe(viewLifecycleOwner, observer)
            page++
            loadingMoreAnimation.show()
        }
    }

    companion object
    {
        fun newInstance(category: Category) : DetailCategoryFragment
        {
            val args = Bundle()
            args.putSerializable(CATEGORY,category)
            val frag = DetailCategoryFragment()
            frag.arguments =  args
            return frag
        }
        var isLoading=false
    }

}
