package com.example.myentertainment.view.add

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Book
import com.example.myentertainment.data.Game
import com.example.myentertainment.data.Movie
import com.example.myentertainment.data.Music

class AddActivity : AppCompatActivity() {

    private var category: CategoryObject? = null
    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host)
        val categoryName = intent.getStringExtra(Constants.CATEGORY)
        category = CategoryObject.getCategoryByName(categoryName)
        category?.let { setFragment(it) }
        establishOpeningContext()
        showFragment(fragment)
    }

    private fun establishOpeningContext() {
        if (intent.hasExtra(CategoryObject.MOVIES.categoryName)) {
            val item = intent.getParcelableExtra<Movie>(CategoryObject.MOVIES.categoryName)
            val bundle = Bundle()
            bundle.putParcelable(CategoryObject.MOVIES.categoryName, item)
            fragment.arguments = bundle

        } else if (intent.hasExtra(CategoryObject.BOOKS.categoryName)) {
            val item = intent.getParcelableExtra<Book>(CategoryObject.BOOKS.categoryName)
            val bundle = Bundle()
            bundle.putParcelable(CategoryObject.BOOKS.categoryName, item)
            fragment.arguments = bundle

        } else if (intent.hasExtra(CategoryObject.GAMES.categoryName)) {
            val item = intent.getParcelableExtra<Game>(CategoryObject.GAMES.categoryName)
            val bundle = Bundle()
            bundle.putParcelable(CategoryObject.GAMES.categoryName, item)
            fragment.arguments = bundle

        } else if (intent.hasExtra(CategoryObject.MUSIC.categoryName)) {
            val item = intent.getParcelableExtra<Music>(CategoryObject.MUSIC.categoryName)
            val bundle = Bundle()
            bundle.putParcelable(CategoryObject.MUSIC.categoryName, item)
            fragment.arguments = bundle
        }
    }

    private fun setFragment(category: CategoryObject) {
        fragment = when (category) {
            CategoryObject.MOVIES -> AddMovieFragment()
            CategoryObject.BOOKS -> AddBookFragment()
            CategoryObject.GAMES -> AddGameFragment()
            CategoryObject.MUSIC -> AddMusicFragment()
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.host_fragment, fragment)
            commit()
        }
    }

}