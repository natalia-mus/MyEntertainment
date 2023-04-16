package com.example.myentertainment.view.add

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject

class AddActivity : AppCompatActivity() {

    private var category: String? = ""
    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host)
        category = intent.getStringExtra(Constants.CATEGORY)
        category?.let { setFragment(it) }
        establishOpeningContext()
        showFragment(fragment)
    }

    private fun establishOpeningContext() {
        if (intent.hasExtra(Constants.ID)) {
            val id = intent.getStringExtra(Constants.ID)
            val bundle = Bundle()
            bundle.putString(Constants.ID, id)
            fragment.arguments = bundle
        }
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
            replace(R.id.host_fragment, fragment)
            commit()
        }
    }

}