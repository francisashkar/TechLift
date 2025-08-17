package com.example.techlift.util

import android.net.Uri
import com.example.techlift.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

/**
 * Helper class for Firebase operations
 */
class FirebaseHelper {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    
    /**
     * Get the current authenticated user
     */
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
    
    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean = auth.currentUser != null
    
    /**
     * Sign in with email and password
     */
    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw Exception("Login failed")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Register a new user with email and password
     */
    suspend fun register(email: String, password: String, displayName: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw Exception("Registration failed")
            
            // Update display name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            
            user.updateProfile(profileUpdates).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Sign out the current user
     */
    fun signOut() {
        auth.signOut()
    }
    
    /**
     * Save user data to Firestore
     */
    suspend fun saveUserData(user: User): Result<Unit> {
        return try {
            firestore.collection(User.COLLECTION)
                .document(user.uid)
                .set(user)
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get user data from Firestore
     */
    suspend fun getUserData(userId: String): Result<User> {
        return try {
            val documentSnapshot = firestore.collection(User.COLLECTION)
                .document(userId)
                .get()
                .await()
            
            if (documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(User::class.java)
                    ?: throw Exception("Failed to parse user data")
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Upload profile image to Firebase Storage
     */
    suspend fun uploadProfileImage(userId: String, imageUri: Uri): Result<String> {
        return try {
            val filename = UUID.randomUUID().toString()
            val ref = storage.reference.child("users/$userId/profile/$filename")
            
            val uploadTask = ref.putFile(imageUri).await()
            val downloadUrl = ref.downloadUrl.await().toString()
            
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Update user profile
     */
    suspend fun updateUserProfile(
        displayName: String? = null,
        photoUrl: Uri? = null
    ): Result<Unit> {
        return try {
            val user = auth.currentUser ?: throw Exception("User not logged in")
            
            val profileUpdates = UserProfileChangeRequest.Builder().apply {
                displayName?.let { setDisplayName(it) }
                photoUrl?.let { setPhotoUri(it) }
            }.build()
            
            user.updateProfile(profileUpdates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Send password reset email
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    companion object {
        @Volatile
        private var instance: FirebaseHelper? = null
        
        fun getInstance(): FirebaseHelper {
            return instance ?: synchronized(this) {
                instance ?: FirebaseHelper().also { instance = it }
            }
        }
    }
} 