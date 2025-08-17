package com.example.techlift.model

/**
 * מחלקה המייצגת קוויז לבדיקת ידע
 *
 * @property id מזהה ייחודי של הקוויז
 * @property lessonId מזהה השיעור הקשור לקוויז
 * @property title כותרת הקוויז
 * @property description תיאור הקוויז
 * @property questions רשימת השאלות בקוויז
 * @property passingScore ציון עובר (אחוז)
 * @property timeLimit מגבלת זמן בדקות (אופציונלי)
 */
data class Quiz(
    val id: String,
    val lessonId: String,
    val title: String,
    val description: String,
    val questions: List<QuizQuestion>,
    val passingScore: Int = 70,
    val timeLimit: Int? = null
)

/**
 * מחלקה המייצגת שאלה בקוויז
 *
 * @property id מזהה ייחודי של השאלה
 * @property questionText טקסט השאלה
 * @property options רשימת התשובות האפשריות
 * @property correctAnswerIndex אינדקס התשובה הנכונה
 * @property explanation הסבר לתשובה הנכונה
 */
data class QuizQuestion(
    val id: String,
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

/**
 * מחלקה המייצגת תוצאות קוויז
 *
 * @property quizId מזהה הקוויז
 * @property userId מזהה המשתמש
 * @property score הציון שהתקבל
 * @property totalQuestions מספר השאלות הכולל
 * @property correctAnswers מספר התשובות הנכונות
 * @property timeTaken זמן שלקח להשלמת הקוויז
 * @property isPassed האם עבר את הקוויז
 * @property completedAt תאריך השלמת הקוויז
 */
data class QuizResult(
    val quizId: String,
    val userId: String,
    val score: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val timeTaken: Long,
    val isPassed: Boolean,
    val completedAt: Long = System.currentTimeMillis()
) 