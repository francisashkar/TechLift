package com.example.techlift

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.widget.TextView
import android.widget.ImageView
import com.example.techlift.util.FirebaseHelper
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerLink: TextView
    private lateinit var forgotPasswordLink: TextView
    private lateinit var appLogoImage: ImageView
    private lateinit var progressView: View
    
    private val firebaseHelper = FirebaseHelper.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize components
        emailInputLayout = findViewById(R.id.emailInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        registerLink = findViewById(R.id.registerLink)
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink)
        appLogoImage = findViewById(R.id.appLogoImage)
        progressView = findViewById(R.id.progressView)

        // Check if user is already logged in
        if (firebaseHelper.isLoggedIn()) {
            navigateToMainActivity()
            return
        }

        // Set login button action
        loginButton.setOnClickListener {
            // Input validation
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            
            if (!validateInputs(email, password)) {
                return@setOnClickListener
            }
            
            // Show progress
            showProgress(true)
            
            // Attempt login with Firebase
            lifecycleScope.launch {
                val result = firebaseHelper.signIn(email, password)
                
                result.fold(
                    onSuccess = {
                        showProgress(false)
                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        navigateToMainActivity()
                    },
                    onFailure = { exception ->
                        showProgress(false)
                        Toast.makeText(this@LoginActivity, "Login failed: ${exception.message}", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }

        // Set register link action
        registerLink.setOnClickListener {
            // Navigate to registration screen
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        
        // Set forgot password link action
        forgotPasswordLink.setOnClickListener {
            val email = emailInput.text.toString().trim()
            
            if (email.isEmpty()) {
                emailInputLayout.error = "Please enter your email address"
                return@setOnClickListener
            }
            
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInputLayout.error = "Please enter a valid email address"
                return@setOnClickListener
            }
            
            showProgress(true)
            
            lifecycleScope.launch {
                val result = firebaseHelper.sendPasswordResetEmail(email)
                
                showProgress(false)
                
                result.fold(
                    onSuccess = {
                        Toast.makeText(
                            this@LoginActivity,
                            "Password reset email sent to $email",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    onFailure = { exception ->
                        Toast.makeText(
                            this@LoginActivity,
                            "Failed to send reset email: ${exception.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }
        }
    }
    
    /**
     * Validate login inputs
     */
    private fun validateInputs(email: String, password: String): Boolean {
        var isValid = true
        
        // Validate email
        if (email.isEmpty()) {
            emailInputLayout.error = "Email cannot be empty"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.error = "Please enter a valid email address"
            isValid = false
        } else {
            emailInputLayout.error = null
        }
        
        // Validate password
        if (password.isEmpty()) {
            passwordInputLayout.error = "Password cannot be empty"
            isValid = false
        } else if (password.length < 6) {
            passwordInputLayout.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            passwordInputLayout.error = null
        }
        
        return isValid
    }
    
    /**
     * Show or hide progress view
     */
    private fun showProgress(show: Boolean) {
        progressView.visibility = if (show) View.VISIBLE else View.GONE
        loginButton.isEnabled = !show
    }
    
    /**
     * Navigate to main activity
     */
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
} 