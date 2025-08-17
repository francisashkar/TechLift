package com.example.techlift.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.techlift.R
import com.example.techlift.model.Post
import java.text.SimpleDateFormat
import java.util.Locale

class PostAdapter(private var posts: List<Post>, private val listener: OnPostClickListener? = null) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    
    interface OnPostClickListener {
        fun onPostClick(post: Post)
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorImageView: ImageView = itemView.findViewById(R.id.authorImageView)
        val authorNameTextView: TextView = itemView.findViewById(R.id.authorNameTextView)
        val postDateTextView: TextView = itemView.findViewById(R.id.postDateTextView)
        val postTitleTextView: TextView = itemView.findViewById(R.id.postTitleTextView)
        val postContentTextView: TextView = itemView.findViewById(R.id.postContentTextView)
        val postImageView: ImageView = itemView.findViewById(R.id.postImageView)
        val likesTextView: TextView = itemView.findViewById(R.id.likesTextView)
        val commentsTextView: TextView = itemView.findViewById(R.id.commentsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        val context = holder.itemView.context
        
        // Set click listener
        holder.itemView.setOnClickListener {
            listener?.onPostClick(post)
        }

        // Load author image
        if (post.authorPhotoUrl.isNotEmpty()) {
            Glide.with(context)
                .load(post.authorPhotoUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_profile)
                .into(holder.authorImageView)
        } else {
            holder.authorImageView.setImageResource(R.drawable.ic_profile)
        }

        // Set author name
        holder.authorNameTextView.text = post.authorName

        // Format and set date
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.postDateTextView.text = dateFormat.format(post.createdAt)

        // Set post title and content
        holder.postTitleTextView.text = post.title
        holder.postContentTextView.text = post.content

        // Load post image if available
        if (post.imageUrl.isNotEmpty()) {
            holder.postImageView.visibility = View.VISIBLE
            Glide.with(context)
                .load(post.imageUrl)
                .centerCrop()
                .into(holder.postImageView)
        } else {
            holder.postImageView.visibility = View.GONE
        }

        // Set likes and comments count
        holder.likesTextView.text = "${post.likes} לייקים"
        holder.commentsTextView.text = "${post.comments} תגובות"
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}