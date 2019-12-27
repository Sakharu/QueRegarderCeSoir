package com.sakharu.queregardercesoir.ui.base

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.ui.search.title.TitleSearchingActivity


open class BaseActivity : AppCompatActivity()
{
    open var searchIconEnable=true
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
            if (searchIconEnable)
                startActivity(Intent(this, TitleSearchingActivity::class.java))
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

    /**
     *  DIALOG BOX
     */

    fun showDialogBox(titleRef:Int?, message:Int, positiveButtonMessage:Int, negativeButtonMessage:Int?, fonctionOui:()->Unit , fonctionNon:()->Unit)
    {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            if (titleRef!=null)
                setTitle(titleRef)
            setMessage(message)
            setPositiveButton(positiveButtonMessage) { dialog, _ -> dialog.dismiss(); fonctionOui() }
            if(negativeButtonMessage!=null)
                setNegativeButton(negativeButtonMessage) { dialog, _ -> dialog.dismiss(); fonctionNon() }
        }

        builder.create().show()
    }

    fun showDialogBox(titleRef:String, message:String, positiveButtonMessage:String, negativeButtonMessage:String, fonctionOui:()->Unit , fonctionNon:()->Unit)
    {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle(titleRef)
            setMessage(message)
            setPositiveButton(positiveButtonMessage) { dialog, _ -> dialog.dismiss(); fonctionOui() }
            setNegativeButton(negativeButtonMessage) { dialog, _ -> dialog.dismiss(); fonctionNon() }
        }

        builder.create().show()
    }

    /**
     * ERREURS
     */
    fun showDialogNetworkError() = showDialogBox(null,R.string.errorDesc,R.string.ok,null,{},{})


}