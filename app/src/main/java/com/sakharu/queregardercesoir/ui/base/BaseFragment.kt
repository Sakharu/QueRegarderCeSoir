package com.sakharu.queregardercesoir.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment()
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
        registerReceivers(broadcastReceiver)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceivers(broadcastReceiver)
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

    fun ajouterActionAIntentFiltrer(action:String)
    {
        mIntentFilter.addAction(action)
    }

    private fun registerReceivers(broadcastReceiver: BroadcastReceiver)
    {
        try { context?.registerReceiver(broadcastReceiver, mIntentFilter) }
        catch (t: Throwable) { t.printStackTrace() }
    }

    private fun unregisterReceivers(broadcastReceiver: BroadcastReceiver)
    {
        try {context?.unregisterReceiver(broadcastReceiver) }
        catch(e : IllegalArgumentException) {}
    }



}