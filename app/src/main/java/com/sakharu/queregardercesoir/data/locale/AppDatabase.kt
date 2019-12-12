package com.sakharu.queregardercesoir.data.locale

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sakharu.queregardercesoir.data.locale.category.Category
import com.sakharu.queregardercesoir.data.locale.category.CategoryDAO
import com.sakharu.queregardercesoir.data.locale.genre.Genre
import com.sakharu.queregardercesoir.data.locale.movie.Movie
import com.sakharu.queregardercesoir.data.locale.movie.MovieDAO
import com.sakharu.queregardercesoir.data.locale.movieInCategory.MovieInCategory
import com.sakharu.queregardercesoir.data.locale.movieInCategory.MovieInCategoryDAO


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