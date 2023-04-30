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

    private val mainPath: DatabaseReference
    private val user: String

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        mainPath = entertainmentReference.child(user).child(CategoryObject.BOOKS)
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
        mainPath.child(id.toString()).removeValue()
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    fetchBooks()
                    itemDeleted.value = true
                } else {
                    Log.e("error", "error")
                }
            }
    }

    fun fetchBooks() {
        mainPath.get().addOnSuccessListener {
            val booksList: MutableList<Book> = mutableListOf()
            var lastChild = it.childrenCount

            var i = 0
            while (i < lastChild) {
                if (!it.child(i.toString()).exists()) {
                    lastChild++
                }
                i++
            }

            for (j in 0 until lastChild) {
                val singleBook = it.child(j.toString()).getValue(Book::class.java)
                singleBook?.let { book -> booksList.add(book) }
            }

            books.value = booksList
        }
    }

}