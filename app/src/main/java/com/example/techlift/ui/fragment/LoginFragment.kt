package com.example.techlift.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.techlift.R
import com.example.techlift.databinding.FragmentLoginBinding
import com.example.techlift.viewmodel.AuthViewModel

class LoginFragment : Fragment() {
    
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    
    private val authViewModel: AuthViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Set up click listeners
        setupClickListeners()
        
        // Observe login state
        observeLoginState()
    }
    
    private fun setupClickListeners() {
        // Login button click
        binding.loginButton.setOnClickListener {
            validateAndLogin()
        }
        
        // Register link click
        binding.registerLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
    
    private fun validateAndLogin() {
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()
        
        // Validate email
        if (email.isEmpty()) {
            binding.emailInputLayout.error = getString(R.string.error_email_required)
            return
        } else {
            binding.emailInputLayout.error = null
        }
        
        // Validate password
        if (password.isEmpty()) {
            binding.passwordInputLayout.error = getString(R.string.error_password_required)
            return
        } else {
            binding.passwordInputLayout.error = null
        }
        
        // Attempt login
        authViewModel.login(email, password)
    }
    
    private fun observeLoginState() {
        authViewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthViewModel.LoginState.Loading -> {
                    // Show loading state
                    binding.loginProgress.visibility = View.VISIBLE
                    binding.loginButton.isEnabled = false
                }
                is AuthViewModel.LoginState.Success -> {
                    // Hide loading state
                    binding.loginProgress.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    
                    // Navigate to home screen
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                is AuthViewModel.LoginState.Error -> {
                    // Hide loading state
                    binding.loginProgress.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    
                    // Show error message
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 