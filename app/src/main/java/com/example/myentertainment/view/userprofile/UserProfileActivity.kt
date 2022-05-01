package com.example.myentertainment.view.userprofile

import android.os.Bundle
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

    private fun initView(userProfileData: UserProfile) {
        username = findViewById(R.id.userProfile_username)
        realName = findViewById(R.id.userProfile_realName)
        city = findViewById(R.id.userProfile_city)
        country = findViewById(R.id.userProfile_country)
        age = findViewById(R.id.userProfile_age)

        username.text = userProfileData.username
        realName.text = userProfileData.firstName.plus(" ").plus(userProfileData.lastName)
        city.text = userProfileData.city
        country.text = userProfileData.country
        age.text = userProfileData.age.toString()
    }
}