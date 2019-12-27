package com.sakharu.queregardercesoir

import android.app.Application
import com.sakharu.queregardercesoir.data.locale.repository.*

class App:Application()
{
    override fun onCreate()
    {
        super.onCreate()
        MovieRepository.initialize(this)
        GenreRepository.initialize(this)
        CategoryRepository.initialize(this)
        UsualSearchRepository.initialize(this)
        UserListRepository.initialize(this)
    }
}