package com.sakharu.queregardercesoir.ui.discover

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.ui.base.BaseActivity
import com.sakharu.queregardercesoir.ui.base.BaseFragment
import com.sakharu.queregardercesoir.ui.detailMovie.DetailMovieActivity
import com.sakharu.queregardercesoir.ui.discover.suggestedMovie.AskForFavoriteGenreDialog
import com.sakharu.queregardercesoir.ui.discover.suggestedMovie.SuggestMovieActivity
import com.sakharu.queregardercesoir.ui.discover.suggestedMovie.SuggestedMovieAdapter
import com.sakharu.queregardercesoir.ui.discover.usualSearch.OnUsualSearchClickListener
import com.sakharu.queregardercesoir.ui.discover.usualSearch.UsualSearchAdapter
import com.sakharu.queregardercesoir.ui.movieGridCategory.littleMovie.OnMovieClickListener
import com.sakharu.queregardercesoir.ui.search.advanced.AdvancedResultSearchActivity
import com.sakharu.queregardercesoir.ui.search.advanced.AdvancedSearchActivity
import com.sakharu.queregardercesoir.util.*
import java.util.*


class DiscoverFragment : BaseFragment(), OnUsualSearchClickListener, OnMovieClickListener
{
    private lateinit var discoverViewModel : DiscoverViewModel
    private lateinit var usualSearchAdapter : UsualSearchAdapter
    private var genresFavoritesId:ArrayList<Long> = arrayListOf()
    private var suggestedMovieAdapter : SuggestedMovieAdapter?=null
    private lateinit var askForFavoriteGenreButton : Button
    private lateinit var askForFavoriteGenreTV : TextView
    private lateinit var loadingProgressBar : ProgressBar
    private lateinit var seeMoreButton : ImageButton
    private var isErrorDialogShown=false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        ajouterActionAIntentFiltrer(ACTION_FAVORITE_GENRE_VALIDATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_discover, container, false)

        discoverViewModel = ViewModelProvider(activity!!, ViewModelFactory()).get(DiscoverViewModel::class.java)
        (activity as BaseActivity).changeTitleFromActionbar(getString(R.string.discoverTitleFragment))

        askForFavoriteGenreButton = root.findViewById(R.id.noFavoritesGenreButton)
        askForFavoriteGenreTV = root.findViewById(R.id.noFavoritesGenreTV)
        loadingProgressBar = root.findViewById(R.id.loadingMoreAnimationSuggestedMovies)
        seeMoreButton = root.findViewById(R.id.seeMoreButtonDiscover)

        /**
         * RECHERCHES COURANTES
         */

