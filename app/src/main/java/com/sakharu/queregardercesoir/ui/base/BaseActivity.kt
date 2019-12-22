package com.sakharu.queregardercesoir.ui.base

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.TitleSearchingActivity


open class BaseActivity : AppCompatActivity()
{

    /**
        GESTION DE L'ACTION BAR
     **/
    fun setUpActionBar(title:String, enableBack: Boolean = true)
    {
        setSupportActionBar(findViewById(R.id.appToolbar))
        supportActionBar?.title=title
        if (enableBack)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            findViewById<Toolbar>(R.id.appToolbar)?.setNavigationOnClickListener {
                onBackPressed()
            }
        }
        else
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.searchAppBar ->
        {
            startActivity(Intent(this,TitleSearchingActivity::class.java))
            true
        }
        else -> true

    }

    fun changeTitleFromActionbar(title:String) { supportActionBar?.title=title }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    /**
     * CLAVIER
     */
    fun hideKeyboard()
    {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null)
            view = View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}