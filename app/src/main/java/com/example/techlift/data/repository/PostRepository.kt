package com.example.techlift.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.techlift.data.local.dao.PostDao
import com.example.techlift.data.local.entity.PostEntity
import com.example.techlift.model.Post
import com.example.techlift.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.UUID
import com.example.techlift.data.MockDataProvider

class PostRepository(
    private val postDao: PostDao,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    // Get all posts from local database
    fun getAllPosts(): LiveData<List<Post>> {
        return postDao.getAllPosts().map { entities ->
            entities.map { it.toPost() }
        }
    }
    
    // Get posts by user ID from local database
    fun getPostsByUser(userId: String): LiveData<List<Post>> {
        return postDao.getPostsByUser(userId).map { entities ->
            entities.map { it.toPost() }
        }
    }
    
    // Get post by ID from local database
    fun getPostById(postId: String): LiveData<Post?> {
        return postDao.getPostById(postId).map { it?.toPost() }
    }
    
    // Fetch all posts from Firestore and merge with mock posts
    suspend fun fetchPosts() {
        withContext(Dispatchers.IO) {
            try {
                // Get posts from Firestore
                val querySnapshot = firestore.collection(Post.COLLECTION)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .get()
                    .await()
                
                // Convert to Post objects
                val remotePosts = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Post::class.java)?.copy(id = document.id)
                }
                
                // Merge remote posts with mock posts (dedupe by id, keep newest order)
                val mergedPosts = (remotePosts + MockDataProvider.mockPosts)
                    .associateBy { it.id }
                    .values
                    .sortedByDescending { it.createdAt }
                
                // Save to local database
                val postEntities = mergedPosts.map { PostEntity.fromPost(it) }
                postDao.insertPosts(postEntities)
            } catch (e: Exception) {
                // On error, fallback to mock posts only
                val postEntities = MockDataProvider.mockPosts
                    .sortedByDescending { it.createdAt }
                    .map { PostEntity.fromPost(it) }
                postDao.insertPosts(postEntities)
                e.printStackTrace()
            }
        }
    }
    
    // Create a new post
    suspend fun createPost(title: String, content: String, imageUri: Uri?): Result<Post> {
        return withContext(Dispatchers.IO) {
            try {
                val currentUser = auth.currentUser ?: throw Exception("User not logged in")
                
                // Get user data
                val userDoc = firestore.collection(User.COLLECTION)
                    .document(currentUser.uid)
                    .get()
                    .await()
                
                val user = userDoc.toObject(User::class.java) ?: throw Exception("User data not found")
                
                var imageUrl = ""
                
                // Upload image if provided
                if (imageUri != null) {
                    val filename = UUID.randomUUID().toString()
                    val storageRef = storage.reference.child("post_images/${currentUser.uid}/$filename")
                    
                    storageRef.putFile(imageUri).await()
                    imageUrl = storageRef.downloadUrl.await().toString()
                }
                
                // Create post object
                val postId = UUID.randomUUID().toString()
                val now = Date()
                val post = Post(
                    id = postId,
                    userId = currentUser.uid,
                    authorName = user.displayName,
                    authorPhotoUrl = user.photoUrl,
                    title = title,
                    content = content,
                    imageUrl = imageUrl,
                    createdAt = now,
                    updatedAt = now
                )
                
                // Save to Firestore
                firestore.collection(Post.COLLECTION)
                    .document(postId)
                    .set(post)
                    .await()
                
                // Save to local database
                postDao.insertPost(PostEntity.fromPost(post))
                
                Result.success(post)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    // Update an existing post
    suspend fun updatePost(postId: String, title: String, content: String, imageUri: Uri?): Result<Post> {
        // Implementation details...
        return Result.failure(Exception("Not implemented"))
    }
    
    // Delete a post
    suspend fun deletePost(postId: String): Result<Boolean> {
        // Implementation details...
        return Result.failure(Exception("Not implemented"))
    }
} 