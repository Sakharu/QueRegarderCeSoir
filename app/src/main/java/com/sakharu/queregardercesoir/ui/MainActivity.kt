package com.sakharu.queregardercesoir.ui

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.ui.base.BaseActivity

class MainActivity : BaseActivity(), LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        setUpActionBar(getString(R.string.title_home),false)
        navView.setupWithNavController(navController)

    }
}
