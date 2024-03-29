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

    companion object {
        private const val UNLIMITED = "unlimited"
        private const val RESTRICTED = "restricted"
    }

    @Singleton
    @Provides
    @Named("aboutReference")
    fun provideAboutReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference(UNLIMITED).child("about")
    }

    @Singleton
    @Provides
    @Named("entertainmentReference")
    fun provideEntertainmentReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference(UNLIMITED).child("entertainment")
    }

    @Singleton
    @Provides
    @Named("friendsReference")
    fun provideFriendsReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference(UNLIMITED).child("friends")
    }

    @Singleton
    @Provides
    @Named("invitationsReference")
    fun provideInvitationsReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference(UNLIMITED).child("invitations")
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    fun provideFirebaseStorageReference(): StorageReference {
        return Firebase.storage.reference
    }

    @Singleton
    @Provides
    @Named("rawReference")
    fun provideRawReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }

    @Singleton
    @Provides
    @Named("reportsReference")
    fun provideReportsReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference(RESTRICTED).child("reports")
    }

    @Singleton
    @Provides
    @Named("usersReference")
    fun provideUsersReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference(UNLIMITED).child("users")
    }

}