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
        loading.value = false

        if (filtered.isEmpty()) {
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
