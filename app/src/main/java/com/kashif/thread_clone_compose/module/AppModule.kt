package com.kashif.thread_clone_compose.module

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by Mohammad Kashif Ansari on 19,April,2024
 */
@InstallIn(ViewModelComponent::class)
@Module
class AppModule {

    @Provides
    fun provideAuthentication():FirebaseAuth{
        return Firebase.auth
    }

    @Provides
    fun provideFirestore():FirebaseFirestore{
        return Firebase.firestore
    }

    @Provides
    fun provideFirebaseDatabase():FirebaseDatabase{
        return Firebase.database
    }

    @Provides
    fun provideStorage():FirebaseStorage{
        return Firebase.storage
    }
}