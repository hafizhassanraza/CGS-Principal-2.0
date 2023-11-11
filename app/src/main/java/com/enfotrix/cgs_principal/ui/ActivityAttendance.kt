package com.enfotrix.cgs_principal.ui

import ClassesListAdapter
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.FragmentAbsent
import com.enfotrix.cgs_principal.FragmentLeave
import com.enfotrix.cgs_principal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.ClassViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.Utils
import com.enfotrix.cgs_principal.databinding.ActivityAttendanceBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class ActivityAttendance : AppCompatActivity() {
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private val classViewModel: ClassViewModel by viewModels()
    private lateinit var utils: Utils

    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var recyclerView: RecyclerView
    private var attendanceList: List<AttendenceModel> = emptyList()

    private lateinit var classAdapter: ClassesListAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityAttendance
        sharedPrefManager = SharedPrefManager(mContext)
        utils = Utils(mContext)
        getAttendance(getCurrentDate())


        binding.FAttendance.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FragmentAttendance())
                .commit()
            binding.cvAttendance.visibility=View.VISIBLE
            binding.cvAbsents.visibility=View.GONE
            binding.cvLeave.visibility=View.GONE

            binding.FAttendance.visibility=View.GONE
            binding.FAbsent.visibility=View.VISIBLE
            binding.FLeave.visibility=View.VISIBLE
        }


        binding.FAbsent.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FragmentAbsent())
                .commit()
            binding.cvAttendance.visibility=View.GONE
            binding.cvAbsents.visibility=View.VISIBLE
            binding.cvLeave.visibility=View.GONE

            binding.FAttendance.visibility=View.VISIBLE
            binding.FAbsent.visibility=View.GONE
            binding.FLeave.visibility=View.VISIBLE
        }



        binding.FLeave.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FragmentLeave())
                .commit()
            binding.cvAttendance.visibility=View.GONE
            binding.cvAbsents.visibility=View.GONE
            binding.cvLeave.visibility=View.VISIBLE

            binding.FAttendance.visibility=View.VISIBLE
            binding.FAbsent.visibility=View.VISIBLE
            binding.FLeave.visibility=View.GONE
        }


        binding.datePicker.setOnClickListener {
            showDatePickerDialog()
        }
        binding.iBA.setOnClickListener {
            super.onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAttendance(date: String) {
        utils.startLoadingAnimation()
        lifecycleScope.launch {
            attendanceViewModel.getAttendanceRec(date)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        utils.endLoadingAnimation()
                        attendanceList =
                            task.result.map { it.toObject(AttendenceModel::class.java) }

                        sharedPrefManager.putAttendanceListByDate(attendanceList)


                        if (attendanceList.isNotEmpty()) {
                                         
                            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

                            if (currentFragment is FragmentAttendance) {
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainer, FragmentAttendance())
                                    .commit()
                            } else if (currentFragment is FragmentAbsent) {
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainer, FragmentAbsent())
                                    .commit()
                            }
                            else if (currentFragment is FragmentLeave) {
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainer, FragmentLeave())
                                    .commit()
                            }
                            else{
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainer, FragmentAttendance())
                                    .commit()
                            }

                        }

                        val counterPresent = attendanceList.count { it.Status == "Present" }
                        val counterAbsent = attendanceList.count { it.Status == "Absent" }
                        val counterLeave = attendanceList.count { it.Status == "Leave" }
                        val total = counterPresent + counterAbsent + counterLeave
                        if (total > 0) {
                            val percent = (counterPresent.toFloat() / total) * 100
                            binding.tvAttendanceHeader.text =
                                "Attendance Percentage: %.2f%%".format(percent)
                        }
                        binding.studentsPresent.text = counterPresent.toString()
                        binding.studentsAbsent.text = counterAbsent.toString()
                        binding.studentsLeave.text = counterLeave.toString()

                        utils.endLoadingAnimation()
                    }
                }
                .addOnFailureListener {
                    // Handle the failure case
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val todayYear = calendar.get(Calendar.YEAR)
        val todayMonth = calendar.get(Calendar.MONTH)
        val todayDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            { datePicker, selectedYear, selectedMonth, selectedDay ->
                val selectedDateCalendar = Calendar.getInstance()
                selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay)
                val selectedDateFormatted =
                    SimpleDateFormat(
                        "dd-MM-yyyy",
                        Locale.getDefault()
                    ).format(selectedDateCalendar.time)
                binding.datePicker.setText(selectedDateFormatted)
                getAttendance(selectedDateFormatted.toString())
            },
            todayYear,
            todayMonth,
            todayDayOfMonth
        )
        datePickerDialog.show()
    }

}