package com.example.myentertainment.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.Date
import com.example.myentertainment.data.UserProfile
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class FindFriendsViewModel: ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    @Named("usersReference")
    lateinit var databaseReference: DatabaseReference


    fun findFriends(phrase: String) {
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

                } else {
                    // TODO - no results
                }

            } else {
                // TODO - error
            }
        }
    }

    private fun containsPhrase(item: UserProfile, phrase: String): Boolean {
        return (item.username?.contains(phrase, true) == true || item.realName?.contains(phrase, true) == true)
    }

    private fun parseUserProfileObject(user: HashMap<String, UserProfile>): UserProfile {
        var userName = ""
        var realName = ""
        var city: String? = null
        var country: String? = null
        var birthDate: Date? = null
        var email: String? = null

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

        return UserProfile(userName, realName, city, country, birthDate, email)
    }

}
