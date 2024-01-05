package com.example.myentertainment.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

open class EntertainmentViewModel(category: CategoryObject) : ViewModel() {

    private val itemType: Class<out IEntertainment>
    private val path: DatabaseReference
    private val user: String

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        path = entertainmentReference.child(user).child(category.categoryName)
        itemType = getItemType(category)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("entertainmentReference")
    lateinit var entertainmentReference: DatabaseReference

    protected val items = MutableLiveData<List<IEntertainment>>()

    open fun onItemsValueChanged() {}

    fun fetchItems() {
        val result = ArrayList<IEntertainment>()

        path.get().addOnSuccessListener {
            it.children.forEach {
                val child = it.getValue(itemType)
                child?.let { item -> result.add(item) }
            }

            items.value = orderByCreationDate(result)
            onItemsValueChanged()
        }
    }

    fun orderByCreationDate(array: ArrayList<IEntertainment>): ArrayList<IEntertainment> {
        array.sortBy { it.creationDate }
        return array
    }

    private fun getItemType(category: CategoryObject): Class<out IEntertainment> {
        return when (category) {
            CategoryObject.MOVIES -> Movie::class.java
            CategoryObject.BOOKS -> Book::class.java
            CategoryObject.GAMES -> Game::class.java
            CategoryObject.MUSIC -> Music::class.java
        }
    }
}