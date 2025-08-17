package com.example.techlift.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.techlift.data.local.dao.PostDao
import com.example.techlift.data.local.dao.UserDao
import com.example.techlift.data.local.entity.PostEntity
import com.example.techlift.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, PostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "techlift_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 