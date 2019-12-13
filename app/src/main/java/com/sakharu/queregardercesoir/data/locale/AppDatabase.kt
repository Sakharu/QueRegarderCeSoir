package com.sakharu.queregardercesoir.data.locale

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sakharu.queregardercesoir.data.locale.model.Category
import com.sakharu.queregardercesoir.data.locale.dao.CategoryDAO
import com.sakharu.queregardercesoir.data.locale.model.Genre
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.dao.MovieDAO
import com.sakharu.queregardercesoir.data.locale.model.MovieInCategory
import com.sakharu.queregardercesoir.data.locale.dao.MovieInCategoryDAO


@Database(entities = [Movie::class, Genre::class, Category::class, MovieInCategory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun movieDAO(): MovieDAO
    abstract fun categoryDAO(): CategoryDAO
    abstract fun movieInCategoryDAO(): MovieInCategoryDAO
    companion object
    {
        fun buildInstance(context: Context) = Room
            .databaseBuilder(context, AppDatabase::class.java, "FilmDatabase")
            .build()
    }
}