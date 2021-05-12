package com.example.myentertainment.view.main

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
import com.example.myentertainment.R
import com.example.myentertainment.data.Book
import com.example.myentertainment.view.main.adapters.BooksAdapter
import com.example.myentertainment.viewmodel.BooksFragmentViewModel

class BooksFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var viewModel: BooksFragmentViewModel

    private lateinit var booksList: RecyclerView
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var noBooksLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_books, container, false)
        viewModel = ViewModelProvider(this).get(BooksFragmentViewModel::class.java)
        setView()
        setObservers()
        viewModel.fetchBooks()
        return fragmentView
    }

    private fun setView() {
        booksList = fragmentView.findViewById(R.id.books_list)
        loadingSection = fragmentView.findViewById(R.id.books_loadingSection)
        noBooksLabel = fragmentView.findViewById(R.id.books_noBooksLabel)
    }

    private fun setObservers() {
        viewModel.books.observe(this, { updateView(it) })
    }

    private fun updateView(books: List<Book>) {
        if (books.isEmpty()) {
            noBooksLabel.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.INVISIBLE
            booksList.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            booksList.adapter = BooksAdapter(requireContext(), books)
        }
    }

}