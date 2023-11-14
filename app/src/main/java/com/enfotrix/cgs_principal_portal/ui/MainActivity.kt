package com.enfotrix.cgs_principal_portal.ui
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.enfotrix.cgs_principal_portal.Models.AnnouncementModel
import com.enfotrix.cgs_principal_portal.Models.AnnouncementViewModel
import com.enfotrix.cgs_principal_portal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal_portal.Models.AttendenceModel
import com.enfotrix.cgs_principal_portal.Models.ClassModel
import com.enfotrix.cgs_principal_portal.Models.SectionModel
import com.enfotrix.cgs_principal_portal.R
import com.enfotrix.cgs_principal_portal.SharedPrefManager
import com.enfotrix.cgs_principal_portal.Utils
import com.enfotrix.cgs_principal_portal.databinding.ActivityMainBinding
import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private val attendanceViewModel: AttendanceViewModel by viewModels()

    private val announcementViewModel: AnnouncementViewModel by viewModels()






    private lateinit var mContext: Context
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager
    private val classesList = mutableListOf<ClassModel>()
    private val sectionList = mutableListOf<SectionModel>()
    private lateinit var dialog : Dialog
    private lateinit var utils: Utils

    private lateinit var binding:ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        mContext = this@MainActivity;
        utils = Utils(mContext)
        constants = Constants()
        sharedPrefManager = SharedPrefManager(mContext)
        getannouncement()

        binding.Attendance.setOnClickListener {
            // Define the action you want to perform when the button is clicked.
            // For example, you can start a new activity here.
            val intent = Intent(this, ActivityAttendance::class.java)
            startActivity(intent)



        }
        binding.events.setOnClickListener {
            Toast.makeText(mContext, "Coming Soon", Toast.LENGTH_SHORT).show()

        }
        binding.profiles.setOnClickListener {
            Toast.makeText(mContext, "Coming Soon", Toast.LENGTH_SHORT).show()

        }
        binding.Timetable.setOnClickListener {
            Toast.makeText(mContext, "Coming Soon", Toast.LENGTH_SHORT).show()

        }
        binding.Reports.setOnClickListener {
            Toast.makeText(mContext, "Coming Soon", Toast.LENGTH_SHORT).show()

        }
        binding.Results.setOnClickListener {


            startActivity(Intent(mContext, ActivityResult::class.java)) }

        binding.cardStudentReports.setOnClickListener {
            startActivity(Intent(mContext,ActivityStudentReports::class.java))
        }

        getAttendance()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAttendance() {
        lifecycleScope.launch {
            attendanceViewModel.getAttendanceRec(getCurrentDate()).addOnCompleteListener { task->
                if(task.isSuccessful){
                    val attendanceList= task.result.map { it.toObject(AttendenceModel::class.java) }

                    var counterPresent : Int=0
                    var counterAbsent : Int
                    var counterLeave : Int

                    counterPresent = attendanceList.filter { attendance-> attendance.Status.equals("Present") }.count()
                    counterAbsent = attendanceList.filter { attendance-> attendance.Status.equals("Absent") }.count()
                    counterLeave = attendanceList.filter { attendance-> attendance.Status.equals("Leave") }.count()


                    val total = counterPresent + counterAbsent + counterLeave
                    if (total > 0) {
                        val percent = (counterPresent.toFloat() / total) * 100
                        binding.tvAttendanceHeader.text = "Today Attendance: %.2f%%".format(percent)
                    }

                    binding.studentsPresent.text= counterPresent.toString()
                    binding.studentsAbsent.text= counterAbsent.toString()
                    binding.studentsLeave.text= counterLeave.toString()
                    //binding.tvAttendanceHeader.text= "Today Attendance: " + percent

                }


            }.addOnFailureListener {
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }


    private fun getannouncement(){
        lifecycleScope.launch { announcementViewModel.getAnnouncementModel().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val documents = task.result
                for (document in documents){
                    val announcement = document.toObject(AnnouncementModel::class.java)
                    binding.tvAnnouncement.text=announcement.Announcement.toString()
                }
            }
            else{
                Toast.makeText(mContext,"Sommthing went wrong",Toast.LENGTH_SHORT).show()
            }
        } }
    }

}