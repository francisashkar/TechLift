package com.example.techlift.model

import java.util.Date

/**
 * Post data model representing user-shared content
 */
data class Post(
    val id: String = "",
    val userId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String = "",
    val title: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val likes: Int = 0,
    val comments: Int = 0,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) {
    // Empty constructor for Firestore
    constructor() : this("", "", "", "", "", "", "")
    
    companion object {
        const val COLLECTION = "posts"
    }
} 