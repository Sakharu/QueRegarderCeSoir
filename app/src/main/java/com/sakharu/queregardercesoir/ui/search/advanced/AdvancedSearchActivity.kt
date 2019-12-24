package com.sakharu.queregardercesoir.ui.search.advanced

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.ui.base.BaseActivity
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.activity_advanced_search.*
import java.util.*

class AdvancedSearchActivity : BaseActivity()
{
    private lateinit var searchViewModel : SearchViewModel
    private var listIdOfSelectedGenre = linkedSetOf<Long>()
    private var genreObserver = Observer<List<Genre>> {
            for (genre in it)
            {
                val chip = layoutInflater.inflate(R.layout.chip_layout,layoutAdvanceSearch, false) as Chip
                chip.text = genre.name
                chip.id = genre.id.toInt()
                chip.setOnClickListener{
                    if (listIdOfSelectedGenre.contains(genre.id))
                        listIdOfSelectedGenre.remove(genre.id)
                    else
                        listIdOfSelectedGenre.add(genre.id)
                }
                chipGroupGenres.addView(chip as View)
            }
    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_search)

        searchViewModel = ViewModelProvider(this, ViewModelFactory()).get(SearchViewModel::class.java)
        searchViewModel.genresListLive.observe(this,genreObserver)

        editTextYearAdvanceSearch.setText((Calendar.getInstance().get(Calendar.YEAR)+1).toString())

        setUpActionBar(getString(R.string.advancedSearch),true)


        seekbarAverageVoteAdvancedSearch.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, progess: Int, fromUser: Boolean)
            {
                voteAverageMinAdvancedSearch.text = (progess/10.0).toString()
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) {}
            override fun onStopTrackingTouch(seekbar: SeekBar?) {}
        })

        advanceSearchButton.setOnClickListener{
            var year = -1
            try
            { year = editTextYearAdvanceSearch.text.toString().toInt() }
            catch (e:Exception)
            {
                e.printStackTrace()
                showLongToast(this,getString(R.string.yearInvalid))
            }

            if (year <1850 || year >2050)
                showLongToast(this,getString(R.string.yearInvalid))
            else
            {
                val sortBy = when(spinnerSortAdvancedSearch.selectedItemPosition)
                {
                    0-> POPULARITYDESC
                    1-> RELEASEDATEDESC
                    else-> VOTEAVERAGEDESC
                }
                val intentAdvancedSearch = Intent(this,
                    AdvancedResultSearchActivity::class.java)
                intentAdvancedSearch.apply {
                    if (listIdOfSelectedGenre.isNotEmpty())
                        putExtra(EXTRA_GENRES,listIdOfSelectedGenre.joinToString { it.toString() })
                    putExtra(EXTRA_SORTBY,sortBy)
                    putExtra(EXTRA_BEFORE_DURING_AFTER,spinnerGreaterLowerYear.selectedItemPosition)
                    putExtra(EXTRA_YEAR, year)
                    putExtra(EXTRA_AVERAGE_VOTE_MIN, voteAverageMinAdvancedSearch.text.toString().toDouble())
                }
                startActivity(intentAdvancedSearch)
            }
        }
    }
}
