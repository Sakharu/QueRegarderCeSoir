package com.sakharu.queregardercesoir

import android.app.Application
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository

class App:Application()
{
    override fun onCreate() {
        super.onCreate()
        MovieRepository.initialize(this)
    }

    companion object {
        //index du dernier pokemon téléchargé
        var indexactuel:Int=1
    }
}