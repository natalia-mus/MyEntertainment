package com.example.myentertainment.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class BooksFragmentViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    lateinit var databaseReference: DatabaseReference

    private val user = databaseAuth.uid.toString()

    val books = MutableLiveData<List<Book>>()


    fun fetchBooks() {
        databaseReference.child(user).child(CategoryObject.BOOKS).get().addOnSuccessListener {
            val countItems = it.childrenCount
            val booksList: MutableList<Book> = mutableListOf()

            for (i in 0 until countItems) {
                val singleBook = it.child(i.toString()).getValue(Book::class.java)
                singleBook?.let { book -> booksList.add(book) }
            }
            books.value = booksList
        }
    }
}