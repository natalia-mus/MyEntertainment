package com.example.myentertainment.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.Date
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
    val resultUserProfiles = MutableLiveData<ArrayList<UserProfile>>()

    private var phrase = ""

    fun findFriends(phrase: String) {
        loading.value = true
        this.phrase = phrase
        getAllUsers()
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
        resultUserProfiles.value = userProfiles.value
        loading.value = false

        if (resultUserProfiles.value == null || resultUserProfiles.value?.isEmpty() == true) {
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
