package com.sakharu.queregardercesoir.data.locale

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sakharu.queregardercesoir.data.locale.converters.Converters
import com.sakharu.queregardercesoir.data.locale.dao.*
import com.sakharu.queregardercesoir.data.locale.model.*


@Database(entities = [Movie::class, Genre::class, Category::class, MovieInCategory::class,UsualSearch::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun movieDAO(): MovieDAO
    abstract fun categoryDAO(): CategoryDAO
    abstract fun movieInCategoryDAO(): MovieInCategoryDAO
    abstract fun genreDAO(): GenreDAO
    abstract fun usualSearchDAO(): UsualSearchDAO
    companion object
    {
        fun buildInstance(context: Context) = Room
            .databaseBuilder(context, AppDatabase::class.java, "FilmDatabase")
            .build()
    }
}