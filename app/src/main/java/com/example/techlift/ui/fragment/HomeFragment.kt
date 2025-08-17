package com.example.techlift.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.techlift.R
import com.example.techlift.adapter.RoadmapAdapter
import com.example.techlift.data.LearningContentManager
import com.example.techlift.model.Roadmap
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var welcomeTextView: TextView
    private lateinit var statsCard: MaterialCardView
    private lateinit var totalLessonsTextView: TextView
    private lateinit var completedLessonsTextView: TextView
    private lateinit var progressPercentageTextView: TextView
    private lateinit var currentStreakTextView: TextView
    private lateinit var roadmapsCard: MaterialCardView
    private lateinit var roadmapsRecyclerView: RecyclerView
    private lateinit var progressCard: MaterialCardView
    private lateinit var learningProgressBar: ProgressBar
    private lateinit var progressDescriptionTextView: TextView

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupUserStats()
        setupRoadmapsList()
        setupClickListeners()
    }

    private fun initializeViews(view: View) {
        welcomeTextView = view.findViewById(R.id.welcomeTextView)
        statsCard = view.findViewById(R.id.statsCard)
        totalLessonsTextView = view.findViewById(R.id.totalLessonsTextView)
        completedLessonsTextView = view.findViewById(R.id.completedLessonsTextView)
        progressPercentageTextView = view.findViewById(R.id.progressPercentageTextView)
        currentStreakTextView = view.findViewById(R.id.currentStreakTextView)
        roadmapsCard = view.findViewById(R.id.roadmapsCard)
        roadmapsRecyclerView = view.findViewById(R.id.roadmapsRecyclerView)
        progressCard = view.findViewById(R.id.progressCard)
        learningProgressBar = view.findViewById(R.id.learningProgressBar)
        progressDescriptionTextView = view.findViewById(R.id.progressDescriptionTextView)
    }

    private fun setupUserStats() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Set welcome message
            welcomeTextView.text = "שלום ${currentUser.displayName ?: "משתמש"}! 👋"
            
            // Load user stats from Firestore
            loadUserStats(currentUser.uid)
        } else {
            welcomeTextView.text = "ברוכים הבאים ל-TechLift! 🚀"
        }
    }

    private fun loadUserStats(userId: String) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val completedLessons = document.getLong("completedLessons") ?: 0L
                    val currentStreak = document.getLong("currentStreak") ?: 0L
                    val lastLoginDate = document.getTimestamp("lastLoginAt")
                    
                    updateStatsDisplay(completedLessons.toInt(), currentStreak.toInt(), lastLoginDate)
                } else {
                    // New user - show default stats
                    updateStatsDisplay(0, 0, null)
                }
            }
            .addOnFailureListener {
                // Fallback to default stats
                updateStatsDisplay(0, 0, null)
            }
    }

    private fun updateStatsDisplay(completedLessons: Int, currentStreak: Int, lastLoginDate: com.google.firebase.Timestamp?) {
        // Calculate total lessons across all roadmaps
        val totalLessons = LearningContentManager.getAllLessons().size
        
        // Calculate progress percentage
        val progressPercentage = if (totalLessons > 0) {
            (completedLessons * 100) / totalLessons
        } else 0

        // Update UI
        totalLessonsTextView.text = totalLessons.toString()
        completedLessonsTextView.text = completedLessons.toString()
        progressPercentageTextView.text = "$progressPercentage%"
        currentStreakTextView.text = "$currentStreak ימים"
        
        // Update progress bar
        learningProgressBar.progress = progressPercentage
        progressDescriptionTextView.text = "השלמת $completedLessons מתוך $totalLessons שיעורים"

        // Update streak based on last login
        updateStreakIfNeeded(lastLoginDate)
    }

    private fun updateStreakIfNeeded(lastLoginDate: com.google.firebase.Timestamp?) {
        val currentUser = auth.currentUser ?: return
        
        if (lastLoginDate != null) {
            val lastLogin = lastLoginDate.toDate()
            val today = java.util.Date()
            val diffInMillis = today.time - lastLogin.time
            val diffInDays = diffInMillis / (24 * 60 * 60 * 1000)
            
            // If user logged in today, increment streak
            if (diffInDays == 0L) {
                // User already logged in today, don't change streak
                return
            } else if (diffInDays == 1L) {
                // User logged in yesterday, increment streak
                val currentStreak = currentStreakTextView.text.toString().split(" ")[0].toIntOrNull() ?: 0
                currentStreakTextView.text = "${currentStreak + 1} ימים"
                
                // Update in Firestore
                firestore.collection("users").document(currentUser.uid)
                    .update("currentStreak", currentStreak + 1)
            } else if (diffInDays > 1L) {
                // Streak broken, reset to 1
                currentStreakTextView.text = "1 ימים"
                firestore.collection("users").document(currentUser.uid)
                    .update("currentStreak", 1)
            }
        }
        
        // Update last login
        firestore.collection("users").document(currentUser.uid)
            .update("lastLoginAt", com.google.firebase.Timestamp.now())
    }

    private fun setupRoadmapsList() {
        val roadmaps = listOf(
            Roadmap("frontend", "פיתוח צד לקוח", "HTML, CSS, JavaScript, React", 30),
            Roadmap("backend", "פיתוח צד שרת", "Node.js, Python, Java, APIs", 45),
            Roadmap("ai", "בינה מלאכותית", "Python, TensorFlow, ML Algorithms", 10)
        )
        
        val adapter = RoadmapAdapter(roadmaps, object : RoadmapAdapter.OnRoadmapClickListener {
            override fun onRoadmapClick(roadmap: Roadmap, position: Int) {
                // Navigate to roadmap detail
                findNavController().navigate(R.id.action_homeFragment_to_roadmapsFragment)
            }
        })
        
        roadmapsRecyclerView.adapter = adapter
    }

    private fun setupClickListeners() {
        // No click listeners needed after removing the viewPostsButton
    }
} 