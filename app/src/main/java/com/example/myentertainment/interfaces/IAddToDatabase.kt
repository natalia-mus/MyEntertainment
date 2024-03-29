package com.example.myentertainment.interfaces

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.view.main.MainActivity

interface IAddToDatabase {

    fun initView()
    fun setObservers()

    fun handleAddingToDatabaseResult(
        addingToDatabaseResult: Boolean,
        context: Context,
        message: String,
        category: CategoryObject
    ) {
        if (addingToDatabaseResult) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(Constants.CATEGORY, category.categoryName)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        } else {
            Toast.makeText(context, context.resources.getString(R.string.error), Toast.LENGTH_LONG).show()
        }
    }

    fun handleValidationResult(validationResult: ValidationResult, context: Context, message: String) {
        when (validationResult) {
            ValidationResult.EMPTY_VALUES -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun updateView(loading: Boolean, loadingSection: ConstraintLayout) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.GONE
        }
    }

}