package com.enfotrix.cgs_principal.ui
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.AttendanceRecAdapter
import com.enfotrix.cgs_principal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.ClassViewModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.Models.StudentViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.databinding.ActivityStudentAttandanceRegisterBinding
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
        //Toast.makeText(mContext, ""+attendanceList.size, Toast.LENGTH_SHORT).show()


        //Toast.makeText(mContext, ""+ID, Toast.LENGTH_SHORT).show()
        //getStudentsList(ID)


        //getAttendanceRecord(getCurrentDate(),ID)

//        if (fromActivity == "Main") {
//            binding.datePicker.visibility = View.GONE
//
//        } else {
//            binding.datePicker.visibility = View.VISIBLE
//        }
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

    /////////////////////////////////  GET STUDENT lIST ////////////////////////////////////
    /*private fun getAttendanceRecord(date: String,sectionId:String) {
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
    }*/



    ////////////////////////////// FUNCTION TO GET CURRENT DATE ///////////////////////////


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }


    private fun getStudentsList(Id: String) {
        // Check if the student list is already loaded from SharedPreferences
        if (studentList.isEmpty()) {
            // If not, fetch the list from SharedPreferences
            val storedStudentList = studentViewModel.getStudentsList()
            if (storedStudentList.isNotEmpty()) {
                //studentList.addAll(storedStudentList)
                //attendanceRecAdapter.notifyDataSetChanged()
            }
        }

        // If the list is still empty or not found in SharedPreferences, fetch it from your source
//        if (studentList.isEmpty()) {
//            lifecycleScope.launch {
//                // Fetch the data from your source (e.g., studentViewModel)
//                studentViewModel.getStudentsList(Id)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            val documents = task.result
//                            for (document in documents) {
//                                val student = document.toObject(StudentModel::class.java)
//                                studentList.add(student)
//                            }
//                            studentList.sortBy { it.RegNumber }
//                            attendanceRecAdapter.notifyDataSetChanged()
//
//                            // Save the fetched list in SharedPreferences
//                            sharedPrefManager.saveStudentList(studentList)
//                        } else {
//                            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
//                        }
//                    }
            }
}