package com.example.techlift.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.techlift.R
import com.example.techlift.databinding.FragmentCreatePostBinding
import com.example.techlift.viewmodel.PostViewModel

class CreatePostFragment : Fragment() {
    
    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!
    
    private val postViewModel: PostViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    
    // Image picker launcher
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            selectedImageUri = result.data?.data
            binding.postImageView.setImageURI(selectedImageUri)
            binding.postImageView.visibility = View.VISIBLE
            binding.removeImageButton.visibility = View.VISIBLE
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Set up click listeners
        setupClickListeners()
        
        // Observe post creation state
        observeCreatePostState()
    }
    
    private fun setupClickListeners() {
        // Add image button
        binding.addImageButton.setOnClickListener {
            openImagePicker()
        }
        
        // Remove image button
        binding.removeImageButton.setOnClickListener {
            selectedImageUri = null
            binding.postImageView.setImageURI(null)
            binding.postImageView.visibility = View.GONE
            binding.removeImageButton.visibility = View.GONE
        }
        
        // Publish button
        binding.publishButton.setOnClickListener {
            validateAndCreatePost()
        }
    }
    
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }
    
    private fun validateAndCreatePost() {
        val title = binding.postTitleInput.text.toString().trim()
        val content = binding.postContentInput.text.toString().trim()
        
        // Validate title
        if (title.isEmpty()) {
            binding.postTitleLayout.error = "יש להזין כותרת"
            return
        } else {
            binding.postTitleLayout.error = null
        }
        
        // Validate content
        if (content.isEmpty()) {
            binding.postContentLayout.error = "יש להזין תוכן"
            return
        } else {
            binding.postContentLayout.error = null
        }
        
        // Create post
        postViewModel.createPost(title, content, selectedImageUri)
    }
    
    private fun observeCreatePostState() {
        postViewModel.createPostState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PostViewModel.CreatePostState.Loading -> {
                    // Show loading state
                    binding.progressBar.visibility = View.VISIBLE
                    binding.publishButton.isEnabled = false
                }
                is PostViewModel.CreatePostState.Success -> {
                    // Hide loading state
                    binding.progressBar.visibility = View.GONE
                    binding.publishButton.isEnabled = true
                    
                    // Show success message
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.post_created),
                        Toast.LENGTH_SHORT
                    ).show()
                    
                    // Navigate back
                    findNavController().popBackStack()
                }
                is PostViewModel.CreatePostState.Error -> {
                    // Hide loading state
                    binding.progressBar.visibility = View.GONE
                    binding.publishButton.isEnabled = true
                    
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