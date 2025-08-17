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
import com.example.techlift.LoginActivity
import com.example.techlift.R
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private lateinit var profileImage: CircleImageView
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var specializationTextView: TextView
    private lateinit var experienceTextView: TextView
    private lateinit var editProfileButton: Button
    private lateinit var logoutButton: Button
    
    private val auth = FirebaseAuth.getInstance()

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
        
        // Set up user information
        setupUserInfo()
        
        // Set up click listeners
        setupClickListeners()
        
        return view
    }
    
    private fun setupUserInfo() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            nameTextView.text = currentUser.displayName ?: "User"
            emailTextView.text = currentUser.email ?: "No email"
            
            // In a real app, we would fetch these from the user's profile in Firestore
            specializationTextView.text = "Software Development"
            experienceTextView.text = "Beginner"
        }
    }
    
    private fun setupClickListeners() {
        editProfileButton.setOnClickListener {
            showToast("Edit profile feature coming soon!")
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
    
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
} 