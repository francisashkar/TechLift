package com.example.techlift.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Utility class to test Firebase connectivity
 */
object FirebaseTest {
    private const val TAG = "FirebaseTest"
    
    /**
     * Test Firebase connectivity and show results in a toast
     */
    suspend fun testFirebaseConnectivity(context: Context) {
        try {
            // Ensure Firebase is initialized
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            
            // Test Firebase services
            val authResult = testAuth()
            val firestoreResult = testFirestore()
            val storageResult = testStorage()
            
            // Show results
            val message = """
                Firebase Connectivity Test:
                - Auth: $authResult
                - Firestore: $firestoreResult
                - Storage: $storageResult
            """.trimIndent()
            
            Log.d(TAG, message)
            
            withContext(Dispatchers.Main) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Firebase test failed", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Firebase test failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    /**
     * Test Firebase Authentication
     */
    private suspend fun testAuth(): String {
        return try {
            val auth = FirebaseAuth.getInstance()
            val isInitialized = auth != null
            
            if (isInitialized) {
                "OK"
            } else {
                "Failed to initialize"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
    
    /**
     * Test Firestore
     */
    private suspend fun testFirestore(): String {
        return try {
            val firestore = FirebaseFirestore.getInstance()
            val isAvailable = withContext(Dispatchers.IO) {
                try {
                    // Try to access a collection
                    firestore.collection("test").limit(1).get().await()
                    true
                } catch (e: Exception) {
                    Log.e(TAG, "Firestore test failed", e)
                    false
                }
            }
            
            if (isAvailable) {
                "OK"
            } else {
                "Connection failed"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
    
    /**
     * Test Firebase Storage
     */
    private suspend fun testStorage(): String {
        return try {
            val storage = FirebaseStorage.getInstance()
            val isInitialized = storage != null
            
            if (isInitialized) {
                "OK"
            } else {
                "Failed to initialize"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
} 