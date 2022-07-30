package com.example.myentertainment.interfaces

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myentertainment.Constants
import com.example.myentertainment.`object`.ValidationObject
import com.example.myentertainment.view.main.MainActivity

interface AddFragmentViewModelInterface {

    fun initView()
    fun setObservers()

    fun addingToDatabaseResult(
        addingToDatabaseResult: Boolean,
        context: Context,
        message: String,
        category: String
    ) {
        if (addingToDatabaseResult) {
            Toast.makeText(context, message, Toast.LENGTH_LONG)
                .show()
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(Constants.CATEGORY, category)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG).show()
        }
    }

    fun updateView(loading: Boolean, loadingSection: ConstraintLayout) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.INVISIBLE
        }
    }

    fun validationResult(validationResult: Int, context: Context, message: String) {
        when (validationResult) {
            ValidationObject.EMPTY_VALUES -> Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}