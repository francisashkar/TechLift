package com.example.techlift.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.techlift.data.local.dao.UserDao
import com.example.techlift.data.local.entity.UserEntity
import com.example.techlift.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.UUID

class UserRepository(
    private val userDao: UserDao,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    // Get current user from Firebase Auth
    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            displayName = firebaseUser.displayName ?: "",
            photoUrl = firebaseUser.photoUrl?.toString() ?: ""
        )
    }
    
    // Get user by ID from local database
    fun getUserById(userId: String): LiveData<User?> {
        return userDao.getUserById(userId).map { it?.toUser() }
    }
    
    // Register a new user
    suspend fun registerUser(email: String, password: String, displayName: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                // Create user in Firebase Auth
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user ?: throw Exception("User creation failed")
                
                // Update display name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
                
                firebaseUser.updateProfile(profileUpdates).await()
                
                // Create user object
                val user = User(
                    uid = firebaseUser.uid,
                    email = email,
                    displayName = displayName,
                    createdAt = Date(),
                    lastLoginAt = Date()
                )
                
                // Save to Firestore
                firestore.collection(User.COLLECTION)
                    .document(user.uid)
                    .set(user)
                    .await()
                
                // Save to local database
                userDao.insertUser(UserEntity.fromUser(user))
                
                Result.success(user)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    // Login user
    suspend fun loginUser(email: String, password: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                // Sign in with Firebase Auth
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user ?: throw Exception("Login failed")
                
                // Get user data from Firestore
                val userDoc = firestore.collection(User.COLLECTION)
                    .document(firebaseUser.uid)
                    .get()
                    .await()
                
                val user = if (userDoc.exists()) {
                    userDoc.toObject(User::class.java)
                        ?: User(
                            uid = firebaseUser.uid,
                            email = firebaseUser.email ?: "",
                            displayName = firebaseUser.displayName ?: ""
                        )
                } else {
                    // Create user document if it doesn't exist
                    val newUser = User(
                        uid = firebaseUser.uid,
                        email = firebaseUser.email ?: "",
                        displayName = firebaseUser.displayName ?: "",
                        createdAt = Date(),
                        lastLoginAt = Date()
                    )
                    
                    firestore.collection(User.COLLECTION)
                        .document(newUser.uid)
                        .set(newUser)
                        .await()
                    
                    newUser
                }
                
                // Update last login time
                val updatedUser = user.copy(lastLoginAt = Date())
                firestore.collection(User.COLLECTION)
                    .document(updatedUser.uid)
                    .update("lastLoginAt", updatedUser.lastLoginAt)
                    .await()
                
                // Save to local database
                userDao.insertUser(UserEntity.fromUser(updatedUser))
                
                Result.success(updatedUser)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    // Update user profile
    suspend fun updateUserProfile(displayName: String, specialization: String, experience: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val currentUser = getCurrentUser() ?: throw Exception("User not logged in")
                
                // Update display name in Firebase Auth
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
                
                auth.currentUser?.updateProfile(profileUpdates)?.await()
                
                // Get current user data
                val userDoc = firestore.collection(User.COLLECTION)
                    .document(currentUser.uid)
                    .get()
                    .await()
                
                val user = userDoc.toObject(User::class.java) ?: currentUser
                
                // Update user data
                val updatedUser = user.copy(
                    displayName = displayName,
                    specialization = specialization,
                    experience = experience
                )
                
                // Save to Firestore
                firestore.collection(User.COLLECTION)
                    .document(updatedUser.uid)
                    .set(updatedUser)
                    .await()
                
                // Save to local database
                userDao.insertUser(UserEntity.fromUser(updatedUser))
                
                Result.success(updatedUser)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    // Upload profile image
    suspend fun uploadProfileImage(imageUri: Uri): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val currentUser = getCurrentUser() ?: throw Exception("User not logged in")
                
                // Create storage reference
                val filename = UUID.randomUUID().toString()
                val storageRef = storage.reference.child("profile_images/${currentUser.uid}/$filename")
                
                // Upload file
                val uploadTask = storageRef.putFile(imageUri).await()
                val downloadUrl = storageRef.downloadUrl.await().toString()
                
                // Update profile image URL in Firebase Auth
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(downloadUrl))
                    .build()
                
                auth.currentUser?.updateProfile(profileUpdates)?.await()
                
                // Update in Firestore
                firestore.collection(User.COLLECTION)
                    .document(currentUser.uid)
                    .update("photoUrl", downloadUrl)
                    .await()
                
                // Update in local database
                val userEntity = userDao.getUserByIdSync(currentUser.uid)
                userEntity?.let {
                    val updatedEntity = it.copy(photoUrl = downloadUrl)
                    userDao.updateUser(updatedEntity)
                }
                
                Result.success(downloadUrl)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    // Logout user
    suspend fun logoutUser() {
        withContext(Dispatchers.IO) {
            try {
                val currentUser = getCurrentUser()
                
                // Sign out from Firebase Auth
                auth.signOut()
                
                // Clear local user data
                currentUser?.let {
                    userDao.deleteUser(it.uid)
                }
            } catch (e: Exception) {
                // Handle logout error
                e.printStackTrace()
            }
        }
    }
} 