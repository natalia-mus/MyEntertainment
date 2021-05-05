package com.example.myentertainment.dagger.modules

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("users")
    }
}