package com.enfotrix.cgs_principal.ui
import ClassesListAdapter
import android.content.Context
import android.content.Intent
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
import com.enfotrix.cgs_principal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.ClassViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.Utils
import com.enfotrix.cgs_principal.databinding.FragmentAttendanceBinding
import com.google.gson.Gson

class FragmentAttendance : Fragment() ,ClassesListAdapter.AttendanceClickListener, ActivityAttendance.AttendanceDataListener{
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
         attendanceData = (arguments?.getSerializable("attendanceList") as ArrayList<AttendenceModel>?)!!


        if (attendanceData != null) {
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
        return view
    }




//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getAttendance(date: String) {
//        utils.startLoadingAnimation()
//        lifecycleScope.launch {
//            attendanceViewModel.getAttendanceRec(date)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        utils.endLoadingAnimation()
//                        val attendanceList = task.result.map { it.toObject(AttendenceModel::class.java) }
//                        // Your code for calculating and displaying attendance data here
//
//                    }
//                }
//                .addOnFailureListener {
//                    // Handle the failure
//                }
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun showDatePickerDialog() {
//        val calendar: Calendar = Calendar.getInstance()
//        val todayYear: Int = calendar.get(Calendar.YEAR)
//        val todayMonth: Int = calendar.get(Calendar.MONTH)
//        val todayDayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)
//        val datePickerDialog = DatePickerDialog(
//            requireContext(),
//            { datePicker, selectedYear, selectedMonth, selectedDay ->
//                val selectedDateCalendar = Calendar.getInstance()
//                selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay)
//                val selectedDateFormatted = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//                    .format(selectedDateCalendar.time)
//               // getAttendance(selectedDateFormatted)
//            },
//            todayYear,
//            todayMonth,
//            todayDayOfMonth
//        )
//        datePickerDialog.show()
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getCurrentDate(): String {
//        val currentDate = LocalDate.now()
//        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//        return currentDate.format(formatter)
//    }
//

    fun updateAdapter() {
        attendanceData = (arguments?.getSerializable("attendanceList") as? ArrayList<AttendenceModel>)!!

        if (attendanceData != null) {
            classAdapter = ClassesListAdapter(
                mContext,
                classViewModel.getClassList(),
                classViewModel.getSectionList(),
                attendanceData,
                this@FragmentAttendance
            )
            recyclerView.adapter = classAdapter
            utils.endLoadingAnimation()
        } else {
            // Argument is null or not an ArrayList
            // Handle this case appropriately
        }
    }


    fun updateAttendanceData(attendanceList: List<AttendenceModel>) {
        // Update the attendance data and refresh the UI
        attendanceData = attendanceList
        updateAdapter()
    }

    override fun onAttendanceClicked(sectionID: String, attendacneList: List<AttendenceModel>) {
        val intent = Intent(requireContext(), ActivityStudentAttendanceRegister::class.java)
        intent.putExtra("Id", sectionID)
        intent.putExtra("studentlist", Gson().toJson(attendacneList))
        startActivity(intent)
    }

    override fun onAttendanceDataReceived(attendanceList: List<AttendenceModel>) {
        // Update the attendance data and refresh the UI
        this.attendanceData = attendanceList
        updateAdapter()
    }
}
