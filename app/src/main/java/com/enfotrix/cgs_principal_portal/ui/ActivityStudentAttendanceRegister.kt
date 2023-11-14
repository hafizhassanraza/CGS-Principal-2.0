package com.enfotrix.cgs_principal_portal.ui
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal_portal.Adapters.AttendanceRecAdapter
import com.enfotrix.cgs_principal_portal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal_portal.Models.AttendenceModel
import com.enfotrix.cgs_principal_portal.Models.ClassViewModel
import com.enfotrix.cgs_principal_portal.Models.StudentModel
import com.enfotrix.cgs_principal_portal.Models.StudentViewModel
import com.enfotrix.cgs_principal_portal.R
import com.enfotrix.cgs_principal_portal.SharedPrefManager
import com.enfotrix.cgs_principal_portal.databinding.ActivityStudentAttandanceRegisterBinding
import com.enftorix.cgs_principal.Constants
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ActivityStudentAttendanceRegister : AppCompatActivity() {
    private val studentViewModel: StudentViewModel by viewModels()
    private val classViewModel: ClassViewModel by viewModels()
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private lateinit var binding: ActivityStudentAttandanceRegisterBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var attendanceRecAdapter: AttendanceRecAdapter

    private val studentList: MutableList<StudentModel> = mutableListOf()
    private val attendanceList = mutableListOf<AttendenceModel>()
    private lateinit var ID:String


    private var constants = Constants()

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentAttandanceRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityStudentAttendanceRegister
        sharedPrefManager = SharedPrefManager(mContext)
        studentList.addAll(studentViewModel.getStudentsList())




        ////////////////// HERE WILL GET INTENT VALUE ///////////////////////

        ID= intent.getStringExtra("Id").toString()
        val attendanceList: List<AttendenceModel> = Gson().fromJson(
            intent.getStringExtra("studentlist"),
            object : TypeToken<List<AttendenceModel?>?>() {}.getType()

        )
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)

// Sort the attendanceList based on registration number
        val sortedAttendanceList = attendanceList.sortedBy { attendanceModel ->
            val studentModel = studentList.firstOrNull { it.StudentId == attendanceModel.StudentID }
            studentModel?.RegNumber ?: ""
        }

        attendanceRecAdapter = AttendanceRecAdapter(mContext, sortedAttendanceList, studentList)
        recyclerView.adapter = attendanceRecAdapter

// Retrieve class and section names from shared preferences
        val className = classViewModel.getSectionModel(ID).ClassName
        val sectionName = classViewModel.getSectionModel(ID).SectionName

// Set the class and section names in your UI
        binding.ClassName.text = className
        binding.sectionName.text = sectionName
    }
    ////////////////////////////// FUNCTION TO GET CURRENT DATE ///////////////////////////


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }


}