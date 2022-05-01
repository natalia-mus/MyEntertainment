package com.example.myentertainment.view.userprofile

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        viewModel = ViewModelProvider(this).get(UserProfileActivityViewModel::class.java)
        setObservers()
        viewModel.getUserProfileData()
    }

    private fun setObservers() {
        viewModel.userProfile.observe(this, { initView(it) })
    }

    private fun initView(userProfileData: UserProfile?) {
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

        if (userProfileData != null) {
            username.text = userProfileData.username
            realName.text = userProfileData.realName
            city.text = userProfileData.city
            country.text = userProfileData.country
            age.text = userProfileData.age.toString()

            usernameEditable.setText(userProfileData.username)
            realNameEditable.setText(userProfileData.realName)
            cityEditable.setText(userProfileData.city)
            countryEditable.setText(userProfileData.country)
            ageEditable.setText(userProfileData.age.toString())
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
}