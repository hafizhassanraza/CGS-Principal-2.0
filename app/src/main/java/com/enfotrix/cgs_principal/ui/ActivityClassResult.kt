package com.enfotrix.cgs_principal.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.AdapterClassResult
import com.enfotrix.cgs_principal.Models.ClassViewModel
import com.enfotrix.cgs_principal.Models.ExamViewModel
import com.enfotrix.cgs_principal.Models.ResultModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.Models.StudentViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.databinding.ActivityClassResultBinding
import com.enftorix.cgs_principal.Constants
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class ActivityClassResult : AppCompatActivity() {


    private val studentViewModel: StudentViewModel by viewModels()
    private val classViewModel: ClassViewModel by viewModels()
    private val examViewModel: ExamViewModel by viewModels()
    private lateinit var binding: ActivityClassResultBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var resultAdapter: AdapterClassResult


    private val studentList: MutableList<StudentModel> = mutableListOf()
    private val resltList = mutableListOf<ResultModel>()
    private lateinit var ID:String
    private var constants = Constants()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityClassResult
        sharedPrefManager = SharedPrefManager(mContext)
        studentList.addAll(studentViewModel.getStudentsList())
        val selectedYear = intent.getStringExtra("selectedYear")
        val selectedTerm = intent.getStringExtra("selectedTerm")


        ////////////////// HERE WILL GET INTENT VALUE ///////////////////////

        ID = intent.getStringExtra("Id").toString()
        var studentID: String? = null

        val ResultList: List<ResultModel> = Gson().fromJson(
            intent.getStringExtra("studentlist"),
            object : TypeToken<List<ResultModel?>?>() {}.getType()
        )
        Toast.makeText(mContext, "resultlist"+ResultList.size, Toast.LENGTH_SHORT).show()
        for (student in ResultList) {
            studentID = student.studentId

            // ... (access other properties as needed)
        }


        val sortedList = studentList.sortedBy { studentmodel ->
            val studentModel = studentList.firstOrNull { it.StudentId == studentmodel.StudentId }
            studentModel?.RegNumber ?: ""
        }


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val resultList= examViewModel.getResult(
            studentID.toString(),
            selectedYear.toString(),
            selectedTerm.toString()
        )

        resultAdapter = AdapterClassResult(mContext, sortedList, resltList)
        recyclerView.adapter = resultAdapter

// Retrieve class and section names from shared preferences
        val classCard = classViewModel.getSectionModel(ID).ClassName
        val sectionCard = classViewModel.getSectionModel(ID).SectionName
        binding.ClassName.text = classCard
        binding.sectionName.text = sectionCard


    }
}
