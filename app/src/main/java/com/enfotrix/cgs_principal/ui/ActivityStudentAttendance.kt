package com.enfotrix.cgs_principal.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.AttendanceAdapter
import com.enfotrix.cgs_principal.CalendarDateDecorator
import com.enfotrix.cgs_principal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.Utils
import com.enfotrix.cgs_principal.databinding.ActivityStudentAttendanceBinding
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class ActivityStudentAttendance : AppCompatActivity() {

    private lateinit var adapter: AttendanceAdapter
    private lateinit var attendance: ArrayList<AttendenceModel>
    private lateinit var binding: ActivityStudentAttendanceBinding
    private lateinit var mContext: Context
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var recyclerView:RecyclerView
    private lateinit var utils: Utils


    private var currentMonth =9
    private val maxDaysInMonth = getMaxDaysInMonth(2023, currentMonth) // Get the maximum days in the selected month

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        mContext = this@ActivityStudentAttendance
        utils = Utils(mContext)

        calendarView=binding.materialCalendarView

        binding.imgBackArrow.setOnClickListener {
            super.onBackPressed()
        }


        val gson = Gson()
        var studentID:String?=null
        val studentListJson = intent.getStringExtra("selectedStudentList")
        val type = object : TypeToken<List<StudentModel>>() {}.type
        val selectedStudentList = gson.fromJson<List<StudentModel>>(studentListJson, type)
        // Now you can access the elements in the studentList
        for (student in selectedStudentList) {
            studentID =student.StudentId
            // ... (access other properties as needed)
        }

        ///////////////////////////////for recycler View/////////////////////////////////
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        attendance = ArrayList()
        adapter = AttendanceAdapter(attendance)
        recyclerView.adapter=adapter

        // Load data for the current month initially
        getAttendence(Calendar.getInstance().get(Calendar.MONTH)+1,studentID)

        // Set up the calendar view
        calendarView.setOnMonthChangedListener { widget, date ->
            currentMonth = date.month
            Log.d(ContentValues.TAG, "Current Month: $currentMonth")
            attendance.clear()
            adapter.notifyDataSetChanged()
            getAttendence(currentMonth,studentID)
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun getAttendence(month: Int,studentID:String?) {

        lifecycleScope.launch {
            utils.startLoadingAnimation()

            try {

                val result: QuerySnapshot = attendanceViewModel.getAttendence(studentID.toString()).await()
                // Process the result and update UI with attendance data here
                // Iterate through the result to populate the 'attendance' ArrayList
                for (document in result) {

                    val date = document.getString("date")
                    val status=document.getString("status")
                    for (i in 0..maxDaysInMonth) {

                        // for each computed date, fetch the students who attended on those dates
                        val dateM = String.format("%02d", i) + "-" + String.format("%02d", month) + "-" + 2023
                        if (date==dateM){
                            // Parse the document data and add it to the 'attendance' list
                            val studentAttendance = document.toObject(AttendenceModel::class.java)
                            attendance.add(studentAttendance)
                        }

                    }

                    val calendarDay = CalendarDay.from(
                        date!!.substring(6, 10).toInt(),
                        date.substring(3, 5).toInt(),
                        date.substring(0, 2).toInt()
                    )
                    calendarView.addDecorator(CalendarDateDecorator(mContext, calendarDay, status.toString()
                    ))
                }
                utils.endLoadingAnimation()

                // Notify the adapter of data change
                adapter.notifyDataSetChanged()
                // Update progress bars
                updateProgressBars(month)
            } catch (e: Exception) {
                // Handle any exceptions that may occur during Firestore query
                Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateProgressBars(month: Int) {

        lifecycleScope.launch {
            var presentCount = 0
            var absentCount = 0
            var totalCount = 0
            var leaveCount = 0

            try {
                val documents=attendance
                for (document in documents) {
                    val status = document.Status
                    val date=document.Date
                    if (status != null && date != null) {
                        for (i in 0..maxDaysInMonth) {

                            // for each computed date, fetch the students who attended on those dates
                            val dateM = String.format("%02d", i) + "-" + String.format("%02d", month) + "-" + 2023
                            if (date==dateM){
                                totalCount++
                                when (status) {
                                    "Present" -> presentCount++
                                    "Absent" -> absentCount++
                                    "Leave" -> leaveCount++
                                }
                            }
                        }

                    }
                }

                // Update the progress bars with the total number of attendance
                binding.presentProgressBar.progress = presentCount
                binding.absentProgressBar.progress = absentCount
                binding.leaveProgressBar.progress = leaveCount

                // Set the maximum value of the progress bars to the maximum days in the selected month
                binding.presentProgressBar.max = maxDaysInMonth
                binding.absentProgressBar.max = maxDaysInMonth
                binding.leaveProgressBar.max = maxDaysInMonth

                // Calculate attendance percentages with a maximum of 100 for each category
                val presentAbsentPercentage =
                    (presentCount.toDouble() / totalCount.toDouble()) * 100

                binding.presentTextView.text=presentCount.toString()
                binding.absentTextView.text=absentCount.toString()
                binding.leaveTextView.text=leaveCount.toString()
                binding.summaryPercentage.text=presentAbsentPercentage.toInt().toString()

                /////////end loading /////////
                utils.endLoadingAnimation()
            }
            catch (e:Exception){
                Toast.makeText(mContext, ""+e, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun getMaxDaysInMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
}
