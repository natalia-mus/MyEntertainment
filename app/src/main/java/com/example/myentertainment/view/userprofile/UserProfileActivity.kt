package com.example.myentertainment.view.userprofile

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.data.UserProfile
import com.example.myentertainment.viewmodel.userprofile.UserProfileActivityViewModel

class UserProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: UserProfileActivityViewModel

    private lateinit var username: TextView
    private lateinit var realName: TextView
    private lateinit var city: TextView
    private lateinit var country: TextView
    private lateinit var age: TextView
    private lateinit var editButton: ImageButton
    private lateinit var saveButton: Button
    private lateinit var usernameEditable: EditText
    private lateinit var realNameEditable: EditText
    private lateinit var cityEditable: EditText
    private lateinit var countryEditable: EditText
    private lateinit var ageEditable: EditText
    private lateinit var loadingSection: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        viewModel = ViewModelProvider(this).get(UserProfileActivityViewModel::class.java)
        setObservers()
        viewModel.getUserProfileData()
        initView()
    }

    private fun setObservers() {
        viewModel.loading.observe(this, { loadingStatusChanged(it) })
        viewModel.userProfile.observe(this, { updateView(it) })
        viewModel.addingToDatabaseResult.observe(this, { addingToDatabaseResult(it) })
    }

    private fun initView() {
        username = findViewById(R.id.userProfile_username)
        realName = findViewById(R.id.userProfile_realName)
        city = findViewById(R.id.userProfile_city)
        country = findViewById(R.id.userProfile_country)
        age = findViewById(R.id.userProfile_age)
        editButton = findViewById(R.id.userProfile_buttonEdit)
        saveButton = findViewById(R.id.userProfile_buttonSave)

        usernameEditable = findViewById(R.id.userProfile_username_editable)
        realNameEditable = findViewById(R.id.userProfile_realName_editable)
        cityEditable = findViewById(R.id.userProfile_city_editable)
        countryEditable = findViewById(R.id.userProfile_country_editable)
        ageEditable = findViewById(R.id.userProfile_age_editable)

        editButton.setOnClickListener() {
            prepareEditing()
        }

        saveButton.setOnClickListener() {
            updateUserProfileData()
        }
    }

    private fun updateView(userProfileData: UserProfile?) {
        if (userProfileData != null) {
            username.visibility = View.VISIBLE
            realName.visibility = View.VISIBLE
            city.visibility = View.VISIBLE
            country.visibility = View.VISIBLE
            age.visibility = View.VISIBLE
            usernameEditable.visibility = View.GONE
            realNameEditable.visibility = View.GONE
            cityEditable.visibility = View.GONE
            countryEditable.visibility = View.GONE
            ageEditable.visibility = View.GONE
            saveButton.visibility = View.GONE
            editButton.visibility = View.VISIBLE

            username.text = userProfileData.username
            realName.text = userProfileData.realName
            city.text = if (userProfileData.city?.isNotEmpty() == true) userProfileData.city else getString(R.string.none)
            country.text = if (userProfileData.country?.isNotEmpty() == true) userProfileData.country else getString(R.string.none)
            age.text = if (userProfileData.age?.toString()?.isNotEmpty() == true) userProfileData.age.toString() else getString(R.string.none)

            usernameEditable.setText(userProfileData.username)
            realNameEditable.setText(userProfileData.realName)
            cityEditable.setText(userProfileData.city)
            countryEditable.setText(userProfileData.country)
            ageEditable.setText(userProfileData.age.toString())
        } else {
            onBackPressed()
            Toast.makeText(applicationContext, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    private fun prepareEditing() {
        editButton.visibility = View.GONE
        saveButton.visibility = View.VISIBLE
        username.visibility = View.GONE
        realName.visibility = View.GONE
        city.visibility = View.GONE
        country.visibility = View.GONE
        age.visibility = View.GONE
        usernameEditable.visibility = View.VISIBLE
        realNameEditable.visibility = View.VISIBLE
        cityEditable.visibility = View.VISIBLE
        countryEditable.visibility = View.VISIBLE
        ageEditable.visibility = View.VISIBLE
    }

    private fun updateUserProfileData() {
        val username = usernameEditable.text.toString()
        val realName = realNameEditable.text.toString()
        val city = cityEditable.text.toString()
        val country = countryEditable.text.toString()
        val age = ageEditable.text.toString().toInt()

        val userProfileData = UserProfile(username, realName, city, country, age)
        viewModel.updateUserProfileData(userProfileData)
    }

    private fun loadingStatusChanged(loading: Boolean) {
        if (!this::loadingSection.isInitialized) {
            loadingSection = findViewById(R.id.userProfile_loadingSection)
        }
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.GONE
        }
    }

    private fun addingToDatabaseResult(
        addingToDatabaseResult: Boolean
    ) {
        if (addingToDatabaseResult) {
            Toast.makeText(this, getString(R.string.user_profile_data_updated), Toast.LENGTH_LONG)
                .show()
            hideKeyboard()
            viewModel.getUserProfileData()
        } else {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = findViewById<View>(android.R.id.content).rootView
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}