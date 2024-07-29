package com.example.myentertainment

import android.content.Context
import android.content.SharedPreferences

object SharedPreferences {

    private const val SHARED_PREFERENCES = "shared_preferences"
    private const val SHOW_START_APP_ACTIVITY = "show_start_app_activity"

    fun getShowStartAppActivity(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(SHOW_START_APP_ACTIVITY, true)
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }
}