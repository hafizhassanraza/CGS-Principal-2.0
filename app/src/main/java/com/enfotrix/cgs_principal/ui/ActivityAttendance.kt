package com.enfotrix.cgs_principal.ui
import ClassesListAdapter
import FragmentAbsent
import FragmentLeave
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.ClassViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.Utils
import com.enfotrix.cgs_principal.databinding.ActivityAttendanceBinding
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class ActivityAttendance : AppCompatActivity(), ClassesListAdapter.AttendanceClickListener {
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private val classViewModel: ClassViewModel by viewModels()
    private lateinit var utils: Utils

    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var recyclerView: RecyclerView

    private lateinit var classAdapter: ClassesListAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityAttendance
        sharedPrefManager = SharedPrefManager(mContext)
        utils = Utils(mContext)


        binding.datePicker.setText(getCurrentDate())

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, FragmentAttendance())
            .commit()

        binding.FAttendance.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FragmentAttendance())
                .commit()
        }
        binding.FAbsent.setOnClickListener {
            val fragment = FragmentAbsent.newInstance(getCurrentDate())
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.FLeave.setOnClickListener {
            val fragment = FragmentLeave() // Create an instance of your LeaveFragment
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }


        getAttendance(getCurrentDate())

        binding.datePicker.setOnClickListener {
            showDatePickerDialog()
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
                        val attendanceList = task.result.map { it.toObject(AttendenceModel::class.java) }
                        val counterPresent = attendanceList.count { it.Status == "Present" }
                        val counterAbsent = attendanceList.count { it.Status == "Absent" }
                        val counterLeave = attendanceList.count { it.Status == "Leave" }
                        val total = counterPresent + counterAbsent + counterLeave
                        if (total > 0) {
                            val percent = (counterPresent.toFloat() / total) * 100
                            binding.tvAttendanceHeader.text = "Today Attendance: %.2f%%".format(percent)
                        }
                        binding.studentsPresent.text = counterPresent.toString()
                        binding.studentsAbsent.text = counterAbsent.toString()
                        binding.studentsLeave.text = counterLeave.toString()
                        classAdapter = ClassesListAdapter(
                            mContext,
                            classViewModel.getClassList(),
                            classViewModel.getSectionList(),
                            attendanceList,
                            this@ActivityAttendance
                        )
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

    override fun onAttendanceClicked(sectionID: String, attendanceList: List<AttendenceModel>) {
        val intent = Intent(this, ActivityStudentAttendanceRegister::class.java)
        intent.putExtra("Id", sectionID)
        intent.putExtra("studentlist", Gson().toJson(attendanceList))
        startActivity(intent)
    }

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
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDateCalendar.time)
                binding.datePicker.setText(selectedDateFormatted)
                showAbsentFragment(selectedDateFormatted)
            },
            todayYear,
            todayMonth,
            todayDayOfMonth
        )
        datePickerDialog.show()
    }

    private fun showAbsentFragment(selectedDate: String) {
        val fragment = FragmentAbsent.newInstance(selectedDate)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
