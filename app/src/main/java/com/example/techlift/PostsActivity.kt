package com.example.techlift

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techlift.data.MockDataProvider
import com.example.techlift.ui.adapter.PostAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PostsActivity : AppCompatActivity() {

    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var createPostFab: FloatingActionButton
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        // Get user ID from intent if available
        userId = intent.getStringExtra("USER_ID")

        setupToolbar()
        setupRecyclerView()
        setupFab()
        loadPosts()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set title based on whether we're showing all posts or a specific user's posts
        if (userId != null) {
            val user = MockDataProvider.mockUsers.find { it.uid == userId }
            if (user != null) {
                supportActionBar?.title = "הפוסטים של ${user.displayName}"
            } else {
                supportActionBar?.title = "פוסטים"
            }
        } else {
            supportActionBar?.title = "פוסטים בקהילה"
        }
    }

    private fun setupRecyclerView() {
        postsRecyclerView = findViewById(R.id.postsRecyclerView)
        postsRecyclerView.layoutManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(emptyList())
        postsRecyclerView.adapter = postAdapter
    }
    
    private fun setupFab() {
        createPostFab = findViewById(R.id.createPostFab)
        createPostFab.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadPosts() {
        val posts = if (userId != null) {
            // Filter posts by user ID
            MockDataProvider.mockPosts.filter { it.userId == userId }
        } else {
            // Show all posts
            MockDataProvider.mockPosts
        }

        postAdapter.updatePosts(posts)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}