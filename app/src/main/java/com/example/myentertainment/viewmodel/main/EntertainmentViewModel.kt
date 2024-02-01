package com.example.myentertainment.viewmodel.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

open class EntertainmentViewModel : ViewModel() {

    private lateinit var itemType: Class<out IEntertainment>
    private lateinit var path: DatabaseReference
    private val user: String

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("entertainmentReference")
    lateinit var entertainmentReference: DatabaseReference


    val entertainmentList = MutableLiveData<List<IEntertainment>>()
    val fetchingItemsStatus = MutableLiveData<Boolean>()
    val itemDeleted = MutableLiveData<Boolean>(false)

    companion object {
        fun create(category: CategoryObject, owner: ViewModelStoreOwner): EntertainmentViewModel {
            val instance = ViewModelProvider(owner).get(EntertainmentViewModel::class.java)
            instance.setCategory(category)
            return instance
        }
    }

    open fun onItemsValueChanged() {}

    fun deleteItem(id: String?) {
        itemDeleted.value = false
        path.child(id.toString())
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fetchItems()
                    itemDeleted.value = true
                } else {
                    Log.e("EntertainmentViewModel", "error")
                }
            }
    }

    fun fetchItems() {
        fetchingItemsStatus.value = true
        val result = ArrayList<IEntertainment>()

        path.get().addOnSuccessListener {
            it.children.forEach {
                val child = it.getValue(itemType)
                child?.let { item -> result.add(item) }
            }

            entertainmentList.value = orderByCreationDate(result)
            onItemsValueChanged()

        }.addOnFailureListener {
            fetchingItemsStatus.value = false
        }
    }

    private fun getItemType(category: CategoryObject): Class<out IEntertainment> {
        return when (category) {
            CategoryObject.MOVIES -> Movie::class.java
            CategoryObject.BOOKS -> Book::class.java
            CategoryObject.GAMES -> Game::class.java
            CategoryObject.MUSIC -> Music::class.java
        }
    }

    private fun orderByCreationDate(array: ArrayList<IEntertainment>): ArrayList<IEntertainment> {
        array.sortBy { it.creationDate }
        return array
    }

    private fun setCategory(category: CategoryObject) {
        path = entertainmentReference.child(user).child(category.categoryName)
        itemType = getItemType(category)
    }
}