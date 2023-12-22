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


    val currentUser = databaseAuth.uid.toString()
    val userProfiles = MutableLiveData<ArrayList<UserProfile>>()
    val userProfilesData = MutableLiveData<ArrayList<UserProfileData>>()

    protected val userProfilesArray = ArrayList<UserProfile>()

    private var profilePicture: Uri? = null
    private var requests = 0
    private var userIdsToFetch = ArrayList<String>()
    private var userProfileData: UserProfileData? = null
    private var userProfilesCache = ArrayList<UserProfile>()
    private var userProfilesDataCache = ArrayList<UserProfileData>()


    fun getAllUsers() {
        if (userProfilesDataCache.isEmpty()) {
            val users = ArrayList<UserProfileData>()

            usersReference.get().addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val allUsersMap = task.result?.value as HashMap<String, UserProfileData>

                    for (item in allUsersMap) {
                        val user = item.value as HashMap<String, UserProfileData>
                        val userProfile = parseUserProfileObject(user)
                        users.add(userProfile)
                    }

                    userProfilesData.value = users
                    userProfilesDataCache = ArrayList(users)
                    onAllUsersChanged()
                }
            }

        } else {
            userProfilesData.value = userProfilesDataCache
            onAllUsersChanged()
        }
    }

    fun getUserProfile(userId: String?) {
        val userIds = ArrayList<String?>()
        val id = userId ?: currentUser
        userIds.add(id)
        getUserProfiles(userIds)
    }

    protected fun getProfilePictureUrl(id: String, getFromCache: Boolean = true) {
        val userProfileFromCache = getUserProfileFromCache(id)
        if (getFromCache && userProfileFromCache != null) {
            if (requests > 0) requests--
            userProfileData = userProfileFromCache.userProfileData
            profilePicture = userProfileFromCache.userProfilePicture
            setUserProfileValue()

        } else {
            getProfilePictureReference(id).downloadUrl
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
                        onGettingProfilePictureFailed()
                    }
                }
        }
    }

    protected fun getProfilePictureUrls() {
        if (userProfilesData.value != null && userProfilesData.value!!.isNotEmpty()) {
            userProfileData = userProfilesData.value!![0]

            if (userProfileData != null) {
                val cachedUserProfile = getUserProfileFromCache(userProfileData!!.userId)

                if (cachedUserProfile != null) {
                    userProfilesArray.add(cachedUserProfile)
                    userProfilesData.value?.remove(userProfileData)
                    fetchAnotherProfilePicture()

                } else {
                    val id = userProfileData!!.userId
                    if (id != null) {
                        getProfilePictureReference(id).downloadUrl
                            .addOnSuccessListener {
                                profilePicture = it
                                joinUserProfilePicture()

                            }.addOnFailureListener {
                                val errorCode = (it as StorageException).errorCode

                                if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                                    profilePicture = null
                                    joinUserProfilePicture()
                                } else {
                                    onGettingProfilePictureFailed()
                                }
                            }
                    }
                }
            }
        }
    }

    /**
     * Gets user profile after profile update - ignores cached user profile
     */
    protected fun getUpdatedUserProfile() {
        userProfilesDataCache.clear()
        userProfilesCache.clear()
        getUserProfile(currentUser)
    }

    protected fun getUserProfileData() {
        if (userIdsToFetch.isNotEmpty()) {
            val id = userIdsToFetch[0]
            requests++

            val userProfileDataFromCache = getUserProfileDataFromCache(id)
            if (userProfileDataFromCache != null) {
                userProfileData = userProfileDataFromCache
                getProfilePictureUrl(id)

            } else {
                usersReference.child(id).get().addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        userProfileData = task.result?.getValue(UserProfileData::class.java)
                        if (userProfileData != null) {
                            userProfilesDataCache.add(userProfileData!!)
                        }
                        getProfilePictureUrl(id)

                    } else {
                        requests--
                        userProfileData = null
                    }
                }
            }
        }
    }

    protected fun getUserProfiles(userIds: ArrayList<String?>) {
        userProfilesArray.clear()
        setUserIdsToFetch(userIds)
        getUserProfileData()
    }

    protected open fun onAllUsersChanged() {}

    protected open fun onGettingProfilePictureFailed() {}

    protected open fun onUserProfilesChanged() {}

    protected fun getProfilePictureReference(id: String): StorageReference {
        val path = StoragePathObject.PATH_PROFILE_PICTURES + "/" + id
        return storageReference.child(path)
    }

    /**
     * Joins profile picture with profile data
     */
    private fun joinUserProfilePicture() {
        if (userProfileData != null) {
            userProfilesArray.add(UserProfile(userProfileData, profilePicture))
            userProfilesData.value?.remove(userProfileData)
            fetchAnotherProfilePicture()
        }
    }

    private fun fetchAnotherProfilePicture() {
        if (userProfilesData.value?.isEmpty() == true) {
            userProfiles.value = userProfilesArray
            userProfilesCache = ArrayList(userProfilesArray)
            onUserProfilesChanged()

        } else {
            getProfilePictureUrls()
        }
    }

    private fun getUserProfileDataFromCache(userId: String): UserProfileData? {
        for (element in userProfilesDataCache) {
            if (element.userId == userId) {
                return element
            }
        }
        return null
    }

    private fun getUserProfileFromCache(userId: String?): UserProfile? {
        if (userId != null) {
            for (element in userProfilesCache) {
                if (element.userProfileData?.userId == userId) {
                    return element
                }
            }
        }
        return null
    }

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

    private fun setUserIdsToFetch(userIds: ArrayList<String?>) {
        for (id in userIds) {
            if (id != null) {
                userIdsToFetch.add(id)
            }
        }
    }

    private fun setUserProfileValue() {
        if (userProfileData != null || profilePicture != null) {
            userProfilesArray.add(UserProfile(userProfileData, profilePicture))

            if (userProfileData!!.userId != null) {
                userIdsToFetch.remove(userProfileData!!.userId)
            }

            if (userIdsToFetch.isEmpty()) {
                userProfiles.value = userProfilesArray
                userProfilesCache = ArrayList(userProfilesArray)
                onUserProfilesChanged()

            } else {
                getUserProfileData()
            }
        }
    }
}