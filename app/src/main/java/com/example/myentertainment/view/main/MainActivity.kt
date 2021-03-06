package com.example.myentertainment.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.view.add.AddActivity
import com.example.myentertainment.view.authentication.AuthenticationActivity
import com.example.myentertainment.viewmodel.main.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private val moviesFragment = MoviesFragment()
    private val booksFragment = BooksFragment()
    private val gamesFragment = GamesFragment()
    private val musicFragment = MusicFragment()

    private lateinit var currentFragment: String

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var addButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        initView()
        checkCategory()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItem_sign_out -> {
                viewModel.signOut()
                val intent = Intent(this, AuthenticationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        bottomNavigation = findViewById(R.id.bottomNavigation_bar)
        addButton = findViewById(R.id.mainActivity_add)

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_movies -> changeCurrentFragment(moviesFragment)
                R.id.item_books -> changeCurrentFragment(booksFragment)
                R.id.item_games -> changeCurrentFragment(gamesFragment)
                R.id.item_music -> changeCurrentFragment(musicFragment)
            }
            true
        }

        addButton.setOnClickListener() {
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra(Constants.CATEGORY, currentFragment)
            startActivity(intent)
        }
    }

    private fun checkCategory() {
        if (intent.hasExtra(Constants.CATEGORY)) {
            currentFragment = intent.getStringExtra(Constants.CATEGORY).toString()
            when (currentFragment) {
                CategoryObject.MOVIES -> changeCurrentFragment(moviesFragment)
                CategoryObject.BOOKS -> changeCurrentFragment(booksFragment)
                CategoryObject.GAMES -> changeCurrentFragment(gamesFragment)
                CategoryObject.MUSIC -> changeCurrentFragment(musicFragment)
            }
        } else {
            changeCurrentFragment(moviesFragment)
        }
    }

    private fun changeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainActivity_fragment, fragment)
            commit()
        }

        when (fragment) {
            is MoviesFragment -> {
                currentFragment = CategoryObject.MOVIES
                bottomNavigation.menu.getItem(0).setChecked(true)
            }
            is BooksFragment -> {
                currentFragment = CategoryObject.BOOKS
                bottomNavigation.menu.getItem(1).setChecked(true)
            }
            is GamesFragment -> {
                currentFragment = CategoryObject.GAMES
                bottomNavigation.menu.getItem(2).setChecked(true)
            }
            is MusicFragment -> {
                currentFragment = CategoryObject.MUSIC
                bottomNavigation.menu.getItem(3).setChecked(true)
            }
        }
    }

}