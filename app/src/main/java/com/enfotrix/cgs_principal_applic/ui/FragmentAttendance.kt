package com.enfotrix.cgs_principal_applic.ui
import ClassesListAdapter
import android.content.Context
import android.content.Intent
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
import com.enfotrix.cgs_principal_applic.Models.AttendanceViewModel
import com.enfotrix.cgs_principal_applic.Models.AttendenceModel
import com.enfotrix.cgs_principal_applic.Models.ClassViewModel
import com.enfotrix.cgs_principal_applic.R
import com.enfotrix.cgs_principal_applic.SharedPrefManager
import com.enfotrix.cgs_principal_applic.Utils
import com.enfotrix.cgs_principal_applic.databinding.FragmentAttendanceBinding
import com.google.gson.Gson

class FragmentAttendance : Fragment() ,ClassesListAdapter.AttendanceClickListener{
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private val classViewModel: ClassViewModel by viewModels()
    private lateinit var utils: Utils
    private lateinit var binding: FragmentAttendanceBinding
    private lateinit var mContext: Context
    private var attendanceData: List<AttendenceModel> = emptyList()
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var classAdapter: ClassesListAdapter


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        val view = binding.root
        mContext = requireContext()
        sharedPrefManager = SharedPrefManager(mContext)
        utils = Utils(mContext)
        recyclerView = view.findViewById(R.id.recyclerViewAttendance)
        recyclerView.layoutManager = LinearLayoutManager(mContext)

        // Load the initial attendance data
        attendanceData = sharedPrefManager.getAttendanceListByDate()
        if (attendanceData.isNotEmpty()) {
            // Initialize the adapter with the initial data
            classAdapter = ClassesListAdapter(
                mContext,
                classViewModel.getClassList(),
                classViewModel.getSectionList(),
                attendanceData,
                this@FragmentAttendance
            )

            recyclerView.adapter = classAdapter
            utils.endLoadingAnimation()


        }
        else{
            classAdapter = ClassesListAdapter(
                mContext,
                classViewModel.getClassList(),
                classViewModel.getSectionList(),
                emptyList(),
                this@FragmentAttendance
            )
            recyclerView.adapter = classAdapter
            utils.endLoadingAnimation()
            classAdapter.notifyDataSetChanged()
        }
        return view
    }


    override fun onAttendanceClicked(sectionID: String, attendacneList: List<AttendenceModel>) {
        val intent = Intent(requireContext(), ActivityStudentAttendanceRegister::class.java)
        intent.putExtra("Id", sectionID)
        intent.putExtra("studentlist", Gson().toJson(attendacneList))
        startActivity(intent)
    }

}
