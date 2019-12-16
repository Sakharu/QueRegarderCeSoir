package com.sakharu.queregardercesoir.base

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager


open class BaseActivity : AppCompatActivity()
{
    private lateinit var mIntentFilter: IntentFilter
    private var progressBar : ProgressBar? = null
    private var dialog : Dialog? = null

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


}