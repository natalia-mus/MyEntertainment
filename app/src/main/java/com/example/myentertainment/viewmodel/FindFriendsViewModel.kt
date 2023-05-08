package com.example.myentertainment.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.StoragePathObject
import com.example.myentertainment.data.Date
import com.example.myentertainment.data.UserProfile
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import javax.inject.Inject
import javax.inject.Named

class FindFriendsViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    @Named("usersReference")
    lateinit var databaseReference: DatabaseReference

    @Inject
    lateinit var storageReference: StorageReference

    val loading = MutableLiveData<Boolean>()
    val status = MutableLiveData<SearchUsersStatus>()
    val users = MutableLiveData<ArrayList<UserProfile>>()
    val profilePictures = MutableLiveData<HashMap<String, Uri>>()

    fun findFriends(phrase: String) {
        loading.value = true
        val filtered = ArrayList<UserProfile>()

        // fetching all users:
        databaseReference.get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                if (task.result != null) {
                    val allUsers = task.result!!.value as HashMap<String, Any>

                    for (item in allUsers) {
                        val user = item.value as HashMap<String, UserProfile>
                        val userProfile = parseUserProfileObject(user)

                        if (containsPhrase(userProfile, phrase)) {
                            filtered.add(userProfile)
                        }
                    }

                    users.value = filtered
                    getProfilePictures()

                } else {
                    loading.value = false
                    users.value?.clear()
                    status.value = SearchUsersStatus.NO_RESULTS
                }

            } else {
                loading.value = false
                users.value?.clear()
                status.value = SearchUsersStatus.ERROR
            }
        }
    }

    private fun containsPhrase(item: UserProfile, phrase: String): Boolean {
        return (item.username?.contains(phrase, true) == true || item.realName?.contains(phrase, true) == true)
    }

    private fun getProfilePictures() {
        val pictures = HashMap<String, Uri>()
        if (users.value != null) {
            val usersSet = users.value!!

            var iterator = 0
            while (iterator < usersSet.size - 1) {
                val user = usersSet[iterator]
                profilePictureReference(user.userId!!).downloadUrl.addOnCompleteListener() { task ->
                    if (task.isSuccessful && task.result != null) {
                        pictures.put(user.userId, task.result!!)
                    }

                    if (iterator == usersSet.size - 1) {
                        profilePictures.value = pictures
                        status.value = if (users.value?.isNotEmpty() == true) SearchUsersStatus.SUCCESS else SearchUsersStatus.NO_RESULTS
                        loading.value = false
                    }
                }
                iterator++
            }

        }
    }

    private fun parseUserProfileObject(user: HashMap<String, UserProfile>): UserProfile {
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

        return UserProfile(userId, userName, realName, city, country, birthDate, email)
    }

    private fun profilePictureReference(userId: String): StorageReference {
        val path = StoragePathObject.PATH_PROFILE_PICTURES + "/" + userId
        return storageReference.child(path)
    }

}


enum class SearchUsersStatus {
    SUCCESS, NO_RESULTS, ERROR
}
