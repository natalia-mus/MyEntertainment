package com.example.myentertainment.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.UserProfile
import com.example.myentertainment.data.UserProfileData
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class FindFriendsViewModel : UserProfileViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    @Named("friendsReference")
    lateinit var friendsReference: DatabaseReference

    val loading = MutableLiveData<Boolean>()
    val status = MutableLiveData<SearchUsersStatus>()

    private var friends = MutableLiveData<ArrayList<UserProfile>>()

    private var phrase = ""

    fun findFriends(phrase: String, userId: String? = null) {
        loading.value = true
        this.phrase = phrase

        if (userId == null) {
            getAllUsers()
        } else {
            filter(false)
        }
    }

    fun getFriends(userId: String) {
        loading.value = true

        friendsReference.child(userId).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                val friendsMap = task.result?.value as HashMap<String, String>

                val friendsIds = ArrayList<String?>()
                for (id in friendsMap.values) {
                    friendsIds.add(id)
                }

                getUserProfiles(friendsIds)
            }
        }
    }

    override fun onAllUsersChanged() {
        filter(true)
    }

    override fun onUserProfilesChanged() {
        loading.value = false

        if (userProfiles.value == null || userProfiles.value?.isEmpty() == true) {
            status.value = SearchUsersStatus.NO_RESULTS
        } else {
            status.value = SearchUsersStatus.SUCCESS
        }
    }

    private fun containsPhrase(item: UserProfileData, phrase: String): Boolean {
        return (item.username?.contains(phrase, true) == true || item.realName?.contains(phrase, true) == true)
    }

    private fun filter(filterAllUsers: Boolean) {
        if (filterAllUsers && allUsers.value != null) {
            val filtered = ArrayList<UserProfileData>()
            for (item in allUsers.value!!) {
                if (containsPhrase(item, phrase) && item.userId != currentUser) {
                    filtered.add(item)
                }
            }

            if (filtered.isNotEmpty()) {
                allUsers.value = filtered
                userProfilesArray.clear()
                getProfilePictureUrls()

            } else {
                onUserProfilesChanged()
            }


        } else {
            if (friends.value == null) {
                friends.value = userProfiles.value
            }

            val filtered = ArrayList<UserProfile>()
            for (item in friends.value!!) {
                if (item.userProfileData != null && containsPhrase(item.userProfileData, phrase)) {
                    filtered.add(item)
                }
            }

            if (filtered.isNotEmpty()) {
                userProfiles.value = filtered
                onUserProfilesChanged()

            } else {
                userProfiles.value?.clear()
                onUserProfilesChanged()
            }
        }

    }

}


enum class SearchUsersStatus {
    SUCCESS, NO_RESULTS, ERROR
}
