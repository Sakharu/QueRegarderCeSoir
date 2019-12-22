package com.sakharu.queregardercesoir.ui.discover

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.Window
import android.widget.Button
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.util.PREFERENCE_IDS_FAVORITES_GENRES
import com.sakharu.queregardercesoir.util.PreferenceUtil

class AskForFavoriteGenreDialog
{
    private var dialog : Dialog? = null
    private val listIdOfSelectedGenre = ArrayList<Long>()
    fun showDialog(activity: Activity, listGenres:List<Genre>)
    {
        dialog = Dialog(activity)

        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_ask_for_favorites_genre)
            setCancelable(true)

            findViewById<Button>(R.id.validateButtonDialogFavoriteGenre).setOnClickListener{
                PreferenceUtil.setLongArray(activity,PREFERENCE_IDS_FAVORITES_GENRES,listIdOfSelectedGenre)
                dismiss()
            }
        }


        val chipGroup = dialog!!.findViewById<ChipGroup>(R.id.chipGroupDialogFavoriteGenre)
        for (genre in listGenres)
        {
            val chip = activity.layoutInflater.inflate(R.layout.chip_layout,
                dialog!!.findViewById(R.id.layoutDialogChooseGenre), false) as Chip
            chip.text = genre.name
            chip.id = genre.id.toInt()
            chip.setOnClickListener {
                if (listIdOfSelectedGenre.contains(genre.id))
                    listIdOfSelectedGenre.remove(genre.id)
                else
                    listIdOfSelectedGenre.add(genre.id)
            }
            chipGroup.addView(chip as View)
        }


        dialog!!.show()
    }


}