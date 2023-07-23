package com.example.myentertainment.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.StoragePathObject
import com.example.myentertainment.data.Date
import com.example.myentertainment.data.UserProfile
import com.example.myentertainment.data.UserProfileData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Named

open class UserProfileViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    lateinit var storageReference: StorageReference

    @Inject
    @Named("usersReference")
    lateinit var usersReference: DatabaseReference


    val updatingProfilePictureSuccessful = MutableLiveData<Boolean>()
    val user = databaseAuth.uid.toString()
    val userProfiles = MutableLiveData<ArrayList<UserProfile>>()

    protected val userProfilesArray = ArrayList<UserProfile>()

    private var userProfileData: UserProfileData? = null
    private var profilePicture: Uri? = null
    private var requests = 0


    fun getAllUsers() {
        userProfilesArray.clear()
        usersReference.get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                if (task.result != null) {
                    val allUsers = task.result!!.value as HashMap<String, Any>

                    for (item in allUsers) {
                        val user = item.value as HashMap<String, UserProfileData>
                        val userProfile = parseUserProfileObject(user)

//                        if (containsPhrase(userProfile, phrase)) {
//                            filtered.add(userProfile)
//                        }

                        //userProfilesArray.add(UserProfile(userProfile, null))

                        userProfileData = userProfile
                        getProfilePictureUrl(userProfile.userId.toString())
                    }

//                    userProfiles.value = userProfilesArray
//                    users.value = filtered
//                    getProfilePictures()

                } else {
//                    loading.value = false
//                    users.value?.clear()
//                    status.value = SearchUsersStatus.NO_RESULTS
                }

            } else {
//                loading.value = false
//                users.value?.clear()
//                status.value = SearchUsersStatus.ERROR
            }
        }
    }

    fun getUserProfile(userId: String?) {
        getUserProfileData(userId)
    }

    fun getUserProfileData(userId: String?) {
        val userIds = ArrayList<String?>()
        userIds.add(userId)
        getUserProfiles(userIds)
    }

    protected fun getUserProfiles(userIds: ArrayList<String?>) {
        for (userId in userIds) {
            requests++
            val id = userId ?: user

            usersReference.child(id).get().addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    userProfileData = task.result?.getValue(UserProfileData::class.java)
                    getProfilePictureUrl(id)

                } else {
                    requests--
                    userProfileData = null
                }
            }
        }
    }

    protected fun getProfilePictureUrl(id: String) {
        profilePictureReference(id).downloadUrl
            .addOnSuccessListener {
                if (requests > 0) requests--
                profilePicture = it
                setUserProfileValue()

            }.addOnFailureListener {
                val errorCode = (it as StorageException).errorCode

                if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    if (requests > 0) requests--
                    profilePicture = null
                    setUserProfileValue()
                } else {
                    updatingProfilePictureSuccessful.value = false
                }
            }
    }

    protected fun profilePictureReference(id: String): StorageReference {
        val path = StoragePathObject.PATH_PROFILE_PICTURES + "/" + id
        return storageReference.child(path)
    }

    protected open fun onUserProfilesChanged() {}

    private fun parseUserProfileObject(user: HashMap<String, UserProfileData>): UserProfileData {
        var userId = ""
        var userName = ""
        var realName = ""
        var city: String? = null
        var country: String? = null
        var birthDate: Date? = null
        var email: String? = null

        if (user.containsKey("userId")) {
            userId = user["userId"].toString()
        }

        if (user.containsKey("username")) {
            userName = user["username"].toString()
        }

        if (user.containsKey("realName")) {
            realName = user["realName"].toString()
        }

        if (user.containsKey("city")) {
            city = user["city"].toString()
        }

        if (user.containsKey("country")) {
            country = user["country"].toString()
        }

        if (user.containsKey("birthDate")) {
            val date = user["birthDate"] as HashMap<String, Any>

            val year = date["year"].toString().toIntOrNull()
            val month = date["month"].toString().toIntOrNull()
            val day = date["day"].toString().toIntOrNull()

            if (year != null && month != null && day != null) {
                birthDate = Date(year, month, day)
            }
        }

        if (user.containsKey("email")) {
            email = user["email"].toString()
        }

        return UserProfileData(userId, userName, realName, city, country, birthDate, email)
    }

    private fun setUserProfileValue() {
        if (userProfileData != null || profilePicture != null) {
            userProfilesArray.add(UserProfile(userProfileData, profilePicture))

            if (requests == 0) {
                userProfiles.value = userProfilesArray
                onUserProfilesChanged()
            }
        }
    }
}