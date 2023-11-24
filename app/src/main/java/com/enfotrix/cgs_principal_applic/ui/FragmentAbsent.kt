package com.enfotrix.cgs_principal_applic

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal_applic.Adapters.AdapterAbsent
import com.enfotrix.cgs_principal_applic.Models.AttendanceViewModel
import com.enfotrix.cgs_principal_applic.Models.AttendenceModel
import com.enfotrix.cgs_principal_applic.Models.StudentModel
import com.enfotrix.cgs_principal_applic.Models.StudentViewModel

class FragmentAbsent : Fragment(), AdapterAbsent.PhoneIconClickListener {
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private val studentViewModel: StudentViewModel by viewModels()
    private lateinit var mContext: Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPrefManager: SharedPrefManager

    private lateinit var adapterAbsent: AdapterAbsent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPrefManager = SharedPrefManager(requireContext())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext = requireContext()
        sharedPrefManager = SharedPrefManager(mContext)
        val view = inflater.inflate(R.layout.fragment_absent, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        val attendanceList = getAttendanceList()
        val absentStudentsList = getAbsentStudentsList(attendanceList)

        if (attendanceList.isNotEmpty()) {
            setupRecyclerView(absentStudentsList, attendanceList)
        } else {
            handleEmptyAttendanceList()
        }

        return view
    }

    private fun getAttendanceList(): List<AttendenceModel> {
        return sharedPrefManager.getAttendanceListByDate().toMutableList()
    }

    private fun getAbsentStudentsList(attendanceList: List<AttendenceModel>): List<StudentModel> {
        val allStudentsList = sharedPrefManager.getStudentList()
        return allStudentsList.filter { student ->
            attendanceList.any { absent ->
                absent.StudentID == student.StudentId && absent.Status == "Absent"
            }
        }
    }

    private fun setupRecyclerView(absentStudentsList: List<StudentModel>, attendanceList: List<AttendenceModel>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapterAbsent = AdapterAbsent(
            mContext,
            absentStudentsList.sortedBy { it.RegNumber },
            sharedPrefManager.getSectionList(),
            attendanceList.filter { it.Status == "Absent" }.toMutableList(),
            this
        )
        recyclerView.adapter = adapterAbsent
    }

    private fun handleEmptyAttendanceList() {
        recyclerView.visibility = View.GONE
    }

    private fun openDialer(phoneNumber: String) {
        val uri = Uri.parse("tel:$phoneNumber")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        startActivity(intent)
    }

    override fun onPhoneIconClick(phoneNumber: String) {
        openDialer(phoneNumber)
    }
}
