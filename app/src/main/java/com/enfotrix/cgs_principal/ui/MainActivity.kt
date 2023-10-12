package com.enfotrix.cgs_principal.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.Attendance.setOnClickListener {
            // Define the action you want to perform when the button is clicked.
            // For example, you can start a new activity here.
            val intent = Intent(this, ActivityClasses::class.java)
            startActivity(intent)
        }
        binding.Results.setOnClickListener {
            // Define the action you want to perform when the button is clicked.
            // For example, you can start a new activity here.
            val intent = Intent(this, ActivityClasses::class.java)
            startActivity(intent)
        }
    }
}