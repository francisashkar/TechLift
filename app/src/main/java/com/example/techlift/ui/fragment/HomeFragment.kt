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
        
        // Safety check - ensure Fragment is attached and has a view
        if (!isAdded) return
        
        initializeViews(view)
        setupUserStats()
        setupRoadmapsList()
        setupClickListeners()
        
        // Log to verify setup
        android.util.Log.d("HomeFragment", "HomeFragment setup completed")
    }
    
    override fun onResume() {
        super.onResume()
        // Don't reload courses to prevent duplicates
        // setupRoadmapsList() - REMOVED to prevent duplicates
        android.util.Log.d("HomeFragment", "HomeFragment onResume - courses not reloaded to prevent duplicates")
    }

    private fun initializeViews(view: View) {
        // Safety check - ensure Fragment is attached and has a view
        if (!isAdded || this.view == null) return
        
        welcomeTextView = view.findViewById(R.id.welcomeTextView)
        statsCard = view.findViewById(R.id.statsCard)
        totalLessonsTextView = view.findViewById(R.id.totalLessonsTextView)
        completedLessonsTextView = view.findViewById(R.id.completedLessonsTextView)
        progressPercentageTextView = view.findViewById(R.id.progressPercentageTextView)
        currentStreakTextView = view.findViewById(R.id.currentStreakTextView)
        roadmapsCard = view.findViewById(R.id.roadmapsCard)
        progressCard = view.findViewById(R.id.progressCard)
        learningProgressBar = view.findViewById(R.id.learningProgressBar)
        progressDescriptionTextView = view.findViewById(R.id.progressDescriptionTextView)
    }

    private fun setupUserStats() {
        // Safety check - ensure Fragment is attached and has a view
        if (!isAdded || view == null) return
        
        val currentUser = FirebaseAuth.getInstance().currentUser
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
        // Safety check - ensure Fragment is attached and has a view
        if (!isAdded || view == null) return
        
        FirebaseFirestore.getInstance().collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (isAdded && view != null) {
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
            }
            .addOnFailureListener {
                // Fallback to default stats
                if (isAdded && view != null) {
                    updateStatsDisplay(0, 0, null)
                }
            }
    }

    private fun updateStatsDisplay(completedLessons: Int, currentStreak: Int, lastLoginDate: com.google.firebase.Timestamp?) {
        // Safety check - ensure Fragment is attached and has a view
        if (!isAdded || view == null) return
        
        // Calculate total courses (current + completed)
        val currentCourses = 1 // User has one current course
        val totalCourses = currentCourses + completedLessons
        
        // Calculate progress percentage based on completed courses
        val progressPercentage = if (totalCourses > 0) {
            (completedLessons * 100) / totalCourses
        } else 0

        // Update UI with course statistics
        totalLessonsTextView.text = currentCourses.toString()
        completedLessonsTextView.text = completedLessons.toString()
        progressPercentageTextView.text = "$progressPercentage%"
        currentStreakTextView.text = "$currentStreak ימים"
        
        // Update progress bar
        learningProgressBar.progress = progressPercentage
        progressDescriptionTextView.text = "השלמת $completedLessons מתוך $totalCourses קורסים"

        // Update streak based on last login
        updateStreakIfNeeded(lastLoginDate)
    }

    private fun updateStreakIfNeeded(lastLoginDate: com.google.firebase.Timestamp?) {
        // Safety check - ensure Fragment is attached and has a view
        if (!isAdded || view == null) return
        
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        
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
                FirebaseFirestore.getInstance().collection("users").document(currentUser.uid)
                    .update("currentStreak", currentStreak + 1)
            } else if (diffInDays > 1L) {
                // Streak broken, reset to 1
                currentStreakTextView.text = "1 ימים"
                FirebaseFirestore.getInstance().collection("users").document(currentUser.uid)
                    .update("currentStreak", 1)
            }
        }
        
        // Update last login
        FirebaseFirestore.getInstance().collection("users").document(currentUser.uid)
            .update("lastLoginAt", com.google.firebase.Timestamp.now())
    }

    private fun setupRoadmapsList() {
        // Safety check - ensure Fragment is attached and has a view
        if (!isAdded || view == null) return
        
        // Don't show multiple courses - only show the user's current course
        // showDefaultRoadmaps() - REMOVED to prevent duplicates
        // loadCompletedCourses() - REMOVED to prevent duplicates
        
        // Get current user's course progress if logged in
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            loadUserCurrentCourse()
        }
    }
    
    private fun loadUserCurrentCourse() {
        // Safety check - ensure Fragment is attached and has a view
        if (!isAdded || view == null) return
        
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (isAdded && view != null) {
                        if (document != null && document.exists()) {
                            val currentCourseId = document.getString("currentCourseId") ?: "frontend"
                            val defaultCourse = getCurrentCourse(currentCourseId, 0)
                            defaultCourse?.let { course ->
                                view?.findViewById<TextView>(R.id.currentCourseTitle)?.text = course.title
                                view?.findViewById<TextView>(R.id.currentCourseDescription)?.text = course.description
                                view?.findViewById<TextView>(R.id.currentCourseStatus)?.text = "לא הושלם"
                                view?.findViewById<TextView>(R.id.currentCourseStatus)?.setTextColor(requireContext().getColor(R.color.status_in_progress))
                            }
                        } else {
                            // Show default course if no user data
                            val defaultCourse = getCurrentCourse("frontend", 0)
                            defaultCourse?.let { course ->
                                view?.findViewById<TextView>(R.id.currentCourseTitle)?.text = course.title
                                view?.findViewById<TextView>(R.id.currentCourseDescription)?.text = course.description
                                view?.findViewById<TextView>(R.id.currentCourseStatus)?.text = "לא הושלם"
                                view?.findViewById<TextView>(R.id.currentCourseStatus)?.setTextColor(requireContext().getColor(R.color.status_in_progress))
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    // Show default course on failure
                    if (isAdded && view != null) {
                        val defaultCourse = getCurrentCourse("frontend", 0)
                        defaultCourse?.let { course ->
                            view?.findViewById<TextView>(R.id.currentCourseTitle)?.text = course.title
                            view?.findViewById<TextView>(R.id.currentCourseDescription)?.text = course.description
                            view?.findViewById<TextView>(R.id.currentCourseStatus)?.text = "לא הושלם"
                            view?.findViewById<TextView>(R.id.currentCourseStatus)?.setTextColor(requireContext().getColor(R.color.status_in_progress))
                        }
                    }
                }
        } else {
            // Show default course if no user is logged in
            if (isAdded && view != null) {
                val defaultCourse = getCurrentCourse("frontend", 0)
                defaultCourse?.let { course ->
                    view?.findViewById<TextView>(R.id.currentCourseTitle)?.text = course.title
                    view?.findViewById<TextView>(R.id.currentCourseDescription)?.text = course.description
                    view?.findViewById<TextView>(R.id.currentCourseStatus)?.text = "לא הושלם"
                    view?.findViewById<TextView>(R.id.currentCourseStatus)?.setTextColor(requireContext().getColor(R.color.status_in_progress))
                }
            }
        }
    }
    

    
    private fun getCurrentCourse(courseId: String, progress: Int): Roadmap? {
        // Safety check - ensure Fragment is attached and has a view
        if (!isAdded || view == null) return null
        
        return when (courseId) {
            "frontend" -> Roadmap("frontend", "קורס פיתוח צד לקוח", "HTML, CSS, JavaScript, React - קורס נוכחי", 0)
            "backend" -> Roadmap("backend", "קורס פיתוח צד שרת", "Node.js, Python, Java, APIs - קורס נוכחי", 0)
            "mobile" -> Roadmap("mobile", "קורס פיתוח מובייל", "React Native, Flutter - קורס נוכחי", 0)
            "devops" -> Roadmap("devops", "קורס DevOps והנדסת תשתיות", "Docker, Kubernetes, CI/CD - קורס נוכחי", 0)
            "ai" -> Roadmap("ai", "קורס בינה מלאכותית", "Python, TensorFlow, ML Algorithms - קורס נוכחי", 0)
            else -> Roadmap("frontend", "קורס פיתוח צד לקוח", "HTML, CSS, JavaScript, React - קורס נוכחי", 0)
        }
    }
    
    private fun setupClickListeners() {
        // Safety check - ensure Fragment is attached and has a view
        if (!isAdded || view == null) return
        
        // No click listeners needed after removing the viewPostsButton
    }
} 