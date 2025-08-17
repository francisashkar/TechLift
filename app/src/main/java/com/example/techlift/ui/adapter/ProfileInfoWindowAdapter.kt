package com.example.techlift.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.techlift.R
import com.example.techlift.model.User
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

/**
 * Custom InfoWindowAdapter for displaying user profile information when clicking on map markers
 */
class ProfileInfoWindowAdapter(
    private val context: Context,
    private val onViewPostsClicked: (String) -> Unit
) : GoogleMap.InfoWindowAdapter {

    private val window: View = LayoutInflater.from(context).inflate(R.layout.profile_info_window, null)
    private var currentUserId: String? = null

    override fun getInfoWindow(marker: Marker): View? {
        return null // Use default window frame
    }

    override fun getInfoContents(marker: Marker): View {
        val user = marker.tag as? User ?: return window

        currentUserId = user.uid

        val profileImageView = window.findViewById<ImageView>(R.id.profile_image)
        val profileNameTextView = window.findViewById<TextView>(R.id.profile_name)
        val profileRoleTextView = window.findViewById<TextView>(R.id.profile_role)
        val profileExperienceTextView = window.findViewById<TextView>(R.id.profile_experience)
        val profileEmailTextView = window.findViewById<TextView>(R.id.profile_email)
        val viewPostsButton = window.findViewById<Button>(R.id.view_posts_button)

        // Set profile information
        profileNameTextView.text = user.displayName
        profileRoleTextView.text = user.specialization
        profileExperienceTextView.text = "ניסיון: ${user.experience}"
        profileEmailTextView.text = user.email

        // Load profile image
        if (user.photoUrl.isNotEmpty()) {
            Glide.with(context)
                .load(user.photoUrl)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .circleCrop()
                .into(profileImageView)
        } else {
            profileImageView.setImageResource(R.drawable.ic_profile)
        }

        // The button click will be handled in the onInfoWindowClick listener
        // since buttons in InfoWindow don't receive click events directly

        return window
    }

    fun getCurrentUserId(): String? {
        return currentUserId
    }
}
