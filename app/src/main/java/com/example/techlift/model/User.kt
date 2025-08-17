package com.example.techlift.model

import java.util.Date

/**
 * User data model representing a user in the application
 */
data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val specialization: String = "",
    val experience: String = "",
    val createdAt: Date = Date(),
    val lastLoginAt: Date = Date()
) {
    // Empty constructor for Firestore
    constructor() : this("", "", "", "", "", "")
    
    companion object {
        const val COLLECTION = "users"
    }
} 