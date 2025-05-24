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

    // נתוני הדמה למסלולים
    private val mockRoadmaps = listOf(
        Roadmap(
            id = "dev",
            title = "פיתוח תוכנה",
            description = "למד את יסודות התכנות, פיתוח צד שרת ופיתוח צד לקוח עם שפות וטכנולוגיות מודרניות",
            progress = 15
        ),
        Roadmap(
            id = "qa",
            title = "QA ובדיקות תוכנה",
            description = "למד את יסודות בדיקות התוכנה, כלי בדיקות אוטומטיות ובדיקות אבטחה",
            progress = 0
        ),
        Roadmap(
            id = "cyber",
            title = "אבטחת מידע וסייבר",
            description = "למד אבטחת רשתות, אבטחת מערכות ותשתיות, הצפנה ופתרון אירועי אבטחה",
            progress = 0
        ),
        Roadmap(
            id = "data",
            title = "מדעי נתונים",
            description = "למד ניתוח נתונים, למידת מכונה, ויזואליזציה של נתונים ועבודה עם כלי Big Data",
            progress = 0
        ),
        Roadmap(
            id = "noc",
            title = "תפעול רשת (NOC)",
            description = "למד ניהול רשתות, ניטור מערכות, פתרון תקלות וטיפול בתשתיות IT",
            progress = 0
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
        // בדמו, רק המסלול הראשון (פיתוח) זמין
        if (roadmap.id == "dev") {
            // כאן יהיה מעבר למסך פרטי המסלול
            Toast.makeText(
                this,
                "פתיחת מסלול ${roadmap.title}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                "מסלול ${roadmap.title} אינו זמין כרגע בדמו",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 