        usualSearchAdapter = UsualSearchAdapter(arrayListOf(),this)
        root.findViewById<RecyclerView>(R.id.recyclerUsualSearches).apply {
            layoutManager = LinearLayoutManager(root.context,LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(root.context, R.anim.layout_animation_slide_from_right)
            scheduleLayoutAnimation()
            adapter = usualSearchAdapter
        }

        discoverViewModel.nbUsualSearches.observe(viewLifecycleOwner, Observer<Int> {
            if (it==0)
                discoverViewModel.insertUsualSearches(resources.getStringArray(R.array.usualSearchestTitles)
                    ,resources.getStringArray(R.array.usualSearchestSubTitles),it)
        })

        discoverViewModel.getAllUsualSearchesListLive.observe(viewLifecycleOwner, Observer {
            usualSearchAdapter.setData(it)
        })


        /**
        RECHERCHE AVANCEE
         **/
        root.findViewById<TextView>(R.id.advancedSearchTV).setOnClickListener{
            startActivity(Intent(root.context, AdvancedSearchActivity::class.java))
        }

        /**
         * SUGGESTIONS DE FILMS
         */

        suggestedMovieAdapter = SuggestedMovieAdapter(arrayListOf(), arrayListOf(),this)

        root.findViewById<RecyclerView>(R.id.recyclerSuggestionsMovies).apply {
            layoutManager = LinearLayoutManager(root.context,LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(false)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(root.context, R.anim.layout_animation_fall_down)
            scheduleLayoutAnimation()
            adapter = suggestedMovieAdapter
        }

        //Si l'utilisateur n'a pas renseigné ses films favoris on lui ouvre la boite de dialogue
        //qui permet de le faire et on affiche un message et un bouton pour pouvoir le faire à tout
        //moment
        genresFavoritesId = PreferenceUtil.getLongArray(root.context, PREFERENCE_IDS_FAVORITES_GENRES)
        if (genresFavoritesId.isEmpty())
        {
            loadingProgressBar.hide()
            seeMoreButton.hide()
            askForFavoriteGenreButton.show()
            askForFavoriteGenreTV.show()

            discoverViewModel.genresListLive.observe(viewLifecycleOwner, Observer {
                //il peut y avoir des erreurs de connexion qui empêche de récupérer les genres d'ou
                //le check de la liste vide
                if (!dialogShown && it.isNotEmpty())
                    AskForFavoriteGenreDialog().showDialog(activity!!,it)
            })

            askForFavoriteGenreButton.setOnClickListener{
                discoverViewModel.genresListLive.observe(viewLifecycleOwner, Observer {
                    if (!dialogShown && it.isNotEmpty())
                        AskForFavoriteGenreDialog().showDialog(activity!!,it)
                })
            }
        }
        //Sinon on va télécharger puis afficher des films qui correspondent à ses genres favoris
        else
            launchSuggestedMovies(root.context)

        discoverViewModel.errorNetwork.observe(viewLifecycleOwner, Observer {
            if (it && !isErrorDialogShown)
            {
                isErrorDialogShown=true
                showDialogError()
                askForFavoriteGenreButton.hide()
                askForFavoriteGenreTV.hide()
                loadingProgressBar.hide()
            }
        })

        return root
    }

    //va créer toute la mécanique de
    private fun launchSuggestedMovies(context:Context)
    {
        genresFavoritesId = PreferenceUtil.getLongArray(context, PREFERENCE_IDS_FAVORITES_GENRES)
        if (genresFavoritesId.isNotEmpty())
        {
            askForFavoriteGenreButton.hide()
            askForFavoriteGenreTV.hide()
            loadingProgressBar.show()

            discoverViewModel.genresListLive.observe(viewLifecycleOwner, Observer { suggestedMovieAdapter!!.addGenres(it) })

            if (DateUtils.isToday(PreferenceUtil.getLong(context, PREFERENCE_LAST_TIMESTAMP_DISCOVER,0)))
                discoverViewModel.refreshSuggestedMovies=false

            discoverViewModel.getSuggestedMovies(genresFavoritesId).observe(viewLifecycleOwner, Observer {
                loadingProgressBar.hide()
                if (it.isNotEmpty())
                {
                    suggestedMovieAdapter!!.addMovie(it)
                    PreferenceUtil.setLong(context, PREFERENCE_LAST_TIMESTAMP_DISCOVER,System.currentTimeMillis())
                    seeMoreButton.show()
                    //lorsque l'on a déjà les 20 films à afficher, on se désabonne des changements qui peuvent survenir dans la BD
                    if (it.size==20)
                        discoverViewModel.getSuggestedMovies(genresFavoritesId).removeObservers(viewLifecycleOwner)
                }
            })
        }

        seeMoreButton.setOnClickListener{
            startActivity(Intent(context,SuggestMovieActivity::class.java).putExtra(EXTRA_GENRES,genresFavoritesId.toLongArray()))
        }
    }

    override fun onUsualSearchClick(idUsualSearch: Int)
    {
        //ON LANCE LA RECHERCHE LORS DE L'APPUI SUR UNE RECHERCHE COURANTE
        val intentSearchActivity = Intent(context,AdvancedResultSearchActivity::class.java)
            .putExtra(EXTRA_SORTBY, VOTEAVERAGEDESC).putExtra(EXTRA_BEFORE_DURING_AFTER,2)

        when(idUsualSearch)
        {
            ID_BEST_RECENT_MOVIES -> startActivity(intentSearchActivity.putExtra(EXTRA_YEAR, Calendar.getInstance().get(Calendar.YEAR)-5))
            ID_MY_FAMILY_FIRST -> startActivity(intentSearchActivity.putExtra(EXTRA_YEAR,Calendar.getInstance().get(Calendar.YEAR)-20)
                .putExtra(EXTRA_CERTIFICATION,CERTIFICATION_FRANCE_NO_AGE_REQUIRED))
            ID_SUCCES_OF_LAST_TEN_YEARS -> startActivity(intentSearchActivity.putExtra(EXTRA_YEAR,Calendar.getInstance().get(Calendar.YEAR)-10))
        }
    }

    override fun doOnReceive(intent: Intent)
    {
        super.doOnReceive(intent)
        when(intent.action)
        {
            ACTION_FAVORITE_GENRE_VALIDATE ->
            {
                if (context!=null)
                    launchSuggestedMovies(context!!)
                dialogShown=false
            }
        }
    }

    override fun onClickOnMovie(movie: Movie, imageView: ImageView)
    {
        if (activity!=null)
            startActivity(Intent(activity, DetailMovieActivity::class.java).putExtra(EXTRA_MOVIE_ID,movie.id))
    }

    companion object
    {
        var dialogShown =false
    }
}