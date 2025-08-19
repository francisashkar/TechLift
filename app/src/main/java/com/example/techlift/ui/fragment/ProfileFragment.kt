package com.example.techlift.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.techlift.EditProfileActivity
import com.example.techlift.LoginActivity
import com.example.techlift.R
import com.example.techlift.util.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var profileImage: CircleImageView
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var specializationTextView: TextView
    private lateinit var experienceTextView: TextView
    private lateinit var editProfileButton: Button
    private lateinit var logoutButton: Button
    private lateinit var progressView: View
    
    private val auth = FirebaseAuth.getInstance()
    private val firebaseHelper = FirebaseHelper.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        
        // Initialize views
        profileImage = view.findViewById(R.id.profileImage)
        nameTextView = view.findViewById(R.id.nameTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        specializationTextView = view.findViewById(R.id.specializationTextView)
        experienceTextView = view.findViewById(R.id.experienceTextView)
        editProfileButton = view.findViewById(R.id.editProfileButton)
        logoutButton = view.findViewById(R.id.logoutButton)
        progressView = view.findViewById(R.id.progressView)
        
        // Set up click listeners
        setupClickListeners()
        
        return view
    }
    
    override fun onResume() {
        super.onResume()
        // Reload user data every time fragment becomes visible
        loadUserData()
    }
    
    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Show basic info immediately
            nameTextView.text = currentUser.displayName ?: "משתמש"
            emailTextView.text = currentUser.email ?: "אין אימייל"
            
            // Load profile image if available
            currentUser.photoUrl?.let { photoUrl ->
                Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.ic_profile)
                    .into(profileImage)
            }
            
            // Show progress while loading additional data from Firestore
            showProgress(true)
            
            // Load detailed user data from Firestore
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val userDataResult = firebaseHelper.getUserData(currentUser.uid)
                    
                    userDataResult.fold(
                        onSuccess = { userData ->
                            // Update UI with user data
                            nameTextView.text = userData.displayName
                            specializationTextView.text = userData.specialization.ifEmpty { "לא הוגדר" }
                            experienceTextView.text = userData.experience.ifEmpty { "לא הוגדר" }
                            
                            // Load profile image if available and not already loaded from Auth
                            if (userData.photoUrl.isNotEmpty() && currentUser.photoUrl == null) {
                                Glide.with(requireContext())
                                    .load(userData.photoUrl)
                                    .placeholder(R.drawable.ic_profile)
                                    .into(profileImage)
                            }
                            
                            // Hide progress when data is loaded successfully
                            showProgress(false)
                        },
                        onFailure = { exception ->
                            // Show error message and hide progress
                            showProgress(false)
                            showToast("שגיאה בטעינת נתוני משתמש: ${exception.message}")
                        }
                    )
                } catch (e: Exception) {
                    // Handle any unexpected errors and hide progress
                    showProgress(false)
                    showToast("שגיאה בטעינת נתונים: ${e.message}")
                }
            }
        }
    }
    
    private fun setupClickListeners() {
        editProfileButton.setOnClickListener {
            // Navigate to Edit Profile Activity
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
        
        logoutButton.setOnClickListener {
            // Sign out from Firebase
            auth.signOut()
            
            // Navigate to login screen
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
    
    private fun showProgress(show: Boolean) {
        progressView?.visibility = if (show) View.VISIBLE else View.GONE
    }
    
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
} 