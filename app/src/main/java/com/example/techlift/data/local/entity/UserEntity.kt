package com.example.techlift.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.techlift.model.User
import java.util.Date

/**
 * Room entity for local caching of user data
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val uid: String,
    val email: String,
    val displayName: String,
    val photoUrl: String,
    val specialization: String,
    val experience: String,
    val createdAt: Long,
    val lastLoginAt: Long
) {
    companion object {
        fun fromUser(user: User): UserEntity {
            return UserEntity(
                uid = user.uid,
                email = user.email,
                displayName = user.displayName,
                photoUrl = user.photoUrl,
                specialization = user.specialization,
                experience = user.experience,
                createdAt = user.createdAt.time,
                lastLoginAt = user.lastLoginAt.time
            )
        }
    }
    
    fun toUser(): User {
        return User(
            uid = uid,
            email = email,
            displayName = displayName,
            photoUrl = photoUrl,
            specialization = specialization,
            experience = experience,
            createdAt = Date(createdAt),
            lastLoginAt = Date(lastLoginAt)
        )
    }
} 