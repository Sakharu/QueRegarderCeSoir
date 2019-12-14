package com.sakharu.queregardercesoir.base

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager


open class BaseFragment : Fragment()
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
        if (context!=null)
            LocalBroadcastManager.getInstance(context!!).registerReceiver(broadcastReceiver,mIntentFilter)
    }

    override fun onPause() {
        super.onPause()
        if (context!=null)
            LocalBroadcastManager.getInstance(context!!).unregisterReceiver(broadcastReceiver)
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