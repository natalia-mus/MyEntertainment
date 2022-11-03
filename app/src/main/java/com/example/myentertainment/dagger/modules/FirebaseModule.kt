package com.example.myentertainment.dagger.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    @Named("rawReference")
    fun provideRawReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }

    @Singleton
    @Provides
    @Named("usersReference")
    fun provideUsersReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("users")
    }

    @Singleton
    @Provides
    @Named("reportsReference")
    fun provideReportsReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("reports")
    }

    @Singleton
    @Provides
    fun provideFirebaseStorageReference(): StorageReference {
        return Firebase.storage.reference
    }

}