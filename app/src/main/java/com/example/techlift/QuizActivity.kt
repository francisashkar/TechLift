package com.example.techlift

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.techlift.data.LearningContentManager
import com.example.techlift.model.Lesson
import com.example.techlift.model.Quiz
import com.example.techlift.model.QuizQuestion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class QuizActivity : AppCompatActivity() {
    
    private lateinit var toolbar: Toolbar
    private lateinit var questionTextView: TextView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var nextButton: Button
    private lateinit var progressTextView: TextView
    
    private var currentQuiz: Quiz? = null
    private var currentQuestionIndex = 0
    private var userAnswers = mutableListOf<Int>()
    private var startTime = System.currentTimeMillis()
    private var lessonId: String = ""
    
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
    companion object {
        const val EXTRA_QUIZ_ID = "quiz_id"
        const val EXTRA_QUIZ_TITLE = "quiz_title"
        const val EXTRA_LESSON_ID = "lesson_id"
        const val RESULT_QUIZ_PASSED = 1001
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        
        // קבלת נתוני הקוויז
        val quizId = intent.getStringExtra(EXTRA_QUIZ_ID) ?: ""
        val quizTitle = intent.getStringExtra(EXTRA_QUIZ_TITLE) ?: ""
        lessonId = intent.getStringExtra(EXTRA_LESSON_ID) ?: ""
        
        // אתחול רכיבים
        toolbar = findViewById(R.id.toolbar)
        questionTextView = findViewById(R.id.questionTextView)
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup)
        nextButton = findViewById(R.id.nextButton)
        progressTextView = findViewById(R.id.progressTextView)
        
        // הגדרת הסרגל העליון
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = quizTitle
        
        // טעינת הקוויז
        loadQuiz(quizId)
        
        // הגדרת מאזיני לחיצה
        setupClickListeners()
    }
    
    private fun loadQuiz(quizId: String) {
        currentQuiz = LearningContentManager.getQuizForLesson(quizId)
        currentQuiz?.let { quiz ->
            userAnswers = MutableList(quiz.questions.size) { -1 }
            displayQuestion(0)
        }
    }
    
    private fun displayQuestion(questionIndex: Int) {
        currentQuiz?.let { quiz ->
            if (questionIndex < quiz.questions.size) {
                val question = quiz.questions[questionIndex]
                
                // עדכון התקדמות
                progressTextView.text = "שאלה ${questionIndex + 1} מתוך ${quiz.questions.size}"
                
                // הצגת השאלה
                questionTextView.text = question.questionText
                
                // ניקוי אפשרויות קודמות
                optionsRadioGroup.removeAllViews()
                
                // הוספת אפשרויות חדשות
                question.options.forEachIndexed { index, option ->
                    val radioButton = RadioButton(this).apply {
                        id = index
                        text = option
                        layoutParams = RadioGroup.LayoutParams(
                            RadioGroup.LayoutParams.MATCH_PARENT,
                            RadioGroup.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 8, 0, 8)
                        }
                    }
                    optionsRadioGroup.addView(radioButton)
                }
                
                // בחירת תשובה קודמת אם קיימת
                if (userAnswers[questionIndex] != -1) {
                    optionsRadioGroup.check(userAnswers[questionIndex])
                }
                
                // עדכון כפתור
                if (questionIndex == quiz.questions.size - 1) {
                    nextButton.text = "סיים קוויז"
                } else {
                    nextButton.text = "הבא"
                }
                
                currentQuestionIndex = questionIndex
            }
        }
    }
    
    private fun setupClickListeners() {
        nextButton.setOnClickListener {
            // שמירת התשובה הנוכחית
            val selectedId = optionsRadioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                userAnswers[currentQuestionIndex] = selectedId
            }
            
            // מעבר לשאלה הבאה או סיום הקוויז
            if (currentQuestionIndex < (currentQuiz?.questions?.size ?: 0) - 1) {
                displayQuestion(currentQuestionIndex + 1)
            } else {
                finishQuiz()
            }
        }
    }
    
    private fun finishQuiz() {
        currentQuiz?.let { quiz ->
            val timeTaken = System.currentTimeMillis() - startTime
            val correctAnswers = userAnswers.mapIndexed { index, answer ->
                if (answer == quiz.questions[index].correctAnswerIndex) 1 else 0
            }.sum()
            
            val score = (correctAnswers.toFloat() / quiz.questions.size * 100).toInt()
            val isPassed = score >= quiz.passingScore
            
            // הצגת תוצאות
            showQuizResults(quiz, score, correctAnswers, quiz.questions.size, timeTaken, isPassed)
        }
    }
    
    private fun showQuizResults(
        quiz: Quiz,
        score: Int,
        correctAnswers: Int,
        totalQuestions: Int,
        timeTaken: Long,
        isPassed: Boolean
    ) {
        val message = if (isPassed) {
            "🎉 כל הכבוד! עברת את הקוויז בהצלחה!\n\n" +
            "ציון: $score%\n" +
            "תשובות נכונות: $correctAnswers/$totalQuestions\n" +
            "זמן: ${timeTaken / 1000} שניות"
        } else {
            "😔 לא עברת הפעם. נסה שוב!\n\n" +
            "ציון: $score%\n" +
            "תשובות נכונות: $correctAnswers/$totalQuestions\n" +
            "ציון עובר: ${quiz.passingScore}%"
        }
        
        AlertDialog.Builder(this)
            .setTitle(if (isPassed) "הצלחה!" else "נסה שוב")
            .setMessage(message)
            .setPositiveButton("אישור") { _, _ ->
                if (isPassed) {
                    // Mark lesson as completed
                    markLessonAsCompleted()
                    
                    // Set result to indicate quiz was passed
                    val resultIntent = Intent()
                    resultIntent.putExtra("QUIZ_PASSED", true)
                    setResult(RESULT_QUIZ_PASSED, resultIntent)
                }
                finish()
            }
            .setCancelable(false)
            .show()
    }
    
    private fun markLessonAsCompleted() {
        if (lessonId.isNotEmpty()) {
            // Update lesson in LearningContentManager
            val lesson = LearningContentManager.getLessonById(lessonId)
            if (lesson != null) {
                lesson.isCompleted = true
                
                // Save to Firebase
                saveLessonCompletion(lessonId)
            }
        }
    }
    
    private fun saveLessonCompletion(lessonId: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Add lesson to user's completed lessons
            val lessonData = hashMapOf(
                "lessonId" to lessonId,
                "completedAt" to com.google.firebase.Timestamp.now(),
                "completedViaQuiz" to true
            )
            
            // Add to user's completed lessons collection
            firestore.collection("users").document(currentUser.uid)
                .collection("completedLessons").document(lessonId)
                .set(lessonData)
                .addOnSuccessListener {
                    // Just mark lesson as completed, no complex progress calculation
                    Toast.makeText(this, "השיעור הושלם בהצלחה!", Toast.LENGTH_SHORT).show()
                }
        }
    }
    
    private fun updateUserProgress(userId: String, completedLessonId: String) {
        // Get the lesson to determine which course it belongs to
        val lesson = LearningContentManager.getLessonById(completedLessonId)
        if (lesson != null) {
            val courseId = lesson.roadmapId
            
            // Simply update the current course ID
            firestore.collection("users").document(userId)
                .update("currentCourseId", courseId)
        }
    }
    
    private fun showCourseCompletionCelebration(courseId: String) {
        val courseName = when (courseId) {
            "frontend" -> "פיתוח צד לקוח"
            "backend" -> "פיתוח צד שרת"
            "mobile" -> "פיתוח אפליקציות מובייל"
            "devops" -> "DevOps והנדסת תשתיות"
            "ai" -> "בינה מלאכותית"
            else -> "הקורס"
        }
        
        AlertDialog.Builder(this)
            .setTitle("🎉 מזל טוב! 🎉")
            .setMessage("השלמת בהצלחה את הקורס: $courseName!\n\n" +
                    "כל השיעורים הושלמו והקוויזים עברו בהצלחה.\n" +
                    "אתה מוכן לעבור לקורס הבא!")
            .setPositiveButton("מעולה!") { _, _ ->
                // Course completed successfully
            }
            .setCancelable(false)
            .show()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 