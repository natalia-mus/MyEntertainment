package com.example.myentertainment.viewmodel.authentication

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


//@RunWith(AndroidJUnit4::class)
class SignUpFragmentViewModelTest {

    //val context = ApplicationProvider.getApplicationContext<Context>()
    val viewModel = SignUpFragmentViewModel()

    @Test
    fun signUpPasswordTooShort() {
        viewModel.signUp("username", "username@example.com", "pass", "pass")
    }
}