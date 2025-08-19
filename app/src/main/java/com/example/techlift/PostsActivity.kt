package com.example.techlift

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techlift.data.MockDataProvider
import com.example.techlift.model.Post
import com.example.techlift.ui.adapter.PostAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PostsActivity : AppCompatActivity() {

    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var createPostFab: FloatingActionButton
    private var userId: String? = null
    
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

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
        if (userId != null) {
            // Load posts for specific user
            loadUserPosts(userId!!)
        } else {
            // Load all community posts
            loadAllPosts()
        }
    }
    
    private fun loadUserPosts(userId: String) {
        firestore.collection("posts")
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val posts = querySnapshot.documents.mapNotNull { document ->
                    Post(
                        id = document.id,
                        userId = document.getString("userId") ?: "",
                        title = document.getString("title") ?: "",
                        content = document.getString("content") ?: "",
                        imageUrl = document.getString("imageUrl") ?: "",
                        authorName = document.getString("authorName") ?: "",
                        authorPhotoUrl = document.getString("authorPhotoUrl") ?: "",
                        createdAt = document.getTimestamp("createdAt")?.toDate() ?: java.util.Date(),
                        likes = document.getLong("likes")?.toInt() ?: 0,
                        comments = document.getLong("comments")?.toInt() ?: 0
                    )
                }
                postAdapter.updatePosts(posts)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "שגיאה בטעינת הפוסטים: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    
    private fun loadAllPosts() {
        firestore.collection("posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val remotePosts = querySnapshot.documents.mapNotNull { document ->
                    Post(
                        id = document.id,
                        userId = document.getString("userId") ?: "",
                        title = document.getString("title") ?: "",
                        content = document.getString("content") ?: "",
                        imageUrl = document.getString("imageUrl") ?: "",
                        authorName = document.getString("authorName") ?: "",
                        authorPhotoUrl = document.getString("authorPhotoUrl") ?: "",
                        createdAt = document.getTimestamp("createdAt")?.toDate() ?: java.util.Date(),
                        likes = document.getLong("likes")?.toInt() ?: 0,
                        comments = document.getLong("comments")?.toInt() ?: 0
                    )
                }
                // Merge Firestore posts with mock posts, dedupe by id, sort by date desc
                val merged = (remotePosts + MockDataProvider.mockPosts)
                    .associateBy { it.id }
                    .values
                    .sortedByDescending { it.createdAt }
                postAdapter.updatePosts(merged)
            }
            .addOnFailureListener { e ->
                // Fallback to mock posts only
                val fallback = MockDataProvider.mockPosts.sortedByDescending { it.createdAt }
                postAdapter.updatePosts(fallback)
                Toast.makeText(this, "שגיאה בטעינת הפוסטים: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    
    override fun onResume() {
        super.onResume()
        // Reload posts when returning from CreatePostActivity
        loadPosts()
    }
}