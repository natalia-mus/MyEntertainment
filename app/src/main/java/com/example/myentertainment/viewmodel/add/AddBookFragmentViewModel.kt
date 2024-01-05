package com.example.myentertainment.viewmodel.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.data.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class AddBookFragmentViewModel : ViewModel() {

    private val user: String
    private val path: DatabaseReference

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        path = entertainmentReference.child(user).child(CategoryObject.BOOKS.categoryName)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("entertainmentReference")
    lateinit var entertainmentReference: DatabaseReference

    val loading = MutableLiveData<Boolean>()
    val book = MutableLiveData<Book>()
    val validationResult = MutableLiveData<ValidationResult>()
    val addingToDatabaseResult = MutableLiveData<Boolean>()


    fun addToDatabase(item: Book) {
        loading.value = true

        val itemId = UUID.randomUUID().toString()
        val title = item.title
        val author = item.author
        val releaseYear = item.releaseYear
        val genre = item.genre
        val rating = item.rating

        val book = Book(itemId, title, author, releaseYear, genre, rating)

        if (validation(book)) {
            path.child(itemId).setValue(book).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loading.value = false
                    addingToDatabaseResult.value = true
                } else {
                    loading.value = false
                    addingToDatabaseResult.value = false
                }
            }
        }
    }

    fun getBook(id: String) {
        path.get().addOnSuccessListener {
            book.value = it.child(id).getValue(Book::class.java)
        }
    }

    fun updateItem(item: Book) {
        loading.value = true

        if (validation(item)) {
            val book = hashMapOf<String, Any>(item.id.toString() to item)
            path.updateChildren(book).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loading.value = false
                    addingToDatabaseResult.value = true
                } else {
                    loading.value = false
                    addingToDatabaseResult.value = false
                }
            }
        }
    }

    private fun validation(book: Book): Boolean {
        return if (book.title.isNullOrEmpty()) {
            loading.value = false
            validationResult.value = ValidationResult.EMPTY_VALUES
            false

        } else true
    }

}