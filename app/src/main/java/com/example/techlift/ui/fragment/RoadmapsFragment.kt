package com.example.techlift.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techlift.R
import com.example.techlift.RoadmapDetailActivity
import com.example.techlift.adapter.RoadmapAdapter
import com.example.techlift.model.Roadmap
import com.google.android.material.textfield.TextInputEditText

class RoadmapsFragment : Fragment(), RoadmapAdapter.OnRoadmapClickListener {
    
    private lateinit var searchInput: TextInputEditText
    private lateinit var roadmapsRecyclerView: RecyclerView
    private lateinit var roadmapAdapter: RoadmapAdapter

    // מסלולים חדשים - 3 המסלולים העיקריים
    private val mockRoadmaps = listOf(
        Roadmap(
            id = "frontend",
            title = "פיתוח צד לקוח",
            description = "למד HTML, CSS, JavaScript, React, Vue.js וטכנולוגיות מודרניות לפיתוח ממשקי משתמש",
            progress = 30
        ),
        Roadmap(
            id = "backend",
            title = "פיתוח צד שרת",
            description = "למד Node.js, Python, Java, Spring Boot, APIs ופיתוח מערכות צד שרת",
            progress = 45
        ),
        Roadmap(
            id = "ai",
            title = "בינה מלאכותית ולמידת מכונה",
            description = "למד Python, TensorFlow, PyTorch, NLP, Computer Vision ו-ML algorithms",
            progress = 10
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_roadmaps, container, false)
        
        // Initialize views
        searchInput = view.findViewById(R.id.searchInput)
        roadmapsRecyclerView = view.findViewById(R.id.roadmapsRecyclerView)
        
        // Set up RecyclerView
        setupRecyclerView()
        
        return view
    }
    
    private fun setupRecyclerView() {
        roadmapAdapter = RoadmapAdapter(mockRoadmaps, this)
        roadmapsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = roadmapAdapter
        }
    }
    
    override fun onRoadmapClick(roadmap: Roadmap, position: Int) {
        // כל המסלולים עובדים כעת - מעבר למסך פרטי המסלול
        val intent = Intent(requireContext(), RoadmapDetailActivity::class.java).apply {
            putExtra(RoadmapDetailActivity.EXTRA_ROADMAP_ID, roadmap.id)
            putExtra(RoadmapDetailActivity.EXTRA_ROADMAP_TITLE, roadmap.title)
        }
        startActivity(intent)
    }
} 