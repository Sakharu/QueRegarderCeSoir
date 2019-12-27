package com.sakharu.queregardercesoir.data.locale.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sakharu.queregardercesoir.data.locale.AppDatabase
import com.sakharu.queregardercesoir.data.locale.dao.CategoryDAO
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.util.*
import kotlin.coroutines.coroutineContext

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

    suspend fun insertAllCategories(names:Array<String>)
    {
        insertCategory(CATEGORY_TRENDING_ID, names[0])
        insertCategory(CATEGORY_TOPRATED_ID, names[1])
        insertCategory(CATEGORY_NOWPLAYING_ID, names[2])
    }
    /***********************
     *  REGION REMOTE
     **********************/


}
