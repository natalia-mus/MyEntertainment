package com.example.myentertainment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottomNavigation_bar)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainActivity_frameLayout, MoviesFragment())
            commit()
        }

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_movies -> changeCurrentFragment(MoviesFragment())
                R.id.item_books -> changeCurrentFragment(BooksFragment())
                R.id.item_games -> changeCurrentFragment(GamesFragment())
                R.id.item_music -> changeCurrentFragment(MusicFragment())
            }
            true
        }
    }


    private fun changeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainActivity_frameLayout, fragment)
            commit()
        }
    }


}