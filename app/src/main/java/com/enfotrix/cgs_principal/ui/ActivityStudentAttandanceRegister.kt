package com.enfotrix.cgs_principal.ui
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.AttendanceRecAdapter
import com.enfotrix.cgs_principal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.Models.StudentViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.databinding.ActivityStudentAttandanceRegisterBinding
import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class ActivityStudentAttendanceRegister : AppCompatActivity() {
    private val studentViewModel: StudentViewModel by viewModels()
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private lateinit var binding: ActivityStudentAttandanceRegisterBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var attendanceRecAdapter: AttendanceRecAdapter

    private val studentList = mutableListOf<StudentModel>()
    private val attendanceList = mutableListOf<AttendenceModel>()
    private lateinit var fromActivity:String


    private var constants = Constants()

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentAttandanceRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityStudentAttendanceRegister
        //sharedPrefManager = SharedPrefManager(mContext)

        ////////////////// HERE WILL GET INTENT VALUE ///////////////////////

        fromActivity= intent.getStringExtra("SectionId").toString()
        Toast.makeText(mContext, ""+fromActivity, Toast.LENGTH_SHORT).show()
        getStudentsList(fromActivity)
        getAttendanceRecord(getCurrentDate(),fromActivity)

//        if (fromActivity == "Main") {
//            binding.datePicker.visibility = View.GONE
//
//        } else {
//            binding.datePicker.visibility = View.VISIBLE
//        }









        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        attendanceRecAdapter = AttendanceRecAdapter(mContext, attendanceList,studentList)
        recyclerView.adapter = attendanceRecAdapter

//        binding.ClassName.text=sharedPrefManager.getClass()!!.ClassName
//        binding.sectionName.text=sharedPrefManager.getSectionFromSharedmODEL()!!.SectionName


    }

    /////////////////////////////////  GET STUDENT lIST ////////////////////////////////////
    private fun getAttendanceRecord(date: String,sectionId:String) {
        lifecycleScope.launch {


            attendanceViewModel.getAttendanceRec(date, sectionId).addOnCompleteListener { task ->
                if (task.isSuccessful) {





                    val attendanceList_ = task.result
                        .map { it.toObject(AttendenceModel::class.java) }
                        .filterNotNull()
                    attendanceList.addAll(attendanceList_)
                    if(attendanceList.size!=0){
                        binding.recyclerView.visibility=View.VISIBLE
                    }

                    attendanceRecAdapter.notifyDataSetChanged()
                } else {
                    // Handle the case where the task was not successful
                    Toast.makeText(mContext, "Failed to retrieve attendance data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    ////////////////////////////// FUNCTION TO GET CURRENT DATE ///////////////////////////


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }


    private fun getStudentsList(Id:String) {
        lifecycleScope.launch {

            //if (sectionID != null && className != null) {
            studentViewModel.getStudentsList(Id)
                .addOnCompleteListener { task ->


                    if (task.isSuccessful) {
                        val documents = task.result
                        for (document in documents) {
                            val student = document.toObject(StudentModel::class.java)
                            studentList.add(student)
                        }
                        studentList.sortBy { it.RegNumber }

                    } else {
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            //  }

        }
    }


}