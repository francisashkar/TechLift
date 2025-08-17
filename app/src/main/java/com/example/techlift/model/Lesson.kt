package com.example.techlift.model

/**
 * מחלקה המייצגת שיעור למידה במסלול
 *
 * @property id מזהה ייחודי של השיעור
 * @property roadmapId מזהה המסלול אליו שייך השיעור
 * @property title כותרת השיעור
 * @property description תיאור השיעור
 * @property content תוכן השיעור (HTML או טקסט)
 * @property videoUrl קישור לוידאו (אופציונלי)
 * @property duration משך השיעור בדקות
 * @property order סדר השיעור במסלול
 * @property isCompleted האם השיעור הושלם
 * @property quizId מזהה הקוויז הקשור לשיעור (אופציונלי)
 */
data class Lesson(
    val id: String,
    val roadmapId: String,
    val title: String,
    val description: String,
    val content: String,
    val videoUrl: String? = null,
    val duration: Int,
    val order: Int,
    var isCompleted: Boolean = false,
    val quizId: String? = null
) 