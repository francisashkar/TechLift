package com.example.techlift

import android.app.Application
import com.example.techlift.util.FirebaseHelper
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class TechLiftApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Initialize FirebaseHelper
        FirebaseHelper.getInstance()
        
        // Configure Firestore settings
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)  // Enable offline persistence
            .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings
    }
} 