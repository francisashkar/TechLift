package com.example.techlift

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techlift.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // אתחול הרכיבים
        emailInputLayout = findViewById(R.id.emailInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        registerLink = findViewById(R.id.registerLink)

        // הגדרת פעולת כפתור ההתחברות
        loginButton.setOnClickListener {
            // בדיקות תקינות קלט
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            
            if (email.isEmpty()) {
                emailInputLayout.error = "יש להזין כתובת דוא\"ל"
                return@setOnClickListener
            } else {
                emailInputLayout.error = null
            }
            
            if (password.isEmpty()) {
                passwordInputLayout.error = "יש להזין סיסמה"
                return@setOnClickListener
            } else {
                passwordInputLayout.error = null
            }
            
            // לצורך הדמו, כל משתמש יכול להתחבר
            Toast.makeText(this, "מתחבר...", Toast.LENGTH_SHORT).show()
            
            // מעבר למסך הבית
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // הגדרת פעולת הקישור להרשמה
        registerLink.setOnClickListener {
            // לצורך הדמו, נשאר במסך ההתחברות
            Toast.makeText(this, "מעבר למסך הרשמה (לא זמין בדמו)", Toast.LENGTH_SHORT).show()
        }
    }
} 