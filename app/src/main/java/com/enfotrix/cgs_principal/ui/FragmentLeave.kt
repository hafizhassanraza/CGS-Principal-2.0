package com.enfotrix.cgs_principal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.AdapterAbsent
import com.enfotrix.cgs_principal.FragmentAbsent
import com.enfotrix.cgs_principal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.SectionModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.Models.StudentViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FragmentLeave : Fragment(), AdapterAbsent.PhoneIconClickListener {
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private val studentViewModel: StudentViewModel by viewModels()
    private var filteredList = mutableListOf<StudentModel>()
    private var sectionList = mutableListOf<SectionModel>()
    private lateinit var mContext: Context

    private var sharedPrefManager: SharedPrefManager? = null

    private lateinit var adapterAbsent: AdapterAbsent
    private var selectedDate: String=""
    companion object {
        fun newInstance(selectedDate: String): FragmentAbsent {
            val fragment = FragmentAbsent()
            fragment.selectedDate = selectedDate
            return fragment
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        sharedPrefManager = SharedPrefManager(mContext)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_absent, container, false)

        // Set up the RecyclerView and adapter
       // setUpRecyclerView(view)

        // Display absent students for the current date
        //displayAbsentStudents(getCurrentDate())

        return view
    }

    private fun setUpRecyclerView(view: View) {
//        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        adapterAbsent =  AdapterAbsent(
//            mContext,
//            filteredList,
//            sharedPrefManager!!.getSectionList(),
//            attendanceList,
//            this
//        )
//        recyclerView.adapter = adapterAbsent
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayAbsentStudents(date: String) {


// Step 1: Retrieve a list of all students
        studentViewModel.getStudents().addOnCompleteListener { studentTask ->
            if (studentTask.isSuccessful) {
                val studentList = studentTask.result.map { it.toObject(StudentModel::class.java) }.toMutableList()

                // Step 2: Retrieve a list of attendance records
                attendanceViewModel.getAttendanceRec(date).addOnCompleteListener { attendanceTask ->
                    if (attendanceTask.isSuccessful) {
                        val attendanceList = attendanceTask.result.map { it.toObject(AttendenceModel::class.java) }
                        // Step 3: Filter students with "Absent" status
                        val absentStudentNames = studentList.filter { student ->
                            attendanceList.any { it.StudentID == student.StudentId && it.Status == "Leave" }

                        }
                        Toast.makeText(mContext, ""+absentStudentNames.size, Toast.LENGTH_SHORT).show()

                        // Update the filtered list
                        filteredList.clear()
                        filteredList.addAll(absentStudentNames)
                      //  adapterAbsent.notifyDataSetChanged()
                    } else {
                        // Handle the case where attendance data retrieval was not successful
                    }
                }
            } else {
                // Handle the case where student data retrieval was not successful
            }
        }


    }

    private fun openDialer(phoneNumber: String) {
        val uri = Uri.parse("tel:$phoneNumber")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }
    override fun onPhoneIconClick(phoneNumber: String) {
        openDialer(phoneNumber)    }
}