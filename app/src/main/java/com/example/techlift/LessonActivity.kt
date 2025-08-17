package com.example.techlift

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.techlift.data.LearningContentManager
import com.example.techlift.model.Lesson
import com.example.techlift.model.Quiz
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LessonActivity : AppCompatActivity() {
    
    private lateinit var toolbar: Toolbar
    private lateinit var lessonTitleTextView: TextView
    private lateinit var lessonDescriptionTextView: TextView
    private lateinit var lessonContentWebView: WebView
    private lateinit var takeQuizButton: Button
    private lateinit var completeLessonButton: Button
    
    private var currentLesson: Lesson? = null
    private var currentQuiz: Quiz? = null
    
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
    companion object {
        const val EXTRA_LESSON_ID = "lesson_id"
        const val EXTRA_LESSON_TITLE = "lesson_title"
        const val EXTRA_ROADMAP_ID = "roadmap_id"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        
        // קבלת נתוני השיעור
        val lessonId = intent.getStringExtra(EXTRA_LESSON_ID) ?: ""
        val lessonTitle = intent.getStringExtra(EXTRA_LESSON_TITLE) ?: ""
        val roadmapId = intent.getStringExtra(EXTRA_ROADMAP_ID) ?: ""
        
        // אתחול רכיבים
        toolbar = findViewById(R.id.toolbar)
        lessonTitleTextView = findViewById(R.id.lessonTitle)
        lessonDescriptionTextView = findViewById(R.id.lessonDescription)
        lessonContentWebView = findViewById(R.id.lessonContentWebView)
        takeQuizButton = findViewById(R.id.takeQuizButton)
        completeLessonButton = findViewById(R.id.completeLessonButton)
        
        // הגדרת הסרגל העליון
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = lessonTitle
        
        // טעינת תוכן השיעור
        loadLessonContent(lessonId, roadmapId)
        
        // הגדרת מאזיני לחיצה
        setupClickListeners()
    }
    
    private fun loadLessonContent(lessonId: String, roadmapId: String) {
        // מציאת השיעור המתאים
        val lessons = LearningContentManager.getLessonsForRoadmap(roadmapId)
        currentLesson = lessons.find { it.id == lessonId }
        
        currentLesson?.let { lesson ->
            lessonTitleTextView.text = lesson.title
            lessonDescriptionTextView.text = lesson.description
            
            // טעינת תוכן HTML ללא וידאו
            val htmlContent = """
                <!DOCTYPE html>
                <html dir="rtl">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            padding: 16px;
                            background-color: #f8f9fa;
                        }
                        h1, h2, h3 {
                            color: #2c3e50;
                            margin-top: 24px;
                            margin-bottom: 12px;
                        }
                        p {
                            margin-bottom: 16px;
                        }
                        ul, ol {
                            margin-bottom: 16px;
                            padding-left: 20px;
                        }
                        li {
                            margin-bottom: 8px;
                        }
                        code {
                            background-color: #f1f3f4;
                            padding: 2px 6px;
                            border-radius: 4px;
                            font-family: 'Courier New', monospace;
                            font-size: 14px;
                        }
                        pre {
                            background-color: #f8f9fa;
                            border: 1px solid #e9ecef;
                            border-radius: 8px;
                            padding: 16px;
                            overflow-x: auto;
                            margin: 16px 0;
                        }
                        pre code {
                            background: none;
                            padding: 0;
                        }
                        .highlight {
                            background-color: #fff3cd;
                            padding: 8px 12px;
                            border-radius: 4px;
                            border-left: 4px solid #ffc107;
                            margin: 16px 0;
                        }
                    </style>
                </head>
                <body>
                    ${lesson.content}
                </body>
                </html>
            """.trimIndent()
            
            lessonContentWebView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            
            // בדיקה אם יש קוויז לשיעור
            currentQuiz = LearningContentManager.getQuizForLesson(lessonId)
            takeQuizButton.visibility = if (currentQuiz != null) Button.VISIBLE else Button.GONE
            
            // הצגת כפתור הוידאו אם קיים
            if (lesson.videoUrl != null) {
                addVideoButton(lesson.videoUrl)
            }
        }
    }
    
    private fun setupClickListeners() {
        takeQuizButton.setOnClickListener {
            currentQuiz?.let { quiz ->
                val intent = Intent(this, QuizActivity::class.java).apply {
                    putExtra(QuizActivity.EXTRA_QUIZ_ID, quiz.id)
                    putExtra(QuizActivity.EXTRA_QUIZ_TITLE, quiz.title)
                    putExtra(QuizActivity.EXTRA_LESSON_ID, currentLesson?.id)
                }
                startActivityForResult(intent, 1001)
            }
        }

        completeLessonButton.setOnClickListener {
            currentLesson?.let { lesson ->
                // Mark lesson as completed
                lesson.isCompleted = true
                
                // Update button text and disable it
                updateCompletionButtonState(true)
                
                // Show success message
                Toast.makeText(this, "השיעור הושלם בהצלחה! 🎉", Toast.LENGTH_SHORT).show()
                
                // Update the lesson status in the adapter if we're going back
                setResult(RESULT_OK)
                
                // Save to Firebase
                saveLessonCompletion(lesson.id)
            }
        }
    }
    
    private fun updateCompletionButtonState(isCompleted: Boolean) {
        if (isCompleted) {
            completeLessonButton.text = "הושלם ✓"
            completeLessonButton.isEnabled = true
            completeLessonButton.setBackgroundColor(getColor(R.color.status_completed))
            completeLessonButton.setOnClickListener {
                // Allow user to redo the lesson
                currentLesson?.let { lesson ->
                    lesson.isCompleted = false
                    updateCompletionButtonState(false)
                    Toast.makeText(this, "עכשיו תוכל לבצע את השיעור שוב", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            completeLessonButton.text = "השלם שיעור"
            completeLessonButton.isEnabled = true
            completeLessonButton.setBackgroundColor(getColor(R.color.primary))
            completeLessonButton.setOnClickListener {
                currentLesson?.let { lesson ->
                    lesson.isCompleted = true
                    updateCompletionButtonState(true)
                    Toast.makeText(this, "השיעור הושלם בהצלחה! 🎉", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    saveLessonCompletion(lesson.id)
                }
            }
        }
    }
    
    private fun saveLessonCompletion(lessonId: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Update user's completed lessons count
            firestore.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    val currentCompleted = document?.getLong("completedLessons") ?: 0L
                    firestore.collection("users").document(currentUser.uid)
                        .update("completedLessons", currentCompleted + 1)
                        .addOnSuccessListener {
                            Toast.makeText(this, "התקדמות נשמרה! 💾", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "שגיאה בשמירת ההתקדמות", Toast.LENGTH_SHORT).show()
                        }
                }
        }
    }
    
    private fun addVideoButton(videoUrl: String) {
        // יצירת כפתור וידאו
        val videoButton = Button(this).apply {
            text = "🎥 צפה בוידאו שיעור"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 0)
            }
            setOnClickListener {
                openVideo(videoUrl)
            }
        }
        
        // הוספת הכפתור מתחת לכפתור "השלם שיעור"
        val buttonContainer = findViewById<LinearLayout>(R.id.buttonContainer)
        buttonContainer.addView(videoButton)
    }
    
    private fun openVideo(videoUrl: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(videoUrl))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "לא ניתן לפתוח את הוידאו", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        // Handle quiz completion result
        if (requestCode == 1001 && resultCode == QuizActivity.RESULT_QUIZ_PASSED) {
            // Quiz was passed, mark lesson as completed
            currentLesson?.let { lesson ->
                lesson.isCompleted = true
                updateCompletionButtonState(true)
                Toast.makeText(this, "כל הכבוד! השלמת את הקוויז והשיעור! 🎓", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 