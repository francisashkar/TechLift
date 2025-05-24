package com.example.techlift

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

class SplashActivity : AppCompatActivity() {
    private lateinit var splashLogo: ImageView
    private lateinit var appNameText: TextView
    private lateinit var sloganText: TextView
    private lateinit var progressBar: ProgressBar
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        // אתחול הרכיבים
        splashLogo = findViewById(R.id.splashLogo)
        appNameText = findViewById(R.id.appNameText)
        sloganText = findViewById(R.id.sloganText)
        progressBar = findViewById(R.id.progressBar)
        
        // השהייה של 2 שניות לפני מעבר למסך הבא
        Handler(Looper.getMainLooper()).postDelayed({
            // כרגע נעבור למסך הראשי, אחר כך נשנה למסך ההתחברות
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // סגירת ה-Activity הנוכחי כדי שלא יוכלו לחזור אליו
        }, 2000)
    }
} 