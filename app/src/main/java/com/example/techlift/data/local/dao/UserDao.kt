package com.example.techlift.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.techlift.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    @Update
    suspend fun updateUser(user: UserEntity)
    
    @Query("SELECT * FROM users WHERE uid = :uid")
    fun getUserById(uid: String): LiveData<UserEntity>
    
    @Query("SELECT * FROM users WHERE uid = :uid")
    suspend fun getUserByIdSync(uid: String): UserEntity?
    
    @Query("DELETE FROM users WHERE uid = :uid")
    suspend fun deleteUser(uid: String)
    
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
} 