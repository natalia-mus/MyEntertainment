package com.example.myentertainment.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.UserProfile
import com.example.myentertainment.data.UserProfileData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class FriendsViewModel : UserProfileViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    @Named("friendsReference")
    lateinit var friendsReference: DatabaseReference

    val loading = MutableLiveData<Boolean>()
    val status = MutableLiveData<SearchUsersStatus>()
    val friendsListChanged = MutableLiveData<Boolean>()

    private var filterFriendsList = false
    private var friends = MutableLiveData<ArrayList<UserProfile>>()
    private var phrase = ""

    /**
     * Should setFriendsListener() set listener - only once, when setFriendsListener() first time executed
     */
    private var setFriendsListener = true


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

        val friendsIds = ArrayList<String?>()

        friendsReference.child(userId).get().addOnSuccessListener {
            it.children.forEach {
                val child = it.getValue(String::class.java)
                child?.let { id -> friendsIds.add(id) }
            }

            if (friendsIds.isNotEmpty()) {
                getUserProfiles(friendsIds)
            } else {
                loading.value = false
                status.value = SearchUsersStatus.NO_FRIENDS
            }

        }.addOnFailureListener {
            loading.value = false
            status.value = SearchUsersStatus.ERROR

        }
    }

    override fun onAllUsersChanged() {
        filter(true)
    }

    override fun onFetchingDataError() {
        loading.value = false
        status.value = SearchUsersStatus.ERROR
    }

    fun onFriendsListRefreshed() {
        friendsListChanged.value = false
        filterFriendsList = true
    }

    override fun onUserProfilesChanged() {
        if (filterFriendsList) {
            // filter by recently given phrase when refreshing friends list
            filterFriendsList = false
            filter(false)

        } else {
            loading.value = false

            if (userProfiles.value == null || userProfiles.value?.isEmpty() == true) {
                status.value = SearchUsersStatus.NO_RESULTS
            } else {
                status.value = SearchUsersStatus.SUCCESS
            }
        }
    }

    /**
     * Listens if user removed a friend or if another user accepted his invitation -
     * - if so, list of friends needs to be refreshed when back to My Friends
     */
    fun setFriendsListener(userId: String?) {
        if (userId == currentUser && setFriendsListener) {
            setFriendsListener = false

            friendsReference.child(currentUser).addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    friendsListChanged.value = true
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    friendsListChanged.value = true
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun containsPhrase(item: UserProfileData, phrase: String): Boolean {
        return (item.username?.contains(phrase, true) == true || item.realName?.contains(phrase, true) == true)
    }

    private fun filter(filterAllUsers: Boolean) {
        if (filterAllUsers && userProfilesData.value != null) {
            val filtered = ArrayList<UserProfileData>()
            for (item in userProfilesData.value!!) {
                if (item.userId != currentUser && phrase.isNotEmpty() && containsPhrase(item, phrase)) {
                    filtered.add(item)
                }
            }

            if (filtered.isNotEmpty()) {
                userProfilesData.value = filtered
                userProfilesArray.clear()
                getProfilePictureUrls()

            } else {
                userProfiles.value?.clear()
                onUserProfilesChanged()
            }


        } else {
            if (friends.value == null || friends.value?.size == 0) {
                friends.value = userProfiles.value
            }

            val filtered = ArrayList<UserProfile>()
            if (friends.value != null) {
                for (item in friends.value!!) {
                    if (item.userProfileData != null && containsPhrase(item.userProfileData, phrase)) {
                        filtered.add(item)
                    }
                }
            }

            if (filtered.isNotEmpty()) {
                userProfiles.value = filtered
                onUserProfilesChanged()

            } else {
                userProfiles.value = filtered
                onUserProfilesChanged()
            }
        }

    }

}


enum class SearchUsersStatus {
    SUCCESS, NO_RESULTS, NO_FRIENDS, ERROR
}
