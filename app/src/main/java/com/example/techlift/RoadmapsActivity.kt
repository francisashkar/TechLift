package com.example.techlift

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techlift.adapter.RoadmapAdapter
import com.example.techlift.model.Roadmap
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RoadmapsActivity : AppCompatActivity(), RoadmapAdapter.OnRoadmapClickListener {
    private lateinit var toolbar: Toolbar
    private lateinit var searchInput: TextInputEditText
    private lateinit var roadmapsRecyclerView: RecyclerView
    private lateinit var roadmapAdapter: RoadmapAdapter

    // קורסים נוכחיים - כל 5 הקורסים עובדים
    private val mockRoadmaps = listOf(
        Roadmap(
            id = "frontend",
            title = "קורס פיתוח צד לקוח",
            description = "למד HTML, CSS, JavaScript, React, Vue.js וטכנולוגיות מודרניות לפיתוח ממשקי משתמש",
            progress = 0
        ),
        Roadmap(
            id = "backend",
            title = "קורס פיתוח צד שרת",
            description = "למד Node.js, Python, Java, Spring Boot, APIs ופיתוח מערכות צד שרת",
            progress = 0
        ),
        Roadmap(
            id = "mobile",
            title = "קורס פיתוח אפליקציות מובייל",
            description = "למד React Native, Flutter, Android Native ו-iOS development",
            progress = 0
        ),
        Roadmap(
            id = "devops",
            title = "קורס DevOps והנדסת תשתיות",
            description = "למד Docker, Kubernetes, CI/CD, AWS, Azure וניהול תשתיות ענן",
            progress = 0
        ),
        Roadmap(
            id = "ai",
            title = "קורס בינה מלאכותית ולמידת מכונה",
            description = "למד Python, TensorFlow, PyTorch, NLP, Computer Vision ו-ML algorithms",
            progress = 0
        )
    )
    
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roadmaps)

        // אתחול רכיבים
        toolbar = findViewById(R.id.toolbar)
        searchInput = findViewById(R.id.searchInput)
        roadmapsRecyclerView = findViewById(R.id.roadmapsRecyclerView)

        // הגדרת הסרגל העליון
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.roadmap_title)

        // הגדרת הרשימה
        setupRecyclerView()
        
        // Load real progress from Firebase
        loadUserProgress()
    }

    private fun setupRecyclerView() {
        roadmapAdapter = RoadmapAdapter(mockRoadmaps, this)
        roadmapsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RoadmapsActivity)
            adapter = roadmapAdapter
        }
    }
    
    private fun loadUserProgress() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid)
                .collection("completedCourses")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    // Update course completion status based on completed courses
                    val completedCourseIds = querySnapshot.documents.mapNotNull { doc ->
                        doc.getString("courseId")
                    }.toSet()
                    
                    val updatedRoadmaps = mockRoadmaps.map { roadmap ->
                        if (completedCourseIds.contains(roadmap.id)) {
                            roadmap.copy(progress = 100) // Mark as completed
                        } else {
                            roadmap.copy(progress = 0) // Mark as not completed
                        }
                    }
                    
                    // Update adapter with completion status
                    roadmapAdapter.updateRoadmaps(updatedRoadmaps)
                }
                .addOnFailureListener {
                    // If failed to load, show all courses as not completed
                    val updatedRoadmaps = mockRoadmaps.map { roadmap ->
                        roadmap.copy(progress = 0)
                    }
                    roadmapAdapter.updateRoadmaps(updatedRoadmaps)
                }
        }
    }

    override fun onRoadmapClick(roadmap: Roadmap, position: Int) {
        // כל המסלולים עובדים כעת - מעבר למסך פרטי המסלול
        val intent = Intent(this, RoadmapDetailActivity::class.java).apply {
            putExtra(RoadmapDetailActivity.EXTRA_ROADMAP_ID, roadmap.id)
            putExtra(RoadmapDetailActivity.EXTRA_ROADMAP_TITLE, roadmap.title)
        }
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 