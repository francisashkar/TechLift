package com.example.techlift.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.techlift.model.Post
import java.util.Date

/**
 * Room entity for local caching of post data
 */
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val authorName: String,
    val authorPhotoUrl: String,
    val title: String,
    val content: String,
    val imageUrl: String,
    val likes: Int,
    val comments: Int,
    val createdAt: Long,
    val updatedAt: Long
) {
    companion object {
        fun fromPost(post: Post): PostEntity {
            return PostEntity(
                id = post.id,
                userId = post.userId,
                authorName = post.authorName,
                authorPhotoUrl = post.authorPhotoUrl,
                title = post.title,
                content = post.content,
                imageUrl = post.imageUrl,
                likes = post.likes,
                comments = post.comments,
                createdAt = post.createdAt.time,
                updatedAt = post.updatedAt.time
            )
        }
    }
    
    fun toPost(): Post {
        return Post(
            id = id,
            userId = userId,
            authorName = authorName,
            authorPhotoUrl = authorPhotoUrl,
            title = title,
            content = content,
            imageUrl = imageUrl,
            likes = likes,
            comments = comments,
            createdAt = Date(createdAt),
            updatedAt = Date(updatedAt)
        )
    }
} 