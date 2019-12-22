package com.sakharu.queregardercesoir.data.locale.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sakharu.queregardercesoir.data.locale.AppDatabase
import com.sakharu.queregardercesoir.data.locale.dao.CategoryDAO
import com.sakharu.queregardercesoir.data.locale.model.Category

object CategoryRepository
{
    private lateinit var database: AppDatabase
    private lateinit var categoryDAO: CategoryDAO

    fun initialize(application: Application)
    {
        database = AppDatabase.buildInstance(application)
        categoryDAO = database.categoryDAO()
    }

    /***********************
     *  REGION LOCALE
     **********************/

    /*
        CATEGORY
     */

    suspend fun insertCategory(id: Long, name: String) = categoryDAO.insert(Category(id, name))

    fun getAllCategoriesLive() : LiveData<List<Category>> = categoryDAO.getAllCategories()


    /***********************
     *  REGION REMOTE
     **********************/


}
