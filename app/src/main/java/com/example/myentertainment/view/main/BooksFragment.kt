package com.example.myentertainment.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Book
import com.example.myentertainment.interfaces.OnItemClickAction
import com.example.myentertainment.view.add.AddActivity
import com.example.myentertainment.view.main.adapters.BooksAdapter
import com.example.myentertainment.viewmodel.main.BooksFragmentViewModel

class BooksFragment : Fragment(), OnItemClickAction {

    private lateinit var fragmentView: View
    private lateinit var viewModel: BooksFragmentViewModel

    private lateinit var booksList: RecyclerView
    private lateinit var booksAdapter: BooksAdapter
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var noBooksLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_books, container, false)
        viewModel = ViewModelProvider(this).get(BooksFragmentViewModel::class.java)
        initView()
        setObservers()
        viewModel.fetchBooks()
        return fragmentView
    }

    override fun onItemClicked(id: String?) {
        editBook(id)
    }

    override fun onItemLongClicked(id: String?) {
        viewModel.deleteBook(id)
    }

    private fun editBook(id: String?) {
        if (id != null) {
            val intent = Intent(activity, AddActivity::class.java)
            intent.putExtra(Constants.CATEGORY, CategoryObject.BOOKS.categoryName)
            intent.putExtra(Constants.ID, id)
            startActivity(intent)
        }
    }

    private fun initView() {
        booksList = fragmentView.findViewById(R.id.books_list)
        loadingSection = fragmentView.findViewById(R.id.books_loadingSection)
        noBooksLabel = fragmentView.findViewById(R.id.books_noBooksLabel)
    }

    private fun setObservers() {
        viewModel.books.observe(this) { updateView(it) }
    }

    private fun updateView(books: List<Book>) {
        loadingSection.visibility = View.GONE
        if (books.isEmpty()) {
            booksList.visibility = View.GONE
            noBooksLabel.visibility = View.VISIBLE
        } else {
            if (viewModel.itemDeleted.value == true) {
                booksAdapter.dataSetChanged(books)
            } else {
                booksList.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                booksAdapter = BooksAdapter(requireContext(), books, this)
                booksList.adapter = booksAdapter
            }
        }
    }

}