package com.example.techlift

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    // נתוני משתמש לדוגמה (בדמו אמיתי זה יגיע ממסד נתונים או העדפות)
    private val mockUserName = "ישראל ישראלי"

    // הגדרת רכיבי הממשק
    private lateinit var toolbar: Toolbar
    private lateinit var welcomeUserText: TextView
    private lateinit var dailyChallengeCard: MaterialCardView
    private lateinit var roadmapsCard: MaterialCardView
    private lateinit var practiceCard: MaterialCardView
    private lateinit var interviewsCard: MaterialCardView
    private lateinit var projectsCard: MaterialCardView
    private lateinit var communityCard: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // אתחול הרכיבים
        toolbar = findViewById(R.id.toolbar)
        welcomeUserText = findViewById(R.id.welcomeUserText)
        dailyChallengeCard = findViewById(R.id.dailyChallengeCard)
        roadmapsCard = findViewById(R.id.roadmapsCard)
        practiceCard = findViewById(R.id.practiceCard)
        interviewsCard = findViewById(R.id.interviewsCard)
        projectsCard = findViewById(R.id.projectsCard)
        communityCard = findViewById(R.id.communityCard)

        // הגדרת הסרגל העליון
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.dashboard)

        // הגדרת טקסט הברכה
        welcomeUserText.text = getString(R.string.hello_user, mockUserName)

        // הגדרת פעולות הקלקה על הכרטיסיות
        setupCardClickListeners()
    }

    private fun setupCardClickListeners() {
        // אתגר יומי
        dailyChallengeCard.setOnClickListener {
            showFeatureNotAvailableMessage(getString(R.string.daily_challenge))
        }

        // מפות דרכים
        roadmapsCard.setOnClickListener {
            // מעבר למסך מפות הדרכים
            val intent = Intent(this, RoadmapsActivity::class.java)
            startActivity(intent)
        }

        // סביבת תרגול
        practiceCard.setOnClickListener {
            showFeatureNotAvailableMessage(getString(R.string.practice))
        }

        // שאלות ראיון
        interviewsCard.setOnClickListener {
            showFeatureNotAvailableMessage(getString(R.string.interviews))
        }

        // פרויקטים מוצעים
        projectsCard.setOnClickListener {
            showFeatureNotAvailableMessage(getString(R.string.projects))
        }

        // קהילה
        communityCard.setOnClickListener {
            showFeatureNotAvailableMessage(getString(R.string.community))
        }
    }

    private fun showFeatureNotAvailableMessage(featureName: String) {
        // הודעה שהתכונה אינה זמינה בדמו
        Toast.makeText(
            this,
            "התכונה '$featureName' אינה זמינה בגרסת הדמו",
            Toast.LENGTH_SHORT
        ).show()
    }
}