package com.example.techlift.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.techlift.CreatePostActivity
import com.example.techlift.R
import com.example.techlift.model.Post
import com.example.techlift.ui.adapter.PostAdapter
import com.example.techlift.viewmodel.PostViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PostsFragment : Fragment(), PostAdapter.OnPostClickListener {
    
    private val postViewModel: PostViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter
    
    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postsSwipeRefresh: SwipeRefreshLayout
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var postsProgressBar: ProgressBar
    private lateinit var createPostFab: FloatingActionButton
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize views
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView)
        postsSwipeRefresh = view.findViewById(R.id.postsSwipeRefresh)
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout)
        postsProgressBar = view.findViewById(R.id.postsProgressBar)
        createPostFab = view.findViewById(R.id.createPostFab)
        
        // Set up RecyclerView
        setupRecyclerView()
        
        // Set up FAB click listener
        createPostFab.setOnClickListener {
            val intent = Intent(requireContext(), CreatePostActivity::class.java)
            startActivity(intent)
        }
        
        // Observe posts data
        observePosts()
        
        // Observe loading state
        observeLoadingState()
        
        // Refresh posts
        refreshPosts()
    }
    
    private fun setupRecyclerView() {
        postAdapter = PostAdapter(emptyList(), this)
        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        postsRecyclerView.adapter = postAdapter
    }
    
    private fun observePosts() {
        postViewModel.getAllPosts().observe(viewLifecycleOwner) { posts ->
            if (posts.isNullOrEmpty()) {
                emptyStateLayout.visibility = View.VISIBLE
                postsRecyclerView.visibility = View.GONE
            } else {
                emptyStateLayout.visibility = View.GONE
                postsRecyclerView.visibility = View.VISIBLE
                postAdapter.updatePosts(posts)
            }
        }
    }
    
    private fun observeLoadingState() {
        postViewModel.postsLoadingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PostViewModel.PostsLoadingState.Loading -> {
                    postsProgressBar.visibility = View.VISIBLE
                }
                is PostViewModel.PostsLoadingState.Success -> {
                    postsProgressBar.visibility = View.GONE
                    postsSwipeRefresh.isRefreshing = false
                }
                is PostViewModel.PostsLoadingState.Error -> {
                    postsProgressBar.visibility = View.GONE
                    postsSwipeRefresh.isRefreshing = false
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    postsProgressBar.visibility = View.GONE
                    postsSwipeRefresh.isRefreshing = false
                }
            }
        }
    }
    
    private fun refreshPosts() {
        postsSwipeRefresh.setOnRefreshListener {
            postViewModel.loadPosts()
        }
        
        // Initial load
        postViewModel.loadPosts()
    }
    
    override fun onPostClick(post: Post) {
        // Navigate to post detail with bundle
        val bundle = Bundle().apply {
            putString("postId", post.id)
        }
        findNavController().navigate(R.id.action_postsFragment_to_postDetailFragment, bundle)
    }
} 