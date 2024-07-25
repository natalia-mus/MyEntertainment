package com.example.myentertainment.view.authentication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.view.AboutActivity
import com.example.myentertainment.view.ProblemReportActivity

class AuthenticationActivity : AppCompatActivity(), OnSignUpClickAction {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host)

        if (intent.getBooleanExtra(Constants.CHANGE_PASSWORD, false)) {
            changeCurrentFragment(ChangePasswordFragment(), false)
        } else {
            changeCurrentFragment(SignInFragment(this), false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        menu?.let { hideOptions(it) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItem_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.menuItem_reportAProblem -> {
                val intent = Intent(this, ProblemReportActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun signUpClicked() {
        changeCurrentFragment(SignUpFragment(), true)
    }

    private fun changeCurrentFragment(fragment: Fragment, addToBackstack: Boolean) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.host_fragment, fragment)

            if (addToBackstack) {
                addToBackStack("AUTHENTICATION_FRAGMENT")
            }

            commit()
        }
    }

    private fun hideOptions(menu: Menu) {
        menu.findItem(R.id.menuItem_userProfile).isVisible = false
        menu.findItem(R.id.menuItem_signOut).isVisible = false
        menu.findItem(R.id.menuItem_myFriends).isVisible = false
        menu.findItem(R.id.menuItem_findFriends).isVisible = false
    }

}