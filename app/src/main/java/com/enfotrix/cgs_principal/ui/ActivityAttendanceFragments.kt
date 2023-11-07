package com.enfotrix.cgs_principal.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.enfotrix.cgs_principal.R

class ActivityAttendanceFragments : AppCompatActivity() {
    private lateinit var binding:ActivityAttendanceFragments
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding =ActivityAttendanceFragments.inflate(layoutInflater)
//        setContentView(binding.root)
        val Attendance = findViewById<TextView>(R.id.FAttendance)
        val Present = findViewById<TextView>(R.id.FPresent)
        val Absent = findViewById<TextView>(R.id.FAbsent)
        val Leave = findViewById<TextView>(R.id.FLeave)
        Attendance.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AttendanceFragment())
                .commit()
        }

        Present.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PresentFragment())
                .commit()
        }

        Absent.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AbsentFragment())
                .commit()
        }

        Leave.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LeaveFragment())
                .commit()
        }
    }
    }
}