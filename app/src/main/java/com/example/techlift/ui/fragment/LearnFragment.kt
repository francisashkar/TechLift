package com.example.techlift.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techlift.R
import com.google.android.material.card.MaterialCardView

class LearnFragment : Fragment() {

    private lateinit var coursesCard: MaterialCardView
    private lateinit var tutorialsCard: MaterialCardView
    private lateinit var resourcesCard: MaterialCardView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_learn, container, false)
        
        // Initialize views
        coursesCard = view.findViewById(R.id.coursesCard)
        tutorialsCard = view.findViewById(R.id.tutorialsCard)
        resourcesCard = view.findViewById(R.id.resourcesCard)
        
        // Set up click listeners
        setupClickListeners()
        
        return view
    }
    
    private fun setupClickListeners() {
        coursesCard.setOnClickListener {
            showToast("Courses feature coming soon!")
        }
        
        tutorialsCard.setOnClickListener {
            showToast("Tutorials feature coming soon!")
        }
        
        resourcesCard.setOnClickListener {
            showToast("Resources feature coming soon!")
        }
    }
    
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
} 