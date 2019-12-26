package com.sakharu.queregardercesoir.data.locale.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sakharu.queregardercesoir.data.locale.AppDatabase
import com.sakharu.queregardercesoir.data.locale.dao.CategoryDAO
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.util.*

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

    fun getNbCategory() = categoryDAO.getNbCategory()

    suspend fun insertCategory(id: Long, name: String) = categoryDAO.insert(Category(id, name))

    fun getAllCategoriesLive() : LiveData<List<Category>> = categoryDAO.getAllCategories()

    suspend fun insertAllCategories()
    {
        insertCategory(CATEGORY_TRENDING_ID, CATEGORY_TRENDING_NAME)
        insertCategory(CATEGORY_TOPRATED_ID, CATEGORY_TOPRATED_NAME)
        insertCategory(CATEGORY_NOWPLAYING_ID, CATEGORY_NOWPLAYING_NAME)
    }
    /***********************
     *  REGION REMOTE
     **********************/


}
