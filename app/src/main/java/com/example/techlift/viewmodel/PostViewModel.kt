package com.example.techlift.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.techlift.data.local.AppDatabase
import com.example.techlift.data.repository.PostRepository
import com.example.techlift.model.Post
import kotlinx.coroutines.launch

class PostViewModel(application: Application) : AndroidViewModel(application) {
    
    private val postRepository: PostRepository
    
    // LiveData for post creation state
    private val _createPostState = MutableLiveData<CreatePostState>()
    val createPostState: LiveData<CreatePostState> = _createPostState
    
    // LiveData for post update state
    private val _updatePostState = MutableLiveData<UpdatePostState>()
    val updatePostState: LiveData<UpdatePostState> = _updatePostState
    
    // LiveData for post deletion state
    private val _deletePostState = MutableLiveData<DeletePostState>()
    val deletePostState: LiveData<DeletePostState> = _deletePostState
    
    // LiveData for post loading state
    private val _postsLoadingState = MutableLiveData<PostsLoadingState>()
    val postsLoadingState: LiveData<PostsLoadingState> = _postsLoadingState
    
    init {
        val postDao = AppDatabase.getDatabase(application).postDao()
        postRepository = PostRepository(postDao)
        
        // Load posts when ViewModel is created
        loadPosts()
    }
    
    // Get all posts
    fun getAllPosts(): LiveData<List<Post>> {
        return postRepository.getAllPosts()
    }
    
    // Get posts by user
    fun getPostsByUser(userId: String): LiveData<List<Post>> {
        return postRepository.getPostsByUser(userId)
    }
    
    // Get post by ID
    fun getPostById(postId: String): LiveData<Post?> {
        return postRepository.getPostById(postId)
    }
    
    // Load posts from remote source
    fun loadPosts() {
        _postsLoadingState.value = PostsLoadingState.Loading
        
        viewModelScope.launch {
            try {
                postRepository.fetchPosts()
                _postsLoadingState.value = PostsLoadingState.Success
            } catch (e: Exception) {
                _postsLoadingState.value = PostsLoadingState.Error(e.message ?: "שגיאה בטעינת פוסטים")
            }
        }
    }
    
    // Create a new post
    fun createPost(title: String, content: String, imageUri: Uri?) {
        _createPostState.value = CreatePostState.Loading
        
        viewModelScope.launch {
            try {
                val result = postRepository.createPost(title, content, imageUri)
                
                result.fold(
                    onSuccess = { post ->
                        _createPostState.value = CreatePostState.Success(post)
                    },
                    onFailure = { exception ->
                        _createPostState.value = CreatePostState.Error(exception.message ?: "שגיאה ביצירת פוסט")
                    }
                )
            } catch (e: Exception) {
                _createPostState.value = CreatePostState.Error(e.message ?: "שגיאה ביצירת פוסט")
            }
        }
    }
    
    // Update an existing post
    fun updatePost(postId: String, title: String, content: String, imageUri: Uri?) {
        _updatePostState.value = UpdatePostState.Loading
        
        viewModelScope.launch {
            try {
                val result = postRepository.updatePost(postId, title, content, imageUri)
                
                result.fold(
                    onSuccess = { post ->
                        _updatePostState.value = UpdatePostState.Success(post)
                    },
                    onFailure = { exception ->
                        _updatePostState.value = UpdatePostState.Error(exception.message ?: "שגיאה בעדכון פוסט")
                    }
                )
            } catch (e: Exception) {
                _updatePostState.value = UpdatePostState.Error(e.message ?: "שגיאה בעדכון פוסט")
            }
        }
    }
    
    // Delete a post
    fun deletePost(postId: String) {
        _deletePostState.value = DeletePostState.Loading
        
        viewModelScope.launch {
            try {
                val result = postRepository.deletePost(postId)
                
                result.fold(
                    onSuccess = { success ->
                        _deletePostState.value = DeletePostState.Success(postId)
                    },
                    onFailure = { exception ->
                        _deletePostState.value = DeletePostState.Error(exception.message ?: "שגיאה במחיקת פוסט")
                    }
                )
            } catch (e: Exception) {
                _deletePostState.value = DeletePostState.Error(e.message ?: "שגיאה במחיקת פוסט")
            }
        }
    }
    
    // Create post state sealed class
    sealed class CreatePostState {
        object Loading : CreatePostState()
        data class Success(val post: Post) : CreatePostState()
        data class Error(val message: String) : CreatePostState()
    }
    
    // Update post state sealed class
    sealed class UpdatePostState {
        object Loading : UpdatePostState()
        data class Success(val post: Post) : UpdatePostState()
        data class Error(val message: String) : UpdatePostState()
    }
    
    // Delete post state sealed class
    sealed class DeletePostState {
        object Loading : DeletePostState()
        data class Success(val postId: String) : DeletePostState()
        data class Error(val message: String) : DeletePostState()
    }
    
    // Posts loading state sealed class
    sealed class PostsLoadingState {
        object Loading : PostsLoadingState()
        object Success : PostsLoadingState()
        data class Error(val message: String) : PostsLoadingState()
    }
} 