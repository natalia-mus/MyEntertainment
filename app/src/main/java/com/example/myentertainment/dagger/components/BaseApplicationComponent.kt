package com.example.myentertainment.dagger.components

import android.app.Application
import com.example.myentertainment.dagger.modules.FirebaseModule
import com.example.myentertainment.viewmodel.add.AddBookFragmentViewModel
import com.example.myentertainment.viewmodel.add.AddGameFragmentViewModel
import com.example.myentertainment.viewmodel.add.AddMovieFragmentViewModel
import com.example.myentertainment.viewmodel.add.AddMusicFragmentViewModel
import com.example.myentertainment.viewmodel.authentication.SignInFragmentViewModel
import com.example.myentertainment.viewmodel.authentication.SignUpFragmentViewModel
import com.example.myentertainment.viewmodel.main.*
import com.example.myentertainment.viewmodel.userprofile.UserProfileActivityViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseModule::class])
interface BaseApplicationComponent {

    fun inject(baseApplication: Application)
    fun inject(viewModel: SignInFragmentViewModel)
    fun inject(viewModel: SignUpFragmentViewModel)
    fun inject(viewModel: UserProfileActivityViewModel)
    fun inject(viewModel: MainActivityViewModel)
    fun inject(viewModel: AddMovieFragmentViewModel)
    fun inject(viewModel: MoviesFragmentViewModel)
    fun inject(viewModel: AddBookFragmentViewModel)
    fun inject(viewModel: BooksFragmentViewModel)
    fun inject(viewModel: AddGameFragmentViewModel)
    fun inject(viewModel: GamesFragmentViewModel)
    fun inject(viewModel: AddMusicFragmentViewModel)
    fun inject(viewModel: MusicFragmentViewModel)
}