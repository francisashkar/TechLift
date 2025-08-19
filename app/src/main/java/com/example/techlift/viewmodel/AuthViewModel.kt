package com.example.techlift.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.techlift.data.local.AppDatabase
import com.example.techlift.data.repository.UserRepository
import com.example.techlift.model.User
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userRepository: UserRepository
    
    // LiveData for login state
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState
    
    // LiveData for registration state
    private val _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState> = _registerState
    
    // LiveData for current user
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser
    
    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
        
        // Check if user is already logged in
        _currentUser.value = userRepository.getCurrentUser()
    }
    
    // Login user
    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading
        
        viewModelScope.launch {
            try {
                val result = userRepository.loginUser(email, password)
                
                result.fold(
                    onSuccess = { user ->
                        _currentUser.value = user
                        _loginState.value = LoginState.Success
                    },
                    onFailure = { exception ->
                        _loginState.value = LoginState.Error(exception.message ?: "התחברות נכשלה")
                    }
                )
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "שגיאה בהתחברות")
            }
        }
    }
    
    // Register user
    fun register(email: String, password: String, displayName: String) {
        _registerState.value = RegisterState.Loading
        
        viewModelScope.launch {
            try {
                val result = userRepository.registerUser(email, password, displayName)
                
                result.fold(
                    onSuccess = { user ->
                        _currentUser.value = user
                        _registerState.value = RegisterState.Success
                    },
                    onFailure = { exception ->
                        _registerState.value = RegisterState.Error(exception.message ?: "הרשמה נכשלה")
                    }
                )
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "שגיאה בהרשמה")
            }
        }
    }
    
    // Logout user
    fun logout() {
        viewModelScope.launch {
            userRepository.logoutUser()
            _currentUser.value = null
        }
    }
    
    // Check if user is logged in
    fun isLoggedIn(): Boolean {
        return userRepository.getCurrentUser() != null
    }
    
    // Login state sealed class
    sealed class LoginState {
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }
    
    // Register state sealed class
    sealed class RegisterState {
        object Loading : RegisterState()
        object Success : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
} 