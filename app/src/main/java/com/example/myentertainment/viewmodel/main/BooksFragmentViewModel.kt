package com.example.myentertainment.viewmodel.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class BooksFragmentViewModel : ViewModel() {

    private val path: DatabaseReference
    private val user: String

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        path = entertainmentReference.child(user).child(CategoryObject.BOOKS)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("entertainmentReference")
    lateinit var entertainmentReference: DatabaseReference

    val books = MutableLiveData<List<Book>>()
    val itemDeleted = MutableLiveData<Boolean>(false)


    fun deleteBook(id: String?) {
        itemDeleted.value = false
        path.child(id.toString()).removeValue()
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    fetchBooks()
                    itemDeleted.value = true
                } else {
                    Log.e("BooksFragmentViewModel", "error")
                }
            }
    }

    fun fetchBooks() {
        val result = ArrayList<Book>()

        path.get().addOnSuccessListener {
            it.children.forEach {
                val child = it.getValue(Book::class.java)
                child?.let { book -> result.add(book) }
            }

            books.value = result
        }
    }

}