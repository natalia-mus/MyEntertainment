package com.example.myentertainment.dagger.components

import android.app.Application
import com.example.myentertainment.DatabaseRebuilder
import com.example.myentertainment.dagger.modules.FirebaseModule
import com.example.myentertainment.viewmodel.*
import com.example.myentertainment.viewmodel.add.AddBookFragmentViewModel
import com.example.myentertainment.viewmodel.add.AddGameFragmentViewModel
import com.example.myentertainment.viewmodel.add.AddMovieFragmentViewModel
import com.example.myentertainment.viewmodel.add.AddMusicFragmentViewModel
import com.example.myentertainment.viewmodel.authentication.ChangePasswordViewModel
import com.example.myentertainment.viewmodel.authentication.SignInFragmentViewModel
import com.example.myentertainment.viewmodel.authentication.SignUpFragmentViewModel
import com.example.myentertainment.viewmodel.main.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseModule::class])
interface BaseApplicationComponent {

    fun inject(baseApplication: Application)
    fun inject(viewModel: SignInFragmentViewModel)
    fun inject(viewModel: SignUpFragmentViewModel)
    fun inject(viewModel: ChangePasswordViewModel)
    fun inject(viewModel: UserProfileActivityViewModel)
    fun inject(viewModel: MainActivityViewModel)
    fun inject(viewModel: AddMovieFragmentViewModel)
    fun inject(viewModel: EntertainmentViewModel)
    fun inject(viewModel: AddBookFragmentViewModel)
    fun inject(viewModel: AddGameFragmentViewModel)
    fun inject(viewModel: AddMusicFragmentViewModel)
    fun inject(viewModel: ProblemReportViewModel)
    fun inject(viewModel: AboutViewModel)
    fun inject(viewModel: FriendsViewModel)
    fun inject(viewModel: UserProfileViewModel)

    fun inject(databaseRebuilder: DatabaseRebuilder)
}