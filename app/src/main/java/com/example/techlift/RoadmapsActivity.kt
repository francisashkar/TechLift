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

class RoadmapsActivity : AppCompatActivity(), RoadmapAdapter.OnRoadmapClickListener {
    private lateinit var toolbar: Toolbar
    private lateinit var searchInput: TextInputEditText
    private lateinit var roadmapsRecyclerView: RecyclerView
    private lateinit var roadmapAdapter: RoadmapAdapter

    // מסלולים חדשים - כל 5 המסלולים עובדים
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
            id = "mobile",
            title = "פיתוח אפליקציות מובייל",
            description = "למד React Native, Flutter, Android Native ו-iOS development",
            progress = 20
        ),
        Roadmap(
            id = "devops",
            title = "DevOps והנדסת תשתיות",
            description = "למד Docker, Kubernetes, CI/CD, AWS, Azure וניהול תשתיות ענן",
            progress = 15
        ),
        Roadmap(
            id = "ai",
            title = "בינה מלאכותית ולמידת מכונה",
            description = "למד Python, TensorFlow, PyTorch, NLP, Computer Vision ו-ML algorithms",
            progress = 10
        )
    )

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
    }

    private fun setupRecyclerView() {
        roadmapAdapter = RoadmapAdapter(mockRoadmaps, this)
        roadmapsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RoadmapsActivity)
            adapter = roadmapAdapter
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