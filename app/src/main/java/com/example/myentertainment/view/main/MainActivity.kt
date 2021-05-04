package com.example.myentertainment.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myentertainment.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private val moviesFragment = MoviesFragment()
    private val booksFragment = BooksFragment()
    private val gamesFragment = GamesFragment()
    private val musicFragment = MusicFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottomNavigation_bar)

        changeCurrentFragment(moviesFragment)

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_movies -> changeCurrentFragment(moviesFragment)
                R.id.item_books -> changeCurrentFragment(booksFragment)
                R.id.item_games -> changeCurrentFragment(gamesFragment)
                R.id.item_music -> changeCurrentFragment(musicFragment)
            }
            true
        }
    }

    private fun changeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainActivity_fragment, fragment)
            commit()
        }
    }

}