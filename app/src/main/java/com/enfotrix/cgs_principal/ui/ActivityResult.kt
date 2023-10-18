package com.enfotrix.cgs_principal.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.ResultAdapter
import com.enfotrix.cgs_principal.Models.ExamModel
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

class ActivityResult : AppCompatActivity() {
    private val studentViewModel: StudentViewModel by viewModels()
    private val examViewModel: ExamViewModel by viewModels()
    private lateinit var binding: ActivityResultBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private val constants = Constants()
    private val studentList = mutableListOf<StudentModel>()
    private val resultList = mutableListOf<ResultModel>()
    private lateinit var utils: Utils
    var examName = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var resultAdapter: ResultAdapter


    //private lateinit var examAdapter: ExamListAdapter
    var selectedYear: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityResult
        utils = Utils(mContext)
        sharedPrefManager = SharedPrefManager(mContext)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)

//        //getStudentsList()
//// Create a list of years (you can replace this with your list of years)
//        val years = listOf("2023", "2024", "2025", "2026")
//
//// Create an ArrayAdapter to populate the Spinner
//        val yearAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years)
//        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//// Set the ArrayAdapter on the Spinner
//        binding.spinnerYearSelection.adapter = yearAdapter
//        //binding.examTerm.text=sharedPrefManager.getExamModelFromShared()!!.ExamName
//        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(mContext)

        ///////////////spinner setting/////////////
        val items = listOf("2023", "2024", "2025", "2026")

        val spinner = findViewById<Spinner>(R.id.spinnerYearSelection)
        val yearadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        yearadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = yearadapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedYear = items[position]


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        val Examterms = sharedPrefManager.getExamsList()
        val examNames = mutableListOf<String>()

        for (examModel in Examterms) {
            val examName = examModel.ExamName.toString()
            examNames.add(examName)
        }

        val spinnerSelectTerm = findViewById<Spinner>(R.id.spinnerSelectTerm)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, examNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSelectTerm.adapter = adapter

//
//        val Examterms = sharedPrefManager.getExamsList()
//
//            for (i in Examterms.indices)
//            {
//                val examModel = Examterms[i]
//               examName = examModel.ExamName.toString()
//            }
//        binding.spinnerSelectTerm.text



        var examID="5VBcr9qxXfnOnibms4dX";
        var year="2023";


        binding.btnGetResult.setOnClickListener{
            getResult(examID,year)
        }


    }

    private fun getResult(examID: String, year: String) {
        lifecycleScope.launch {
            examViewModel.getResult(year,examID)
                .addOnCompleteListener { task->
                    if(task.isSuccessful){


                        Toast.makeText(mContext, "debug1", Toast.LENGTH_SHORT).show()

                        var Listresult = task.result.map { it.toObject(ResultModel::class.java) }






                        resultAdapter = ResultAdapter(
                            mContext,
                            sharedPrefManager.getSectionList(),
                            Listresult,
                            sharedPrefManager.getStudentList()

                        )
                        recyclerView.adapter = resultAdapter










                    }


                }.addOnFailureListener {
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                }



        }



    }


//    private fun getResultList() {
//        lifecycleScope.launch {
//            val subjectId = sharedPrefManager.getSelectedSubjectInShared()!!.ID
//            val sectionID = sharedPrefManager.getSectionFromShared()!!.ID
//            examViewModel.getResultList(selectedYear!!,subjectId!!, sectionID)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//
//                        val documents = task.result
//                        for (document in documents) {
//                            var result = document.toObject(ResultModel::class.java)
//                            resultList.add(result)
//                            binding.totalMarks.text=result.totalMarks
//                        }
//
//                        // Notify any adapter or UI component that displays the resultList
//                        // Example: adapter.notifyDataSetChanged()
//                         if(resultList.size==0){
//                             Toast.makeText(mContext, "No Result Found", Toast.LENGTH_LONG).show()
//                         }
//
//                        if(resultList.size==0){
//                            binding.recyclerView.visibility=View.INVISIBLE
//                        }
//                        studentList.sortBy { it.RegNumber }
//
//                        examAdapter.notifyDataSetChanged()
//
//                    }
//
//
//
//                    else {
//                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
//                    }
//                }
//        }
//    }













    ////     this function will set and update the result so on    ///

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }


}