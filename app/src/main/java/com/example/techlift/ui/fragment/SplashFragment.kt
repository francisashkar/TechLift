package com.example.techlift.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.techlift.R
import com.example.techlift.viewmodel.AuthViewModel

class SplashFragment : Fragment() {
    
    private val authViewModel: AuthViewModel by viewModels()
    private val SPLASH_DURATION = 2500L
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Delay navigation to allow splash screen to be visible
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, SPLASH_DURATION)
    }
    
    private fun navigateToNextScreen() {
        if (authViewModel.isLoggedIn()) {
            // User is logged in, navigate to home screen
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        } else {
            // User is not logged in, navigate to login screen
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }
} 