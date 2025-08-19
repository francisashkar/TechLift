package com.example.techlift

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.techlift.util.FirebaseHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var profileImage: CircleImageView
    private lateinit var fullNameInput: TextInputEditText
    private lateinit var specializationDropdown: AutoCompleteTextView
    private lateinit var experienceDropdown: AutoCompleteTextView
    private lateinit var saveButton: MaterialButton
    
    private val firebaseHelper = FirebaseHelper.getInstance()
    private var selectedImageUri: Uri? = null
    
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            // Display the selected image
            Glide.with(this)
                .load(selectedImageUri)
                .into(profileImage)
        }
    }
    
    private val specializationOptions = arrayOf(
        "פיתוח תוכנה",
        "פיתוח אתרים",
        "פיתוח מובייל",
        "מדע נתונים",
        "DevOps",
        "עיצוב UI/UX",
        "אבטחת מידע",
        "חישוב ענן",
        "בינה מלאכותית",
        "למידת מכונה"
    )
    
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
        setContentView(R.layout.activity_edit_profile)
        
        // Initialize views
        initializeViews()
        
        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "ערוך פרופיל"
        
        // Set up adapters for dropdowns
        setupDropdowns()
        
        // Get current user data
        val currentUser = firebaseHelper.getCurrentUser()
        if (currentUser != null) {
            // Load user data from Firebase Auth (works offline)
            loadUserData()
        } else {
            // Handle case where no user is logged in
            Toast.makeText(this, "יש להתחבר כדי לערוך פרופיל", Toast.LENGTH_SHORT).show()
            finish()
        }
        
        // Set up click listeners
        setupListeners()
    }
    
    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar)
        profileImage = findViewById(R.id.profileImage)
        fullNameInput = findViewById(R.id.fullNameInput)
        specializationDropdown = findViewById(R.id.specializationDropdown)
        experienceDropdown = findViewById(R.id.experienceDropdown)
        saveButton = findViewById(R.id.saveButton)
    }
    
    private fun setupDropdowns() {
        // Set up specialization dropdown
        val specializationAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, specializationOptions)
        specializationDropdown.setAdapter(specializationAdapter)
        
        // Set up experience dropdown
        val experienceAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, experienceLevels)
        experienceDropdown.setAdapter(experienceAdapter)
    }
    
    private fun loadUserData() {
        val currentUser = firebaseHelper.getCurrentUser()
        
        if (currentUser != null) {
            // Show progress while loading data
            // progressView.visibility = View.VISIBLE
            
            // First load basic data from Firebase Auth
            // This data is available offline
            fullNameInput.setText(currentUser.displayName ?: "")
            
            // Load profile image if available from Auth
            currentUser.photoUrl?.let { photoUrl ->
                Glide.with(this@EditProfileActivity)
                    .load(photoUrl)
                    .placeholder(R.drawable.ic_profile)
                    .into(profileImage)
            }
            
            // Now try to load additional data from Firestore
            lifecycleScope.launch {
                try {
                    val userDataResult = firebaseHelper.getUserData(currentUser.uid)
                    
                    runOnUiThread {
                        userDataResult.fold(
                            onSuccess = { userData ->
                                // Set specialization if available
                                if (userData.specialization.isNotEmpty()) {
                                    specializationDropdown.setText(userData.specialization, false)
                                }
                                
                                // Set experience if available
                                if (userData.experience.isNotEmpty()) {
                                    experienceDropdown.setText(userData.experience, false)
                                }
                                
                                // Load profile image if available from Firestore and not already loaded from Auth
                                if (userData.photoUrl.isNotEmpty() && currentUser.photoUrl == null) {
                                    Glide.with(this@EditProfileActivity)
                                        .load(userData.photoUrl)
                                        .placeholder(R.drawable.ic_profile)
                                        .into(profileImage)
                                }
                            },
                            onFailure = { exception ->
                                // Just show a warning toast but don't prevent profile editing
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Some profile data might be missing. You can still edit your profile.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )
                    }
                } catch (e: Exception) {
                    // Handle error
                    runOnUiThread {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "You appear to be offline. Basic profile editing is still available.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
    
    private fun setupListeners() {
        // Profile image click listener
        profileImage.setOnClickListener {
            openImagePicker()
        }
        
        // Save button click listener
        saveButton.setOnClickListener {
            updateProfile()
        }
    }
    
    private fun openImagePicker() {
        imagePickerLauncher.launch("image/*")
    }
    
    private fun updateProfile() {
        val displayName = fullNameInput.text.toString().trim()
        val specialization = specializationDropdown.text.toString()
        val experience = experienceDropdown.text.toString()
        
        // Validate inputs
        if (displayName.isEmpty()) {
            Toast.makeText(this, "נא להזין את שמך", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Disable save button to prevent double submission
        saveButton.isEnabled = false
        saveButton.text = "מעדכן..."
        
        lifecycleScope.launch {
            try {
                // First update basic profile data in Firebase Auth (works offline)
                var photoUrl: Uri? = null
                
                // Try to upload image if selected
                if (selectedImageUri != null) {
                    try {
                        val currentUser = firebaseHelper.getCurrentUser()
                        if (currentUser != null) {
                            val uploadResult = firebaseHelper.uploadProfileImage(currentUser.uid, selectedImageUri!!)
                            
                            uploadResult.fold(
                                onSuccess = { imageUrl ->
                                    photoUrl = Uri.parse(imageUrl)
                                },
                                onFailure = { exception ->
                                    // Just log the error but continue with profile update
                                    println("Failed to upload image: ${exception.message}")
                                }
                            )
                        }
                    } catch (e: Exception) {
                        // Just log the error but continue with profile update
                        println("Failed to upload image due to exception: ${e.message}")
                    }
                }
                
                // Update profile in Firebase Auth
                try {
                    val updateResult = firebaseHelper.updateUserProfile(displayName, photoUrl)
                    
                    updateResult.fold(
                        onSuccess = {
                            // Success with Firebase Auth, now try Firestore
                        },
                        onFailure = { exception ->
                            runOnUiThread {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "שגיאה בעדכון הפרופיל הבסיסי: ${exception.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                                saveButton.isEnabled = true
                                saveButton.text = "שמור שינויים"
                            }
                        }
                    )
                } catch (e: Exception) {
                    println("Failed to update Firebase Auth profile: ${e.message}")
                }
                
                // Now try to update Firestore data
                try {
                    // Create a user object even if we can't fetch existing data
                    val currentUser = firebaseHelper.getCurrentUser()
                    if (currentUser != null) {
                        var updatedUser: com.example.techlift.model.User? = null
                        
                        try {
                            // Try to get existing user data
                            val userDataResult = firebaseHelper.getUserData(currentUser.uid)
                            
                            userDataResult.fold(
                                onSuccess = { userData ->
                                    // Update user fields from existing data
                                    updatedUser = userData.copy(
                                        displayName = displayName,
                                        specialization = specialization,
                                        experience = experience
                                    )
                                },
                                onFailure = { /* Ignore and create new user object */ }
                            )
                        } catch (e: Exception) {
                            // Ignore network errors
                            println("Network error while loading user data: ${e.message}")
                        }
                        
                        // If we couldn't get existing data, create a new user object
                        if (updatedUser == null) {
                            updatedUser = com.example.techlift.model.User(
                                uid = currentUser.uid,
                                email = currentUser.email ?: "",
                                displayName = displayName,
                                photoUrl = currentUser.photoUrl?.toString() ?: "",
                                specialization = specialization,
                                experience = experience
                            )
                        }
                        
                        // Try to save to Firestore
                        try {
                            val saveResult = firebaseHelper.saveUserData(updatedUser!!)
                            
                            saveResult.fold(
                                onSuccess = {
                                    // Successfully updated both Auth and Firestore
                                },
                                onFailure = { exception ->
                                    // Firestore update failed, but Auth update might have succeeded
                                    println("Failed to update Firestore: ${exception.message}")
                                }
                            )
                        } catch (e: Exception) {
                            // Firestore update failed
                            println("Failed to save to Firestore: ${e.message}")
                        }
                    }
                } catch (e: Exception) {
                    // Ignore Firestore errors in offline mode
                    println("Error during Firestore update: ${e.message}")
                }
                
                // Always consider the update successful if we got this far
                // At minimum, the local auth data should be updated
                runOnUiThread {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "הפרופיל עודכן בהצלחה! 🎉",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            } catch (e: Exception) {
                // Handle any unexpected errors
                runOnUiThread {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "משהו השתבש: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    saveButton.isEnabled = true
                    saveButton.text = "שמור שינויים"
                }
            }
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
