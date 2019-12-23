package com.sakharu.queregardercesoir

import android.app.Application
import com.sakharu.queregardercesoir.data.locale.repository.CategoryRepository
import com.sakharu.queregardercesoir.data.locale.repository.GenreRepository
import com.sakharu.queregardercesoir.data.locale.repository.MovieRepository
import com.sakharu.queregardercesoir.data.locale.repository.UsualSearchRepository

class App:Application()
{
    override fun onCreate()
    {
        super.onCreate()
        MovieRepository.initialize(this)
        GenreRepository.initialize(this)
        CategoryRepository.initialize(this)
        UsualSearchRepository.initialize(this)
    }
}