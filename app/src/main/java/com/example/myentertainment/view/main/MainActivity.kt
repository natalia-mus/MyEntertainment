package com.example.myentertainment.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Invitation
import com.example.myentertainment.data.UserProfile
import com.example.myentertainment.view.AboutActivity
import com.example.myentertainment.view.ProblemReportActivity
import com.example.myentertainment.view.UserProfileActivity
import com.example.myentertainment.view.add.AddActivity
import com.example.myentertainment.view.authentication.AuthenticationActivity
import com.example.myentertainment.view.friends.FriendsActivity
import com.example.myentertainment.viewmodel.main.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), InvitationDialogListener {

    private val moviesFragment by lazy { EntertainmentFragment(CategoryObject.MOVIES) }
    private val booksFragment by lazy { EntertainmentFragment(CategoryObject.BOOKS) }
    private val gamesFragment by lazy { EntertainmentFragment(CategoryObject.GAMES) }
    private val musicFragment by lazy { EntertainmentFragment(CategoryObject.MUSIC) }

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var currentCategory: CategoryObject
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var addButton: FloatingActionButton

    private var invitationDialog: InvitationDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        initView()
        setObservers()
        checkCategory()
        getInvitations()
    }

    override fun onAcceptClick(invitation: Invitation) {
        viewModel.acceptInvitation(invitation)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onDeclineClick(invitationId: String) {
        viewModel.declineInvitation(invitationId)
    }

    override fun onLaterClick() {
        invitationDialog?.dismiss()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItem_userProfile -> {
                val intent = Intent(this, UserProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.menuItem_signOut -> {
                viewModel.signOut()
                val intent = Intent(this, AuthenticationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.menuItem_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.menuItem_myFriends -> {
                val intent = Intent(this, FriendsActivity::class.java)
                intent.putExtra(Constants.USER_ID, viewModel.currentUser)
                startActivity(intent)
            }
            R.id.menuItem_findFriends -> {
                val intent = Intent(this, FriendsActivity::class.java)
                startActivity(intent)
            }
            R.id.menuItem_reportAProblem -> {
                val intent = Intent(this, ProblemReportActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun changeCurrentFragment(fragment: EntertainmentFragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainActivity_fragment, fragment)
            commit()
        }

        currentCategory = fragment.category
        when (currentCategory) {
            CategoryObject.MOVIES -> {
                bottomNavigation.menu.getItem(0).isChecked = true
            }
            CategoryObject.BOOKS -> {
                bottomNavigation.menu.getItem(1).isChecked = true
            }
            CategoryObject.GAMES -> {
                bottomNavigation.menu.getItem(2).isChecked = true
            }
            CategoryObject.MUSIC -> {
                bottomNavigation.menu.getItem(3).isChecked = true
            }
        }
    }

    private fun checkCategory() {
        if (intent.hasExtra(Constants.CATEGORY)) {
            val categoryName = intent.getStringExtra(Constants.CATEGORY)
            val category = CategoryObject.getCategoryByName(categoryName) ?: CategoryObject.MOVIES
            when (category) {
                CategoryObject.MOVIES -> changeCurrentFragment(moviesFragment)
                CategoryObject.BOOKS -> changeCurrentFragment(booksFragment)
                CategoryObject.GAMES -> changeCurrentFragment(gamesFragment)
                CategoryObject.MUSIC -> changeCurrentFragment(musicFragment)
            }
        } else {
            changeCurrentFragment(moviesFragment)
        }
    }

    private fun getInvitations() {
        viewModel.getInvitations()
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
            intent.putExtra(Constants.CATEGORY, currentCategory.categoryName)
            startActivity(intent)
        }
    }

    private fun setObservers() {
        viewModel.userProfiles.observe(this) { showInvitations(it) }
    }

    private fun showInvitations(invitingUsers: ArrayList<UserProfile>) {
        val invitations = viewModel.invitations.value
        if (invitations != null) {
            invitationDialog = InvitationDialog(this, invitations, invitingUsers, this)
            invitationDialog!!.show()
        }
    }

}