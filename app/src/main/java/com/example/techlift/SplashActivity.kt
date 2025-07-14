package com.example.techlift

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

/**
 * Splash screen with app logo as intro
 */
class SplashActivity : AppCompatActivity() {
    
    // Tag for logging
    private val TAG = "SplashActivity"
    
    // Splash duration
    private val SPLASH_DURATION = 2500L
    
    // Flag to prevent multiple navigations
    private var hasNavigated = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // Set content view - wrapped in try/catch to catch any inflation errors
            setContentView(R.layout.activity_splash)
            
            // Simple display for the logo
            try {
                val appLogoImage = findViewById<ImageView>(R.id.appLogoImage)
                appLogoImage?.let {
                    // Make sure the logo is visible
                    it.visibility = View.VISIBLE
                    it.alpha = 1f
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error displaying logo: ${e.message}")
            }
            
            // Schedule navigation to login screen
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToLogin()
            }, SPLASH_DURATION)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}")
            // If any error occurs in onCreate, navigate immediately
            navigateToLogin()
        }
    }
    
    /**
     * Navigate to the login screen
     */
    private fun navigateToLogin() {
        if (hasNavigated) {
            return
        }
        
        hasNavigated = true
        
        try {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e(TAG, "Error navigating to login: ${e.message}")
            finish()
        }
    }
    
    override fun onPause() {
        super.onPause()
        // Ensure we don't leave the splash screen hanging
        navigateToLogin()
    }
} 