package com.example.techlift.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.techlift.data.local.entity.PostEntity

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)
    
    @Update
    suspend fun updatePost(post: PostEntity)
    
    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAllPosts(): LiveData<List<PostEntity>>
    
    @Query("SELECT * FROM posts WHERE userId = :userId ORDER BY createdAt DESC")
    fun getPostsByUser(userId: String): LiveData<List<PostEntity>>
    
    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostById(postId: String): LiveData<PostEntity>
    
    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostByIdSync(postId: String): PostEntity?
    
    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePost(postId: String)
    
    @Query("DELETE FROM posts WHERE userId = :userId")
    suspend fun deleteUserPosts(userId: String)
    
    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()
} 