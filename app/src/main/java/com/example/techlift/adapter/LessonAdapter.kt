package com.example.techlift.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techlift.R
import com.example.techlift.model.Lesson
import com.google.android.material.card.MaterialCardView

/**
 * מתאם להצגת רשימת שיעורים ברשימה מגוללת
 *
 * @property lessons רשימת השיעורים להצגה
 * @property onLessonClickListener ממשק להאזנה ללחיצות על שיעור
 */
class LessonAdapter(
    private var lessons: List<Lesson>,
    private val onLessonClickListener: OnLessonClickListener
) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    /**
     * ממשק להאזנה ללחיצות על פריטים ברשימה
     */
    interface OnLessonClickListener {
        /**
         * מופעל כאשר משתמש לוחץ על שיעור ברשימה
         * 
         * @param lesson השיעור שנלחץ
         * @param position מיקום השיעור ברשימה
         */
        fun onLessonClick(lesson: Lesson, position: Int)
    }

    /**
     * מחזיק תצוגה (ViewHolder) לפריט שיעור בודד ברשימה
     */
    inner class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.lessonCard)
        val titleTextView: TextView = itemView.findViewById(R.id.lessonTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.lessonDescription)
        val orderTextView: TextView = itemView.findViewById(R.id.lessonOrder)
        val statusTextView: TextView = itemView.findViewById(R.id.lessonStatus)

        init {
            // הגדרת מאזין לחיצה על הפריט
            cardView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onLessonClickListener.onLessonClick(lessons[position], position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.titleTextView.text = lesson.title
        holder.descriptionTextView.text = lesson.description
        holder.orderTextView.text = "שיעור ${lesson.order}"
        
        // Hide status text view - no need to show completion status
        holder.statusTextView.visibility = View.GONE
    }

    override fun getItemCount(): Int = lessons.size
    
    fun updateLessons(newLessons: List<Lesson>) {
        lessons = newLessons
        notifyDataSetChanged()
    }
} 