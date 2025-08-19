package com.example.techlift

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techlift.adapter.LessonAdapter
import com.example.techlift.data.LearningContentManager
import com.example.techlift.model.Lesson
import com.example.techlift.model.Roadmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RoadmapDetailActivity : AppCompatActivity(), LessonAdapter.OnLessonClickListener {
    
    private lateinit var toolbar: Toolbar
    private lateinit var lessonsRecyclerView: RecyclerView
    private lateinit var lessonAdapter: LessonAdapter
    private lateinit var roadmap: Roadmap
    private lateinit var completeCourseButton: com.google.android.material.button.MaterialButton
    
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
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
        completeCourseButton = findViewById(R.id.completeCourseButton)
        
        // הגדרת הסרגל העליון
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = roadmapTitle
        
        // הגדרת רשימת השיעורים
        setupLessonsList()
        
        // הגדרת כפתור סיום קורס
        setupCompleteCourseButton()
    }
    
    private fun setupLessonsList() {
        val lessons = LearningContentManager.getLessonsForRoadmap(roadmap.id)
        lessonAdapter = LessonAdapter(lessons, this)
        lessonsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RoadmapDetailActivity)
            adapter = lessonAdapter
        }
        
        // Load user's progress for this course
        loadUserProgress()
    }
    
    private fun setupCompleteCourseButton() {
        completeCourseButton.setOnClickListener {
            completeCourse()
        }
        completeCourseButton.visibility = View.GONE
    }
    
    private fun completeCourse() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val courseData = hashMapOf(
                "courseId" to roadmap.id,
                "courseTitle" to roadmap.title,
                "completedAt" to com.google.firebase.Timestamp.now(),
                "status" to "completed"
            )
            
            firestore.collection("users").document(currentUser.uid)
                .collection("completedCourses").document(roadmap.id)
                .set(courseData)
                .addOnSuccessListener {
                    Toast.makeText(this, "הקורס הושלם בהצלחה!", Toast.LENGTH_LONG).show()
                    completeCourseButton.visibility = View.GONE
                    completeCourseButton.text = "קורס הושלם ✓"
                    completeCourseButton.isEnabled = false
                }
        }
    }
    
    private fun loadUserProgress() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid)
                .collection("completedLessons")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    // Update lesson completion status based on user's progress
                    val completedLessonIds = querySnapshot.documents.mapNotNull { doc ->
                        doc.getString("lessonId")
                    }.toSet()
                    
                    // Get lessons for this roadmap
                    val roadmapLessons = LearningContentManager.getLessonsForRoadmap(roadmap.id)
                    
                    // Update lessons with completion status
                    val updatedLessons = roadmapLessons.map { lesson ->
                        lesson.copy(isCompleted = completedLessonIds.contains(lesson.id))
                    }
                    
                    // Update adapter with completed lessons
                    lessonAdapter.updateLessons(updatedLessons)
                    
                    // Check if all lessons are completed
                    val allLessonsCompleted = updatedLessons.all { it.isCompleted }
                    if (allLessonsCompleted) {
                        completeCourseButton.visibility = View.VISIBLE
                    }
                    
                    // Check if course is already completed
                    checkIfCourseAlreadyCompleted()
                }
        }
    }
    
    private fun checkIfCourseAlreadyCompleted() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid)
                .collection("completedCourses").document(roadmap.id)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        completeCourseButton.text = "קורס הושלם ✓"
                        completeCourseButton.isEnabled = false
                        completeCourseButton.visibility = View.VISIBLE
                    }
                }
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