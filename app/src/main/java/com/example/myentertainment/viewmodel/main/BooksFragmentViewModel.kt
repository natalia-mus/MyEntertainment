package com.example.myentertainment.viewmodel.main

import androidx.lifecycle.MutableLiveData
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Book

class BooksFragmentViewModel : EntertainmentViewModel(CategoryObject.BOOKS) {

    val books = MutableLiveData<List<Book>>()
    val itemDeleted = MutableLiveData<Boolean>(false)


    fun deleteBook(id: String?) {
        itemDeleted.value = false
//        path.child(id.toString()).removeValue()
//            .addOnCompleteListener() { task ->
//                if (task.isSuccessful) {
//                    fetchBooks()
//                    itemDeleted.value = true
//                } else {
//                    Log.e("BooksFragmentViewModel", "error")
//                }
//            }
    }

    fun fetchBooks() {
//        val result = ArrayList<IEntertainment>()
//
//        path.get().addOnSuccessListener {
//            it.children.forEach {
//                val child = it.getValue(Book::class.java)
//                child?.let { book -> result.add(book) }
//            }
//
//            //books.value = orderByCreationDate(result) as ArrayList<Book>
//        }

        fetchItems()
    }

    override fun onItemsValueChanged() {
        books.value = items.value as ArrayList<Book>
    }

}