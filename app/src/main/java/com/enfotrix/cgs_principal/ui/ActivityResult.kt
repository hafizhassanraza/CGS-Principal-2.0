package com.enfotrix.cgs_principal.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.ExamViewModel
import com.enfotrix.cgs_principal.Models.ResultModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.Models.StudentViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.Utils
import com.enfotrix.cgs_principal.databinding.ActivityResultBinding

import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ActivityResult : AppCompatActivity(){
    private val studentViewModel: StudentViewModel by viewModels()
    private val examViewModel: ExamViewModel by viewModels()
    private lateinit var binding: ActivityResultBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private val constants = Constants()
    private val studentList = mutableListOf<StudentModel>()
    private val resultList = mutableListOf<ResultModel>()
    private lateinit var utils: Utils

    //private lateinit var examAdapter: ExamListAdapter
    var selectedYear: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityResult
        utils=Utils(mContext)
        sharedPrefManager = SharedPrefManager(mContext)
         selectedYear = intent.getStringExtra("selectedYear")

        //getStudentsList()
        binding.year.text=selectedYear
        //binding.examTerm.text=sharedPrefManager.getExamModelFromShared()!!.ExamName



        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        //examAdapter = ExamListAdapter(mContext, studentList,resultList)
        //recyclerView.adapter = examAdapter




        ///////for handling the apload button response



    }




    /*private fun getResultList() {
        lifecycleScope.launch {
            val subjectId = sharedPrefManager.getSelectedSubjectInShared()!!.ID
            val sectionID = sharedPrefManager.getSectionFromShared()!!.ID
            examViewModel.getResultList(selectedYear!!,subjectId!!, sectionID)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val documents = task.result
                        for (document in documents) {
                            var result = document.toObject(ResultModel::class.java)
                            resultList.add(result)
                            binding.totalMarks.text=result.totalMarks
                        }

                        // Notify any adapter or UI component that displays the resultList
                        // Example: adapter.notifyDataSetChanged()
                         if(resultList.size==0){
                             Toast.makeText(mContext, "No Result Found", Toast.LENGTH_LONG).show()
                         }

                        if(resultList.size==0){
                            binding.recyclerView.visibility=View.INVISIBLE
                        }
                        studentList.sortBy { it.RegNumber }

                        examAdapter.notifyDataSetChanged()

                    }



                    else {
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }*/












    @SuppressLint("SuspiciousIndentation")
    /*private fun getStudentsList() {
        utils.startLoadingAnimation()
        lifecycleScope.launch {
            val className = sharedPrefManager.getClass()!!.ClassName
            val sectionID = sharedPrefManager.getSectionFromShared()!!.ID

            studentViewModel.getStudentsList(className, sectionID)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        utils.endLoadingAnimation()
                        val documents = task.result
                        for (document in documents) {
                            val student = document.toObject(StudentModel::class.java)
                            studentList.add(student)

                        }
                        getResultList()

                    } else {
                        Toast.makeText(mContext, "Something wsent wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }*/

    ////     this function will set and update the result so on    ///

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }


}
