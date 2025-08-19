package com.example.techlift.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techlift.R
import com.example.techlift.model.Roadmap
import com.google.android.material.progressindicator.LinearProgressIndicator

/**
 * מתאם להצגת רשימת מסלולי למידה ברשימה מגוללת
 *
 * @property roadmaps רשימת מסלולי הלמידה להצגה
 * @property onRoadmapClickListener ממשק להאזנה ללחיצות על מסלול
 */
class RoadmapAdapter(
    private var roadmaps: List<Roadmap>,
    private val onRoadmapClickListener: OnRoadmapClickListener
) : RecyclerView.Adapter<RoadmapAdapter.RoadmapViewHolder>() {

    /**
     * ממשק להאזנה ללחיצות על פריטים ברשימה
     */
    interface OnRoadmapClickListener {
        /**
         * מופעל כאשר משתמש לוחץ על מסלול ברשימה
         * 
         * @param roadmap המסלול שנלחץ
         * @param position מיקום המסלול ברשימה
         */
        fun onRoadmapClick(roadmap: Roadmap, position: Int)
    }

    /**
     * מחזיק תצוגה (ViewHolder) לפריט מסלול בודד ברשימה
     */
    inner class RoadmapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.roadmapTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.roadmapDescription)
        val progressIndicator: LinearProgressIndicator = itemView.findViewById(R.id.progressIndicator)
        val cardView: View = itemView.findViewById(R.id.roadmapCard)

        init {
            // הגדרת מאזין לחיצה על הפריט
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onRoadmapClickListener.onRoadmapClick(roadmaps[position], position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoadmapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_roadmap, parent, false)
        return RoadmapViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoadmapViewHolder, position: Int) {
        val roadmap = roadmaps[position]
        
        holder.titleTextView.text = roadmap.title
        holder.descriptionTextView.text = roadmap.description
        
        // Set click listener for the card (to navigate to course details)
        holder.cardView.setOnClickListener {
            onRoadmapClickListener.onRoadmapClick(roadmap, position)
        }
    }

    override fun getItemCount(): Int = roadmaps.size
    
    fun updateRoadmaps(newRoadmaps: List<Roadmap>) {
        roadmaps = newRoadmaps
        notifyDataSetChanged()
    }
} 