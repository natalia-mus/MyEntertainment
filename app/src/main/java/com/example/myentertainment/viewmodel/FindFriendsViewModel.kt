package com.example.myentertainment.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.UserProfile
import com.example.myentertainment.data.UserProfileData

class FindFriendsViewModel : UserProfileViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    val loading = MutableLiveData<Boolean>()
    val status = MutableLiveData<SearchUsersStatus>()
    val filteredUserProfiles = MutableLiveData<ArrayList<UserProfile>>()

    private var phrase = ""

    fun findFriends(phrase: String) {
        loading.value = true
        this.phrase = phrase
        getAllUsers()
    }

    override fun onAllUsersChanged() {
        val filtered = ArrayList<UserProfileData>()

        if (allUsers.value != null) {
            for (item in allUsers.value!!) {
                if (containsPhrase(item, phrase) && item.userId != currentUser) {
                    filtered.add(item)
                }
            }
        }

        if (filtered.isNotEmpty()) {
            allUsers.value = filtered
            userProfilesArray.clear()
            getProfilePictureUrls()

        } else {
            userProfiles.value?.clear()
            onUserProfilesChanged()
        }
    }

    override fun onUserProfilesChanged() {
        filteredUserProfiles.value = userProfiles.value
        loading.value = false

        if (filteredUserProfiles.value == null || filteredUserProfiles.value?.isEmpty() == true) {
            status.value = SearchUsersStatus.NO_RESULTS
        } else {
            status.value = SearchUsersStatus.SUCCESS
        }
    }

    private fun containsPhrase(item: UserProfileData, phrase: String): Boolean {
        return (item.username?.contains(phrase, true) == true || item.realName?.contains(phrase, true) == true)
    }

}


enum class SearchUsersStatus {
    SUCCESS, NO_RESULTS, ERROR
}
