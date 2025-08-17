package com.example.techlift

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techlift.adapter.LessonAdapter
import com.example.techlift.data.LearningContentManager
import com.example.techlift.model.Lesson
import com.example.techlift.model.Roadmap

class RoadmapDetailActivity : AppCompatActivity(), LessonAdapter.OnLessonClickListener {
    
    private lateinit var toolbar: Toolbar
    private lateinit var lessonsRecyclerView: RecyclerView
    private lateinit var lessonAdapter: LessonAdapter
    private lateinit var roadmap: Roadmap
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roadmap_detail)
        
        // קבלת נתוני המסלול
        val roadmapId = intent.getStringExtra(EXTRA_ROADMAP_ID) ?: ""
        val roadmapTitle = intent.getStringExtra(EXTRA_ROADMAP_TITLE) ?: ""
        
        // יצירת אובייקט Roadmap זמני
        roadmap = Roadmap(
            id = roadmapId,
            title = roadmapTitle,
            description = "",
            progress = 0
        )
        
        // אתחול רכיבים
        toolbar = findViewById(R.id.toolbar)
        lessonsRecyclerView = findViewById(R.id.lessonsRecyclerView)
        
        // הגדרת הסרגל העליון
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = roadmapTitle
        
        // הגדרת רשימת השיעורים
        setupLessonsList()
    }
    
    private fun setupLessonsList() {
        val lessons = LearningContentManager.getLessonsForRoadmap(roadmap.id)
        lessonAdapter = LessonAdapter(lessons, this)
        lessonsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RoadmapDetailActivity)
            adapter = lessonAdapter
        }
    }
    
    override fun onLessonClick(lesson: Lesson, position: Int) {
        // מעבר למסך השיעור
        val intent = Intent(this, LessonActivity::class.java).apply {
            putExtra(LessonActivity.EXTRA_LESSON_ID, lesson.id)
            putExtra(LessonActivity.EXTRA_LESSON_TITLE, lesson.title)
            putExtra(LessonActivity.EXTRA_ROADMAP_ID, roadmap.id)
        }
        startActivity(intent)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == LESSON_REQUEST_CODE && resultCode == RESULT_OK) {
            // Lesson was completed, refresh the list
            setupLessonsList()
        }
    }

    companion object {
        const val EXTRA_ROADMAP_ID = "roadmap_id"
        const val EXTRA_ROADMAP_TITLE = "roadmap_title"
        private const val LESSON_REQUEST_CODE = 1001
    }
} 