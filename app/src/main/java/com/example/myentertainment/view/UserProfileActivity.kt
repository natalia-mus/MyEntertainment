package com.example.myentertainment.view

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.data.Date
import com.example.myentertainment.data.UserProfile
import com.example.myentertainment.view.authentication.AuthenticationActivity
import com.example.myentertainment.viewmodel.userprofile.UserProfileActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.ByteArrayOutputStream

class UserProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: UserProfileActivityViewModel
    private var changesToSave = false

    private lateinit var photo: ImageView
    private lateinit var username: TextView
    private lateinit var realName: TextView
    private lateinit var city: TextView
    private lateinit var country: TextView
    private lateinit var birthDate: TextView
    private lateinit var age: TextView
    private lateinit var removeBirthDate: ImageView
    private lateinit var email: TextView
    private lateinit var editButton: ImageButton
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var usernameEditable: EditText
    private lateinit var realNameEditable: EditText
    private lateinit var cityEditable: EditText
    private lateinit var countryEditable: EditText
    private lateinit var changePassword: TextView
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var buttonsSection: LinearLayout

    private var newBirthDate: Date? = null
    private var currentBirthDate: Date? = null
    private lateinit var yrs: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        yrs = resources.getString(R.string.yrs)
        viewModel = ViewModelProvider(this).get(UserProfileActivityViewModel::class.java)
        setObservers()
        initView()
        viewModel.getUserProfileData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_CODE_CAPTURE_CAMERA_IMAGE -> {
                    val file = data?.getParcelableExtra<Bitmap>("data")
                    if (file != null) {
                        val outputStream = ByteArrayOutputStream()
                        file.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        viewModel.changeProfilePicture(outputStream.toByteArray())
                    }
                }
                Constants.REQUEST_CODE_CAPTURE_GALLERY_IMAGE -> {
                    if (data != null && data.data != null) {
                        val file = data.data!!
                        viewModel.changeProfilePicture(file)
                    }
                }
            }
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

    private fun areValuesDifferent(originalValue: String, newValue: String): Boolean {
        return if (originalValue == "-" && newValue.isEmpty()) false
        else originalValue != newValue
    }

    private fun changePassword() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        intent.putExtra(Constants.CHANGE_PASSWORD, true)
        startActivity(intent)
    }

    private fun changeProfilePicture() {
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
                openGallery()
                photoSourcePanel.dismiss()
            }

        if (viewModel.profilePicture.value == null) {
            photoSourcePanelView.findViewById<LinearLayout>(R.id.panelPhotoSource_remove).visibility =
                View.GONE

        } else {
            photoSourcePanelView.findViewById<LinearLayout>(R.id.panelPhotoSource_remove)
                .setOnClickListener() {
                    removeProfilePicture()
                    photoSourcePanel.dismiss()
                }
        }

        photoSourcePanel.setContentView(photoSourcePanelView)
        photoSourcePanel.show()
    }

    private fun changesToSave(): Boolean {
        if (changesToSave) return true

        val username = usernameEditable.text.toString()
        val realName = realNameEditable.text.toString()
        val city = cityEditable.text.toString()
        val country = countryEditable.text.toString()

        return this.username.text.toString() != username ||
                this.realName.text.toString() != realName ||
                areValuesDifferent(this.city.text.toString(), city) ||
                areValuesDifferent(this.country.text.toString(), country)
    }

    private fun handleDatabaseTaskExecutionResult(successful: Boolean) {
        if (!successful) {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun handleLoadingStatus(loading: Boolean) {
        if (!this::loadingSection.isInitialized) {
            loadingSection = findViewById(R.id.userProfile_loadingSection)
        }
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.GONE
        }
    }

    private fun handleUpdatingUserProfileDataResult(successful: Boolean) {
        if (successful) {
            Toast.makeText(this, getString(R.string.user_profile_data_updated), Toast.LENGTH_LONG)
                .show()
            hideKeyboard()
            viewModel.getUserProfileData()
        } else handleDatabaseTaskExecutionResult(false)
    }

    private fun handleValidationResult(validationResult: ValidationResult) {
        if (validationResult == ValidationResult.EMPTY_VALUES) {
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

    private fun initView() {
        photo = findViewById(R.id.userProfile_photo)
        username = findViewById(R.id.userProfile_username)
        realName = findViewById(R.id.userProfile_realName)
        city = findViewById(R.id.userProfile_city)
        country = findViewById(R.id.userProfile_country)
        birthDate = findViewById(R.id.userProfile_birthDate)
        age = findViewById(R.id.userProfile_age)
        removeBirthDate = findViewById(R.id.userProfile_removeBirthDate)
        email = findViewById(R.id.userProfile_email)
        editButton = findViewById(R.id.userProfile_buttonEdit)
        saveButton = findViewById(R.id.userProfile_buttonSave)
        cancelButton = findViewById(R.id.userProfile_buttonCancel)
        buttonsSection = findViewById(R.id.userProfile_buttonsSection)
        usernameEditable = findViewById(R.id.userProfile_username_editable)
        realNameEditable = findViewById(R.id.userProfile_realName_editable)
        cityEditable = findViewById(R.id.userProfile_city_editable)
        countryEditable = findViewById(R.id.userProfile_country_editable)
        changePassword = findViewById(R.id.userProfile_changePassword)

        removeBirthDate.visibility = View.GONE

        photo.setOnClickListener() {
            changeProfilePicture()
        }

        changePassword.setOnClickListener() {
            changePassword()
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

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, Constants.REQUEST_CODE_CAPTURE_CAMERA_IMAGE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = Constants.INTENT_TYPE_IMAGE
        startActivityForResult(intent, Constants.REQUEST_CODE_CAPTURE_GALLERY_IMAGE)
    }

    private fun prepareBirthDate(date: Date?) {
        var birthDateValue = resources.getString(R.string.unknown)

        if (date != null) {
            val monthName = date.getMonthShortName()
            val day = date.day!!
            val year = date.year!!
            val userAge = date.getUserAge()!!
            birthDateValue = "$monthName $day, $year"
            age.text = "($userAge $yrs)"
            age.visibility = View.VISIBLE
        } else {
            age.visibility = View.GONE
            removeBirthDate.visibility = View.GONE
        }

        setBirthdateLabel(birthDateValue)
    }

    private fun refreshProfilePicture(uri: Uri?) {
        val placeholder = ResourcesCompat.getDrawable(resources, R.drawable.placeholder_user, null)
        Glide.with(this)
            .load(uri)
            .placeholder(placeholder)
            .circleCrop()
            .into(photo)
    }

    private fun removeBirthDate() {
        val removePanel = BottomSheetDialog(this)
        val removePanelView = LayoutInflater.from(this).inflate(R.layout.panel_remove, findViewById(R.id.panelRemove))

        removePanelView.findViewById<TextView>(R.id.panelRemove_message).text = resources.getString(R.string.remove_birthdate_message)

        removePanelView.findViewById<LinearLayout>(R.id.panelRemove_confirmationButton).setOnClickListener() {
            removePanel.dismiss()
            newBirthDate = null
            prepareBirthDate(newBirthDate)
            setBirthdateLabel(null)
            changesToSave = true
        }

        removePanelView.findViewById<LinearLayout>(R.id.panelRemove_dismissButton).setOnClickListener() {
            removePanel.dismiss()
        }

        removePanel.setContentView(removePanelView)
        removePanel.show()
    }

    private fun removeProfilePicture() {
        val removePanel = BottomSheetDialog(this)
        val removePanelView = LayoutInflater.from(this).inflate(R.layout.panel_remove, findViewById(R.id.panelRemove))

        removePanelView.findViewById<TextView>(R.id.panelRemove_message).text = resources.getString(R.string.remove_profile_picture_message)

        removePanelView.findViewById<LinearLayout>(R.id.panelRemove_confirmationButton).setOnClickListener() {
            removePanel.dismiss()
            viewModel.removeProfilePicture()
        }

        removePanelView.findViewById<LinearLayout>(R.id.panelRemove_dismissButton).setOnClickListener() {
            removePanel.dismiss()
            changeProfilePicture()
        }

        removePanel.setContentView(removePanelView)
        removePanel.show()
    }

    /**
     * Sets birthDate label text and changes its appearance
     */
    private fun setBirthdateLabel(birthdateValue: String?) {
        var text = birthdateValue
        var textColor = resources.getColor(R.color.default_text_color, null)
        var typeface = Typeface.create(birthDate.typeface, Typeface.NORMAL)

        if (birthdateValue == null) {
            text = resources.getString(R.string.set)
            textColor = resources.getColor(R.color.blue_light, null)
            typeface = Typeface.create(birthDate.typeface, Typeface.BOLD)
        }

        birthDate.text = text
        birthDate.setTextColor(textColor)
        birthDate.typeface = typeface
    }

    private fun setObservers() {
        viewModel.loading.observe(this) { handleLoadingStatus(it) }
        viewModel.userProfile.observe(this) { updateView(it) }
        viewModel.profilePicture.observe(this) { refreshProfilePicture(it) }
        viewModel.validationResult.observe(this) { handleValidationResult(it) }
        viewModel.updatingUserProfileDataSuccessful.observe(this) { handleUpdatingUserProfileDataResult(it) }
        viewModel.databaseTaskExecutionSuccessful.observe(this) { handleDatabaseTaskExecutionResult(it) }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = Dialog(this)
        val datePickerDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_date_picker, findViewById(R.id.datePicker_dialog))
        val datePicker = datePickerDialogView.findViewById<DatePicker>(R.id.datePicker_date)

        if (currentBirthDate != null) {
            datePicker.updateDate(currentBirthDate!!.year!!, currentBirthDate!!.month!!, currentBirthDate!!.day!!)
        }

        datePickerDialogView.findViewById<Button>(R.id.datePicker_buttonSave).setOnClickListener() {
            datePicker.clearFocus()

            val month = datePicker.month
            val day = datePicker.dayOfMonth
            val year = datePicker.year

            newBirthDate = Date(year, month, day)
            prepareBirthDate(newBirthDate!!)

            datePickerDialog.dismiss()
            removeBirthDate.visibility = View.VISIBLE
            age.visibility = View.GONE
            changesToSave = true
        }

        datePickerDialogView.findViewById<Button>(R.id.datePicker_buttonCancel).setOnClickListener() {
            datePickerDialog.dismiss()
        }

        if (datePickerDialogView.parent != null) {
            val parent = datePickerDialogView.parent as ViewGroup
            parent.removeView(datePickerDialogView)
        }
        datePickerDialog.setContentView(datePickerDialogView)
        datePickerDialog.show()
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
            changePassword.visibility = View.VISIBLE

            newBirthDate = currentBirthDate

            birthDate.isClickable = true
            birthDate.setOnClickListener() {
                showDatePickerDialog()
            }

            if (birthDate.text != resources.getString(R.string.unknown)) {
                removeBirthDate.visibility = View.VISIBLE
                removeBirthDate.setOnClickListener() {
                    removeBirthDate()
                }
            } else {
                setBirthdateLabel(null)
            }

        } else {
            username.visibility = View.VISIBLE
            realName.visibility = View.VISIBLE
            city.visibility = View.VISIBLE
            country.visibility = View.VISIBLE
            birthDate.visibility = View.VISIBLE
            removeBirthDate.visibility = View.GONE
            usernameEditable.visibility = View.GONE
            realNameEditable.visibility = View.GONE
            cityEditable.visibility = View.GONE
            countryEditable.visibility = View.GONE
            changePassword.visibility = View.GONE
            buttonsSection.visibility = View.GONE
            editButton.visibility = View.VISIBLE

            birthDate.isClickable = false
            prepareBirthDate(currentBirthDate)
            removeBirthDate.visibility = View.GONE

            usernameEditable.setText(if (username.text != "-") username.text else "")
            realNameEditable.setText(if (realName.text != "-") realName.text else "")
            cityEditable.setText(if (city.text != "-") city.text else "")
            countryEditable.setText(if (country.text != "-") country.text else "")

            changesToSave = false
        }
    }

    private fun tryToOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), Constants.REQUEST_CODE_PERMISSION_CAMERA)
        }
    }

    private fun updateUserProfileData() {
        if (changesToSave()) {
            val username = usernameEditable.text.toString()
            val realName = realNameEditable.text.toString()
            val city = cityEditable.text.toString()
            val country = countryEditable.text.toString()
            val email = email.text.toString()

            val userProfileData =
                UserProfile(username, realName, city, country, newBirthDate, email)
            viewModel.updateUserProfileData(userProfileData)
        } else {
            switchViewMode(false)
        }
    }

    private fun updateView(userProfileData: UserProfile?) {
        if (userProfileData != null) {
            username.text = userProfileData.username
            realName.text = userProfileData.realName
            city.text = if (userProfileData.city?.isNotEmpty() == true) userProfileData.city else getString(R.string.none)
            country.text = if (userProfileData.country?.isNotEmpty() == true) userProfileData.country else getString(R.string.none)
            email.text = userProfileData.email
            currentBirthDate = userProfileData.birthDate

            switchViewMode(false)

        } else {
            finish()
            Toast.makeText(applicationContext, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

}