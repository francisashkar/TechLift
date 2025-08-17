package com.example.techlift

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.Date
import java.util.UUID

class CreatePostActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var postImageView: ImageView
    private lateinit var addImageButton: Button
    private lateinit var submitButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbar: Toolbar

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        // Initialize views
        initializeViews()
        
        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "יצירת פוסט חדש"
        
        // Set up click listeners
        setupClickListeners()
    }

    private fun initializeViews() {
        titleEditText = findViewById(R.id.postTitleEditText)
        contentEditText = findViewById(R.id.postContentEditText)
        postImageView = findViewById(R.id.postImageView)
        addImageButton = findViewById(R.id.addImageButton)
        submitButton = findViewById(R.id.submitButton)
        progressBar = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbar)
    }

    private fun setupClickListeners() {
        addImageButton.setOnClickListener {
            openImagePicker()
        }

        submitButton.setOnClickListener {
            validateAndSubmitPost()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            
            // Display selected image
            Glide.with(this)
                .load(selectedImageUri)
                .centerCrop()
                .into(postImageView)
                
            postImageView.visibility = View.VISIBLE
        }
    }

    private fun validateAndSubmitPost() {
        val title = titleEditText.text.toString().trim()
        val content = contentEditText.text.toString().trim()
        
        // Validate inputs
        if (title.isEmpty()) {
            titleEditText.error = "נא להזין כותרת"
            return
        }
        
        if (content.isEmpty()) {
            contentEditText.error = "נא להזין תוכן"
            return
        }
        
        // Show progress
        progressBar.visibility = View.VISIBLE
        submitButton.isEnabled = false
        
        // Get current user
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "יש להתחבר כדי לפרסם פוסט", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
            submitButton.isEnabled = true
            return
        }
        
        // If image is selected, upload it first
        if (selectedImageUri != null) {
            uploadImageAndCreatePost(currentUser.uid, title, content)
        } else {
            // Create post without image
            createPost(currentUser.uid, title, content, "")
        }
    }
    
    private fun uploadImageAndCreatePost(userId: String, title: String, content: String) {
        val storageRef = storage.reference
        val imageRef = storageRef.child("post_images/${UUID.randomUUID()}")
        
        selectedImageUri?.let { uri ->
            imageRef.putFile(uri)
                .addOnSuccessListener {
                    // Get download URL
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        createPost(userId, title, content, downloadUri.toString())
                    }
                }
                .addOnFailureListener { e ->
                    progressBar.visibility = View.GONE
                    submitButton.isEnabled = true
                    Toast.makeText(this, "שגיאה בהעלאת התמונה: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    
    private fun createPost(userId: String, title: String, content: String, imageUrl: String) {
        // Get user data
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val userName = document.getString("displayName") ?: "משתמש"
                val userPhotoUrl = document.getString("photoUrl") ?: ""
                
                // Create post object
                val post = hashMapOf(
                    "userId" to userId,
                    "title" to title,
                    "content" to content,
                    "imageUrl" to imageUrl,
                    "authorName" to userName,
                    "authorPhotoUrl" to userPhotoUrl,
                    "createdAt" to com.google.firebase.Timestamp.now(),
                    "likes" to 0,
                    "comments" to 0
                )
                
                // Save to Firestore
                firestore.collection("posts")
                    .add(post)
                    .addOnSuccessListener {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, "הפוסט פורסם בהצלחה!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        progressBar.visibility = View.GONE
                        submitButton.isEnabled = true
                        Toast.makeText(this, "שגיאה בפרסום הפוסט: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                submitButton.isEnabled = true
                Toast.makeText(this, "שגיאה בטעינת נתוני משתמש", Toast.LENGTH_SHORT).show()
            }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
