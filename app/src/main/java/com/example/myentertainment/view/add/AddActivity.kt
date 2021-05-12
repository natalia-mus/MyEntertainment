package com.example.myentertainment.view.add

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject

class AddActivity : AppCompatActivity() {

    private var category: String? = ""
    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        category = intent.getStringExtra("category")
        setFragment(category!!)
        showFragment(fragment)
    }

    private fun setFragment(category: String) {
        when (category) {
            CategoryObject.MOVIES -> fragment = AddMovieFragment()
            CategoryObject.BOOKS -> fragment = AddBookFragment()
            CategoryObject.GAMES -> fragment = AddGameFragment()
            CategoryObject.MUSIC -> fragment = AddMusicFragment()
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.addActivity_fragment, fragment)
            commit()
        }
    }
}