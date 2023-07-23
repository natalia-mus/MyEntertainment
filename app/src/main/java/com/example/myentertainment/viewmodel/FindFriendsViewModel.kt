package com.example.myentertainment.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.StoragePathObject
import com.example.myentertainment.data.Date
import com.example.myentertainment.data.UserProfile
import com.example.myentertainment.data.UserProfileData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

class FindFriendsViewModel : UserProfileViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    val loading = MutableLiveData<Boolean>()
    val status = MutableLiveData<SearchUsersStatus>()
    val filteredUserProfiles = MutableLiveData<ArrayList<UserProfile>>()

    private var phrase = ""

    fun findFriends(phrase: String) {
        this.phrase = phrase
        getAllUsers()
    }

    override fun onUserProfilesChanged() {
        filteredUserProfiles.value?.clear()
        val filtered = ArrayList<UserProfile>()

        if (userProfiles.value != null) {
            for (user in userProfiles.value!!) {
                if (user.userProfileData != null) {
                    if (containsPhrase(user.userProfileData, phrase)) {
                        filtered.add(user)
                    }
                }
            }
        }

        filteredUserProfiles.value = filtered
    }

    private fun containsPhrase(item: UserProfileData, phrase: String): Boolean {
        return (item.username?.contains(phrase, true) == true || item.realName?.contains(phrase, true) == true)
    }

}


enum class SearchUsersStatus {
    SUCCESS, NO_RESULTS, ERROR
}
