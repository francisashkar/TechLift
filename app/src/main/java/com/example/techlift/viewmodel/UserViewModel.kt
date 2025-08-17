package com.example.techlift.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.techlift.data.local.AppDatabase
import com.example.techlift.data.repository.UserRepository
import com.example.techlift.model.User
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userRepository: UserRepository
    
    // LiveData for profile update state
    private val _profileUpdateState = MutableLiveData<ProfileUpdateState>()
    val profileUpdateState: LiveData<ProfileUpdateState> = _profileUpdateState
    
    // LiveData for profile image upload state
    private val _imageUploadState = MutableLiveData<ImageUploadState>()
    val imageUploadState: LiveData<ImageUploadState> = _imageUploadState
    
    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
    }
    
    // Get current user
    fun getCurrentUser(): User? {
        return userRepository.getCurrentUser()
    }
    
    // Get user by ID
    fun getUserById(userId: String): LiveData<User?> {
        return userRepository.getUserById(userId)
    }
    
    // Update user profile
    fun updateProfile(displayName: String, specialization: String, experience: String) {
        _profileUpdateState.value = ProfileUpdateState.Loading
        
        viewModelScope.launch {
            val result = userRepository.updateUserProfile(displayName, specialization, experience)
            
            result.fold(
                onSuccess = { user ->
                    _profileUpdateState.value = ProfileUpdateState.Success(user)
                },
                onFailure = { exception ->
                    _profileUpdateState.value = ProfileUpdateState.Error(exception.message ?: "Profile update failed")
                }
            )
        }
    }
    
    // Upload profile image
    fun uploadProfileImage(imageUri: Uri) {
        _imageUploadState.value = ImageUploadState.Loading
        
        viewModelScope.launch {
            val result = userRepository.uploadProfileImage(imageUri)
            
            result.fold(
                onSuccess = { imageUrl ->
                    _imageUploadState.value = ImageUploadState.Success(imageUrl)
                },
                onFailure = { exception ->
                    _imageUploadState.value = ImageUploadState.Error(exception.message ?: "Image upload failed")
                }
            )
        }
    }
    
    // Profile update state sealed class
    sealed class ProfileUpdateState {
        object Loading : ProfileUpdateState()
        data class Success(val user: User) : ProfileUpdateState()
        data class Error(val message: String) : ProfileUpdateState()
    }
    
    // Image upload state sealed class
    sealed class ImageUploadState {
        object Loading : ImageUploadState()
        data class Success(val imageUrl: String) : ImageUploadState()
        data class Error(val message: String) : ImageUploadState()
    }
} 