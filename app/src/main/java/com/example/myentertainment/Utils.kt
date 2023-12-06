package com.example.myentertainment

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object Utils {

    fun hideKeyboard(context: Context) {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = (context as Activity).findViewById<View>(android.R.id.content).rootView
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}