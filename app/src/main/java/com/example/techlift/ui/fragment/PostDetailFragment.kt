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
import com.example.techlift.databinding.FragmentPostDetailBinding
import com.example.techlift.model.Post
import com.example.techlift.viewmodel.AuthViewModel
import com.example.techlift.viewmodel.PostViewModel
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class PostDetailFragment : Fragment() {
    
    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!
    
    private val postViewModel: PostViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var postId: String
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Get post ID from arguments
        arguments?.let {
            postId = it.getString("postId", "")
            if (postId.isEmpty()) {
                Toast.makeText(requireContext(), "Invalid post ID", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                return
            }
        }
        
        // Load post details
        loadPostDetails()
        
        // Set up delete button click listener
        binding.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
        
        // Set up edit button click listener
        binding.editButton.setOnClickListener {
            navigateToEditPost()
        }
        
        // Observe post deletion state
        observeDeletePostState()
    }
    
    private fun loadPostDetails() {
        postViewModel.getPostById(postId).observe(viewLifecycleOwner) { post ->
            post?.let {
                displayPostDetails(it)
                checkPostOwnership(it)
            } ?: run {
                // Post not found
                Toast.makeText(requireContext(), "הפוסט לא נמצא", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }
    
    private fun displayPostDetails(post: Post) {
        binding.postTitleText.text = post.title
        binding.postContentText.text = post.content
        binding.authorNameText.text = post.authorName
        
        // Format date
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        binding.postDateText.text = dateFormat.format(post.createdAt)
        
        // Load author profile image
        if (post.authorPhotoUrl.isNotEmpty()) {
            Glide.with(requireContext())
                .load(post.authorPhotoUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .circleCrop()
                .into(binding.authorImageView)
        } else {
            binding.authorImageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }
        
        // Load post image if exists
        if (post.imageUrl.isNotEmpty()) {
            binding.postImageView.visibility = View.VISIBLE
            Glide.with(requireContext())
                .load(post.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(binding.postImageView)
        } else {
            binding.postImageView.visibility = View.GONE
        }
    }
    
    private fun checkPostOwnership(post: Post) {
        // Observe current user to check post ownership
        authViewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            val isOwner = currentUser != null && currentUser.uid == post.userId
            
            // Show edit and delete buttons only to the post owner
            binding.editButton.visibility = if (isOwner) View.VISIBLE else View.GONE
            binding.deleteButton.visibility = if (isOwner) View.VISIBLE else View.GONE
        }
    }
    
    private fun showDeleteConfirmationDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete)
            .setMessage(R.string.confirm_delete)
            .setPositiveButton(R.string.yes) { _, _ ->
                deletePost()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
    
    private fun deletePost() {
        postViewModel.deletePost(postId)
    }
    
    private fun navigateToEditPost() {
        // Navigate to edit post fragment with post ID
        val bundle = Bundle().apply {
            putString("postId", postId)
        }
        findNavController().navigate(R.id.editPostFragment, bundle)
    }
    
    private fun observeDeletePostState() {
        postViewModel.deletePostState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PostViewModel.DeletePostState.Loading -> {
                    // Show loading state
                    binding.progressBar.visibility = View.VISIBLE
                }
                is PostViewModel.DeletePostState.Success -> {
                    // Hide loading state
                    binding.progressBar.visibility = View.GONE
                    
                    // Show success message
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.post_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
                    
                    // Navigate back
                    findNavController().popBackStack()
                }
                is PostViewModel.DeletePostState.Error -> {
                    // Hide loading state
                    binding.progressBar.visibility = View.GONE
                    
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