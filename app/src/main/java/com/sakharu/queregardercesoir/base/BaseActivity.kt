package com.sakharu.queregardercesoir.base

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager


open class BaseActivity : AppCompatActivity()
{
    private lateinit var mIntentFilter: IntentFilter

    /*********************
     * CYCLE DE VIE
     ********************/

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mIntentFilter = IntentFilter()
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,mIntentFilter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }


    /*******************************
     * RECEPTION D'INTENT ET EXTRA
     *******************************/

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent)
        {
            doOnReceive(intent)
        }
    }

    open fun doOnReceive(intent: Intent){}

    fun ajouterActionAIntentFilter(action:String)
    {
        mIntentFilter.addAction(action)
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
     * DIALOG BOX
     */

    fun showDialogBox(titleRef:Int, message:Int, positiveButtonMessage:Int, negativeButtonMessage:Int?, fonctionOui:()->Unit , fonctionNon:()->Unit)
    {
        val builder = AlertDialog.Builder(this)
        builder.apply {
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


}