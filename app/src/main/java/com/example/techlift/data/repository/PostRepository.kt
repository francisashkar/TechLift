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
    
    // Fetch all posts from Firestore
    suspend fun fetchPosts() {
        withContext(Dispatchers.IO) {
            try {
                // Get posts from Firestore
                val querySnapshot = firestore.collection(Post.COLLECTION)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .get()
                    .await()
                
                // Convert to Post objects
                val posts = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Post::class.java)?.copy(id = document.id)
                }
                
                // If there are no posts in Firestore, create sample posts
                if (posts.isEmpty()) {
                    createSamplePosts()
                    return@withContext
                }
                
                // Save to local database
                val postEntities = posts.map { PostEntity.fromPost(it) }
                postDao.insertPosts(postEntities)
            } catch (e: Exception) {
                // If there's an error fetching from Firestore, create sample posts
                createSamplePosts()
                e.printStackTrace()
            }
        }
    }
    
    // Create sample posts for testing
    private suspend fun createSamplePosts() {
        val samplePosts = listOf(
            Post(
                id = "sample1",
                userId = "sample_user1",
                authorName = "יוסי כהן",
                authorPhotoUrl = "",
                title = "טיפים ללימוד פיתוח אנדרואיד",
                content = "אנדרואיד הוא מערכת הפעלה נפלאה לפיתוח אפליקציות. הנה כמה טיפים שעזרו לי בדרך:\n\n" +
                        "1. התחילו עם הבסיס - למדו Kotlin היטב\n" +
                        "2. הבינו את מחזור החיים של Activity ו-Fragment\n" +
                        "3. למדו על ארכיטקטורת MVVM\n" +
                        "4. תרגלו באמצעות פרויקטים קטנים\n" +
                        "5. הצטרפו לקהילות מפתחים",
                createdAt = Date(System.currentTimeMillis() - 86400000), // 1 day ago
                updatedAt = Date(System.currentTimeMillis() - 86400000)
            ),
            Post(
                id = "sample2",
                userId = "sample_user2",
                authorName = "מיכל לוי",
                authorPhotoUrl = "",
                title = "המעבר שלי מפיתוח ווב לפיתוח מובייל",
                content = "אחרי 3 שנים כמפתחת ווב, החלטתי לעבור לפיתוח מובייל. התהליך היה מאתגר אבל מספק מאוד. " +
                        "הדבר הכי חשוב שלמדתי הוא שהעקרונות של פיתוח טוב הם אוניברסליים - קוד נקי, ארכיטקטורה טובה, " +
                        "ובדיקות יסודיות חשובים בכל פלטפורמה. אשמח לענות על שאלות למי שמתלבט לגבי מעבר דומה!",
                createdAt = Date(System.currentTimeMillis() - 172800000), // 2 days ago
                updatedAt = Date(System.currentTimeMillis() - 172800000)
            ),
            Post(
                id = "sample3",
                userId = "sample_user3",
                authorName = "דוד אברהם",
                authorPhotoUrl = "",
                title = "פרויקט סיום הקורס שלי - TechLift",
                content = "רציתי לשתף את פרויקט הסיום שלי בקורס פיתוח אנדרואיד. בניתי אפליקציה בשם TechLift שעוזרת לאנשים " +
                        "ללמוד טכנולוגיות חדשות באמצעות מסלולי למידה מובנים. השתמשתי ב-MVVM, Room, Firebase, ו-Navigation Component. " +
                        "האתגר הגדול ביותר היה לשלב בין מסד נתונים מקומי לענן, אבל הצלחתי למצוא פתרון טוב. " +
                        "אשמח לפידבק והצעות לשיפור!",
                createdAt = Date(System.currentTimeMillis() - 259200000), // 3 days ago
                updatedAt = Date(System.currentTimeMillis() - 259200000)
            )
        )
        
        // Save sample posts to local database
        val postEntities = samplePosts.map { PostEntity.fromPost(it) }
        postDao.insertPosts(postEntities)
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