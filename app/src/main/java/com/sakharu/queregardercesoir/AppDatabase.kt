package com.sakharu.queregardercesoir

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.Genre


@Database(entities = [Film::class, Genre::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun filmDAO(): FilmDAO
    companion object
    {
        fun buildInstance(context: Context) = Room
            .databaseBuilder(context, AppDatabase::class.java, "PokemonDatabase")
            .build()
    }
}