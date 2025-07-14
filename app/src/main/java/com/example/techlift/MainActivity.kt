package com.example.techlift

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.techlift.ui.fragment.HomeFragment
import com.example.techlift.ui.fragment.LearnFragment
import com.example.techlift.ui.fragment.ProfileFragment
import com.example.techlift.ui.fragment.RoadmapsFragment
import com.example.techlift.util.FirebaseHelper
import com.example.techlift.util.FirebaseTest
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavigation: BottomNavigationView
    private val firebaseHelper = FirebaseHelper.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        try {
            // Initialize views
            toolbar = findViewById(R.id.toolbar)
            bottomNavigation = findViewById(R.id.bottomNavigation)
            
            // Set up toolbar
            setSupportActionBar(toolbar)
            
            // Set up bottom navigation
            setupBottomNavigation()
            
            // Set default fragment
            if (savedInstanceState == null) {
                loadFragment(HomeFragment())
                bottomNavigation.selectedItemId = R.id.navigation_home
            }
        } catch (e: Exception) {
            // Handle any exceptions during initialization
            Toast.makeText(this, "Error initializing app: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_test_firebase -> {
                testFirebaseConnectivity()
                true
            }
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun testFirebaseConnectivity() {
        lifecycleScope.launch {
            FirebaseTest.testFirebaseConnectivity(this@MainActivity)
        }
    }
    
    private fun logout() {
        firebaseHelper.signOut()
        finish()
        // Redirect to login activity
        startActivity(android.content.Intent(this, LoginActivity::class.java))
    }
    
    private fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_roadmaps -> {
                    loadFragment(RoadmapsFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_learn -> {
                    loadFragment(LearnFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment())
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
    }
    
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}