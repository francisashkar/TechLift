package com.example.techlift.model

/**
 * מחלקה המייצגת מסלול למידה
 *
 * @property id מזהה ייחודי של המסלול
 * @property title כותרת המסלול
 * @property description תיאור המסלול
 * @property progress אחוז ההתקדמות במסלול
 */
data class Roadmap(
    val id: String,
    val title: String,
    val description: String,
    val progress: Int = 0
) 