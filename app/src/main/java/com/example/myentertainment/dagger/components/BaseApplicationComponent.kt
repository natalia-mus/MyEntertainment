package com.example.myentertainment.dagger.components

import android.app.Application
import com.example.myentertainment.dagger.modules.FirebaseModule
import com.example.myentertainment.viewmodel.AddMovieFragmentViewModel
import com.example.myentertainment.viewmodel.MainActivityViewModel
import com.example.myentertainment.viewmodel.SignInFragmentViewModel
import com.example.myentertainment.viewmodel.SignUpFragmentViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseModule::class])
interface BaseApplicationComponent {

    fun inject(baseApplication: Application)
    fun inject(viewModel: SignInFragmentViewModel)
    fun inject(viewModel: SignUpFragmentViewModel)
    fun inject(viewModel: MainActivityViewModel)
    fun inject(viewModel: AddMovieFragmentViewModel)
}