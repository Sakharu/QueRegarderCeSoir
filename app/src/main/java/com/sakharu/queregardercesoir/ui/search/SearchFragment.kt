package com.sakharu.queregardercesoir.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.ui.search.title.ResultSearchActivity
import com.sakharu.queregardercesoir.util.EXTRA_TITLE_SEARCH


class SearchFragment : Fragment()
{
    private lateinit var textInputEdit : TextInputEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        textInputEdit = root.findViewById(R.id.textEditMovieTitle)
        textInputEdit.setText("Banlieue")

        root.findViewById<Button>(R.id.searchButtonSearchFragment).setOnClickListener{ view ->


            when
            {
                //on vérifie que le champs est bien rempli avant de lancer l'activite de recherche
                textInputEdit.text.isNullOrEmpty() ->
                    Toast.makeText(view.context,getString(R.string.emptyFieldTitle),Toast.LENGTH_SHORT).show()

                //on vérifie que le champs a au moins 3 caractères
                textInputEdit.text.toString().length<3 ->
                    Toast.makeText(view.context,getString(R.string.tooShortFieldTitle),Toast.LENGTH_SHORT).show()

                else ->
                    startActivity(Intent(root.context, ResultSearchActivity::class.java)
                    .putExtra(EXTRA_TITLE_SEARCH,textInputEdit.text.toString()))
            }
        }
        return root
    }

}