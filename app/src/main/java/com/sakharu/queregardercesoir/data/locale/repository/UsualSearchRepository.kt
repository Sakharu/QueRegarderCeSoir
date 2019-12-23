package com.sakharu.queregardercesoir.data.locale.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sakharu.queregardercesoir.data.locale.AppDatabase
import com.sakharu.queregardercesoir.data.locale.dao.UsualSearchDAO
import com.sakharu.queregardercesoir.data.locale.model.UsualSearch

object UsualSearchRepository
{
    private lateinit var database: AppDatabase
    private lateinit var usualSearchDAO: UsualSearchDAO


    fun initialize(application: Application)
    {
        database = AppDatabase.buildInstance(application)
        usualSearchDAO = database.usualSearchDAO()
    }

    /***********************
     *  REGION LOCALE
     **********************/
    /*
        GENRE
     */
    suspend fun insertAllUsualSearches(usualSearchList:List<UsualSearch>) = usualSearchDAO.insertAll(usualSearchList)

    fun getAllUsualSearches() = usualSearchDAO.getAllUsualSearches()

    fun getNumberUsualSearches() : LiveData<Int> = usualSearchDAO.numberUsualSearches()

}