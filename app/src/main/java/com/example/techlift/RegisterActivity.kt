package com.example.techlift

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.techlift.model.User
import com.example.techlift.util.FirebaseHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.util.Date
import android.os.Handler
import android.os.Looper

class RegisterActivity : AppCompatActivity() {

    // UI components
    private lateinit var toolbar: Toolbar
    private lateinit var fullNameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var phoneInput: TextInputEditText
    private lateinit var experienceDropdown: AutoCompleteTextView
    private lateinit var registerButton: MaterialButton
    private lateinit var progressView: View

    // Firebase Helper
    private val firebaseHelper = FirebaseHelper.getInstance()

    private val experienceLevels = arrayOf(
        "אין ניסיון קודם",
        "למידה עצמית / קורסים",
        "פרויקטים אישיים",
        "ניסיון עבודה עד שנה",
        "1-3 שנות ניסיון",
        "3+ שנות ניסיון"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
        // Initialize components
        initializeViews()
        
        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Set up event listeners
        setupListeners()
        
        // Set up adapters for dropdowns
        setupDropdowns()
    }

    /**
     * Initialize UI component references
     */
    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar)
        fullNameInput = findViewById(R.id.fullNameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        phoneInput = findViewById(R.id.phoneInput)
        experienceDropdown = findViewById(R.id.experienceDropdown)
        registerButton = findViewById(R.id.registerButton)
        progressView = findViewById(R.id.progressView)
    }

    /**
     * Set up event listeners
     */
    private fun setupListeners() {
        // Register button click
        registerButton.setOnClickListener {
            if (validateForm()) {
                registerUser()
            }
        }
    }

    /**
     * Set up adapters for dropdown menus
     */
    private fun setupDropdowns() {
        // Adapter for experience levels
        val experienceAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, experienceLevels)
        experienceDropdown.setAdapter(experienceAdapter)
    }

    /**
     * Validate form inputs
     * @return Whether the form is valid
     */
    private fun validateForm(): Boolean {
        var isValid = true
        
        // Full name validation
        val fullName = fullNameInput.text.toString().trim()
        if (fullName.isEmpty()) {
            (fullNameInput.parent.parent as TextInputLayout).error = getString(R.string.error_name_required)
            isValid = false
        } else {
            (fullNameInput.parent.parent as TextInputLayout).error = null
        }
        
        // Email validation
        val email = emailInput.text.toString().trim()
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            (emailInput.parent.parent as TextInputLayout).error = getString(R.string.error_invalid_email)
            isValid = false
        } else {
            (emailInput.parent.parent as TextInputLayout).error = null
        }
        
        // Password validation
        val password = passwordInput.text.toString()
        if (password.isEmpty() || password.length < 6) {
            (passwordInput.parent.parent as TextInputLayout).error = getString(R.string.error_password_short)
            isValid = false
        } else {
            (passwordInput.parent.parent as TextInputLayout).error = null
        }
        
        // Experience validation
        if (experienceDropdown.text.toString().isEmpty()) {
            (experienceDropdown.parent.parent as TextInputLayout).error = "Please select an experience level"
            isValid = false
        } else {
            (experienceDropdown.parent.parent as TextInputLayout).error = null
        }
        
        return isValid
    }

    /**
     * Register user with Firebase
     */
    private fun registerUser() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()
        val fullName = fullNameInput.text.toString().trim()
        val experience = experienceDropdown.text.toString()
        
        try {
            // Register user with Firebase Auth
            lifecycleScope.launch {
                val registerResult = firebaseHelper.register(email, password, fullName)
                registerResult.fold(
                    onSuccess = { firebaseUser ->
                        // Save user data to Firestore
                        try {
                            val userData = User(
                                uid = firebaseUser.uid,
                                email = email,
                                displayName = fullName,
                                specialization = "General",
                                experience = experience,
                                createdAt = Date(),
                                lastLoginAt = Date()
                            )
                            firebaseHelper.saveUserData(userData)
                        } catch (e: Exception) {
                            // Continue even if Firestore save fails
                        }
                        
                        // Show success message
                        runOnUiThread {
                            Toast.makeText(this@RegisterActivity, "חשבון נוצר בהצלחה! 🎉", Toast.LENGTH_LONG).show()
                        }
                        
                        // Navigate to login after a short delay
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }, 2000) // 2 seconds delay
                    },
                    onFailure = { exception ->
                        runOnUiThread {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registration failed: ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )
            }
        } catch (e: Exception) {
            // Catch any unexpected exceptions
            runOnUiThread {
                Toast.makeText(
                    this@RegisterActivity,
                    "Unexpected error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    private fun showProgress(show: Boolean) {
        progressView.visibility = if (show) View.VISIBLE else View.GONE
        registerButton.isEnabled = !show
        // Disable all input fields when showing progress
        fullNameInput.isEnabled = !show
        emailInput.isEnabled = !show
        passwordInput.isEnabled = !show
        phoneInput.isEnabled = !show
        experienceDropdown.isEnabled = !show
    }
    
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 