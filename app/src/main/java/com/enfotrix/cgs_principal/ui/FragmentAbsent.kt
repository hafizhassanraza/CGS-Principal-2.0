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
import com.enfotrix.cgs_principal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.SectionModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.Models.StudentViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FragmentAbsent : Fragment(), AdapterAbsent.PhoneIconClickListener {
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private val studentViewModel: StudentViewModel by viewModels()
    private var sectionList = mutableListOf<SectionModel>()
    private var attendanceList = mutableListOf<AttendenceModel>()
    private lateinit var mContext: Context
    private lateinit var recyclerView: RecyclerView
    private var sharedPrefManager: SharedPrefManager? = null

    private lateinit var adapterAbsent: AdapterAbsent
    var selectedDate: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext = requireContext()
        sharedPrefManager = SharedPrefManager(mContext)
        val view = inflater.inflate(R.layout.fragment_absent, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        attendanceList = (arguments?.getSerializable("attendanceList") as? ArrayList<AttendenceModel>) ?: mutableListOf()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapterAbsent = AdapterAbsent(mContext, sharedPrefManager!!.getStudentList(), sharedPrefManager!!.getSectionList(), attendanceList.filter { it.Status == "Absent" }.toMutableList(), this)
        recyclerView.adapter = adapterAbsent
        return view
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
