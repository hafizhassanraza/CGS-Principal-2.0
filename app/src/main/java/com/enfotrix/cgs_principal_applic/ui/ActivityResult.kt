package com.enfotrix.cgs_principal_applic.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal_applic.Adapters.ResultAdapter
import com.enfotrix.cgs_principal_applic.Models.ExamViewModel
import com.enfotrix.cgs_principal_applic.Models.ResultModel
import com.enfotrix.cgs_principal_applic.Models.StudentModel
import com.enfotrix.cgs_principal_applic.Models.StudentViewModel
import com.enfotrix.cgs_principal_applic.R
import com.enfotrix.cgs_principal_applic.SharedPrefManager
import com.enfotrix.cgs_principal_applic.Utils
import com.enfotrix.cgs_principal_applic.databinding.ActivityResultBinding

import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch

class ActivityResult : AppCompatActivity(),ResultAdapter.classClickListener {
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
    var examterm: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityResult
        utils = Utils(mContext)
        sharedPrefManager = SharedPrefManager(mContext)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
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

        val examNames=Examterms.map { it.ExamName }

        val spinnerSelectTerm = findViewById<Spinner>(R.id.spinnerSelectTerm)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, examNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSelectTerm.adapter = adapter
        spinnerSelectTerm.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedExamTerm = Examterms[position]
                examterm=selectedExamTerm.ID
                getResult(examterm!!,selectedYear.toString())



            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun getResult(examID: String, year: String) {
        utils.startLoadingAnimation()
        lifecycleScope.launch {
            examViewModel.getResult(year,examID)
                .addOnCompleteListener { task->
                    if(task.isSuccessful){



                        var Listresult = task.result.map { it.toObject(ResultModel::class.java) }
                        resultAdapter = ResultAdapter(
                            mContext,
                            sharedPrefManager.getSectionList(),
                            Listresult,
                            sharedPrefManager.getStudentList(),
                            this@ActivityResult

                        )
                        recyclerView.adapter = resultAdapter

                    }
                    utils.endLoadingAnimation()



                }.addOnFailureListener {
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                    utils.endLoadingAnimation()
                }



        }
    }

    override fun onclassClicked(sectionID: String) {
        val intent = Intent(this, ActivityClassResult::class.java)
        intent.putExtra("Id",sectionID)
        intent.putExtra("selectedYear", selectedYear)
        intent.putExtra("selectedTerm", examterm)
        startActivity(intent)
    }



}