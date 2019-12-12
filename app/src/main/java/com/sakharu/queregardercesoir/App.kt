package com.sakharu.queregardercesoir

import android.app.Application

class App:Application()
{
    override fun onCreate() {
        super.onCreate()
        //PokemonRepository.initialize(this)
    }

    companion object {
        //index du dernier pokemon téléchargé
        var indexactuel:Int=1
    }
}