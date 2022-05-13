package com.example.myentertainment.view.userprofile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.`object`.ValidationObject
import com.example.myentertainment.data.UserProfile
import com.example.myentertainment.viewmodel.userprofile.UserProfileActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class UserProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: UserProfileActivityViewModel

    private lateinit var photo: ImageView
    private lateinit var username: TextView
    private lateinit var realName: TextView
    private lateinit var city: TextView
    private lateinit var country: TextView
    private lateinit var age: TextView
    private lateinit var email: TextView
    private lateinit var editButton: ImageButton
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var usernameEditable: EditText
    private lateinit var realNameEditable: EditText
    private lateinit var cityEditable: EditText
    private lateinit var countryEditable: EditText
    private lateinit var ageEditable: EditText
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var buttonsSection: LinearLayout

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
        viewModel.validationResult.observe(this, { validationResult(it) })
        viewModel.addingToDatabaseResult.observe(this, { addingToDatabaseResult(it) })
    }

    private fun initView() {
        photo = findViewById(R.id.userProfile_photo)
        username = findViewById(R.id.userProfile_username)
        realName = findViewById(R.id.userProfile_realName)
        city = findViewById(R.id.userProfile_city)
        country = findViewById(R.id.userProfile_country)
        age = findViewById(R.id.userProfile_age)
        email = findViewById(R.id.userProfile_email)
        editButton = findViewById(R.id.userProfile_buttonEdit)
        saveButton = findViewById(R.id.userProfile_buttonSave)
        cancelButton = findViewById(R.id.userProfile_buttonCancel)
        buttonsSection = findViewById(R.id.userProfile_buttonsSection)

        usernameEditable = findViewById(R.id.userProfile_username_editable)
        realNameEditable = findViewById(R.id.userProfile_realName_editable)
        cityEditable = findViewById(R.id.userProfile_city_editable)
        countryEditable = findViewById(R.id.userProfile_country_editable)
        ageEditable = findViewById(R.id.userProfile_age_editable)

        photo.setOnClickListener() {
            changeProfilePhoto()
        }

        editButton.setOnClickListener() {
            switchViewMode(true)
        }

        saveButton.setOnClickListener() {
            updateUserProfileData()
        }

        cancelButton.setOnClickListener() {
            switchViewMode(false)
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
            buttonsSection.visibility = View.GONE
            editButton.visibility = View.VISIBLE

            username.text = userProfileData.username
            realName.text = userProfileData.realName
            city.text = if (userProfileData.city?.isNotEmpty() == true) userProfileData.city else getString(R.string.none)
            country.text = if (userProfileData.country?.isNotEmpty() == true) userProfileData.country else getString(R.string.none)
            age.text = if (userProfileData.age?.toString()?.isNotEmpty() == true) userProfileData.age.toString() else getString(R.string.none)
            email.text = userProfileData.email

            usernameEditable.setText(userProfileData.username)
            realNameEditable.setText(userProfileData.realName)
            cityEditable.setText(userProfileData.city)
            countryEditable.setText(userProfileData.country)
            ageEditable.setText(if (userProfileData.age?.toString()?.isNotEmpty() == true) userProfileData.age.toString() else "")
        } else {
            onBackPressed()
            Toast.makeText(applicationContext, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    private fun switchViewMode(switchToEditMode: Boolean) {
        if (switchToEditMode) {
            editButton.visibility = View.GONE
            buttonsSection.visibility = View.VISIBLE
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
        } else {
            editButton.visibility = View.VISIBLE
            buttonsSection.visibility = View.GONE
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

            usernameEditable.setText(if (username.text != "-") username.text else "")
            realNameEditable.setText(if (realName.text != "-") realName.text else "")
            cityEditable.setText(if (city.text != "-") city.text else "")
            countryEditable.setText(if (country.text != "-") country.text else "")
            ageEditable.setText(if (age.text != "-") age.text else "")
        }
    }

    private fun updateUserProfileData() {
        val username = usernameEditable.text.toString()
        val realName = realNameEditable.text.toString()
        val city = cityEditable.text.toString()
        val country = countryEditable.text.toString()
        val age = ageEditable.text.toString().toInt()
        val email = email.text.toString()

        val userProfileData = UserProfile(username, realName, city, country, age, email)
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

    private fun changeProfilePhoto() {
        val photoSourcePanel = BottomSheetDialog(this)
        val photoSourcePanelView = LayoutInflater.from(this)
            .inflate(R.layout.panel_photo_source, findViewById(R.id.panelPhotoSource_container))

        photoSourcePanelView.findViewById<LinearLayout>(R.id.photoSource_camera)
            .setOnClickListener() {
                tryToOpenCamera()
                photoSourcePanel.dismiss()
            }

        photoSourcePanelView.findViewById<LinearLayout>(R.id.photoSource_gallery)
            .setOnClickListener() {
                // TODO
            }

        photoSourcePanel.setContentView(photoSourcePanelView)
        photoSourcePanel.show()
    }

    private fun tryToOpenCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                Constants.REQUEST_CODE_PERMISSION_CAMERA
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSION_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) openCamera()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, Constants.REQUEST_CODE_CAPTURE_CAMERA_IMAGE)
    }

    private fun validationResult(validationResult: Int) {
        if (validationResult == ValidationObject.EMPTY_VALUES) {
            Toast.makeText(this, getString(R.string.username_can_not_be_empty), Toast.LENGTH_LONG)
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