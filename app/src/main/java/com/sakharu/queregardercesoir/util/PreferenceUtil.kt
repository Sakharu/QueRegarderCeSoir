package com.sakharu.queregardercesoir.util

import android.content.Context
import androidx.preference.PreferenceManager
import java.util.ArrayList
import java.util.HashSet

class PreferenceUtil
{

    companion object
    {
        fun getString(context: Context, key: String, dValue: String): String {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(key, dValue)!!
        }

        fun setString(context: Context, key: String, value: String?) {
            if (!value.isNullOrEmpty())
            {
                val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = preferences.edit()
                editor.putString(key, value)
                editor.apply()
            }
        }

        fun getInt(context: Context, key: String, dValue: Int): Int {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(key, dValue)
        }

        fun setInt(context: Context, key: String, value: Int) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putInt(key, value)
            editor.apply()
        }

        fun getLong(context: Context, key: String, dValue: Long): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(key, dValue)
        }

        fun setLong(context: Context, key: String, value: Long) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putLong(key, value)
            editor.apply()
        }

        fun getBoolean(context: Context, key: String, dValue: Boolean): Boolean
        {
            return try
            {
                val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                preferences.getBoolean(key, dValue)
            }
            catch (e:Exception){ false }

        }

        fun setBoolean(context: Context, key: String, value: Boolean)
        {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun remove(context: Context, key: String) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.remove(key)
            editor.apply()
        }

        fun setLongArray(context: Context, key: String, array: ArrayList<Long>) {
            val set = HashSet<String>()
            for (l in array) {
                set.add(l.toString())
            }
            val preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context)//context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
            val editor = preferences.edit()
            editor.putStringSet(key, set)
            editor.apply()
        }

        fun getLongArray(context: Context, key: String): ArrayList<Long> {
            val preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context)//context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
            val set = preferences.getStringSet(key, null)
            val tmp = ArrayList<String>()
            tmp.addAll(set!!)
            val res = ArrayList<Long>()
            for (s in tmp) {
                res.add(java.lang.Long.parseLong(s))
            }
            return res
        }


    }
}