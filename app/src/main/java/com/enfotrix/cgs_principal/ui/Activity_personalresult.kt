package com.enfotrix.cgs_principal.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.ExamViewModel
import com.enfotrix.cgs_principal.Models.ResultModel
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.databinding.ActivityResultBinding
import com.enftorix.cgs_principal.Constants

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Activity_personalresult : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private val examViewModel: ExamViewModel by viewModels()
    private var examList: List<String>? = null
    private val resultList = mutableListOf<ResultModel>()
    private val constants = Constants()
    var selectedExamTerm: String? = null
    private lateinit var recyclerView: RecyclerView
    //private lateinit var adapter: AdapterResult




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@Activity_personalresult
        sharedPrefManager = SharedPrefManager(mContext)

        recyclerView = binding.recyclerView
        //adapter = AdapterResult(resultList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.adapter = adapter

        //////for setting the exam spinner
        /*lifecycleScope.launch(Dispatchers.IO) {
            examList = getExamsList()
            withContext(Dispatchers.Main) {
                if (!examList.isNullOrEmpty()) {
                    val adapter1 = ArrayAdapter(
                        this@Activity_personalresult,
                        android.R.layout.simple_spinner_item,
                        examList!!
                    )
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    val spinnerExamTerm = binding.spinnerSelectExamType
                    spinnerExamTerm.adapter = adapter1
                    spinnerExamTerm.setSelection(0)
                }
            }
        }*/




        // Set up a listener for the exam term spinner
        //val spinnerExamTerm = binding.spinnerSelectExamType
        /*spinnerExamTerm.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedExamTerm = parent?.getItemAtPosition(position).toString()
//                showToast("Selected Exam Term: $selectedExamTerm")

                (selectedExamTerm!!)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        })*/





        /*binding.arrowBack.setOnClickListener {
            super.onBackPressed()
        }*/


        /*examViewModel.getResult(sharedPrefManager!!.getStudent()?.StudentId.toString(),sharedPrefManager.getSection().ID,"2023","5VBcr9qxXfnOnibms4dX")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    try {
                        val documents = task.result
                        for (document in documents) {
                            val student = document.toObject(ResultModel::class.java)
                            resultList.add(student)
                        }

                        // Notify the adapter that data has changed
                        adapter.notifyDataSetChanged()
                    }
                    catch (e:Exception){
                        Toast.makeText(mContext,""+e.message, Toast.LENGTH_SHORT).show()
                    }

                } else {
                    // Handle the error if fetching data fails
                }
            }*/
    }

    /*private suspend fun getExamsList(): List<String> {
        val result = mutableListOf<String>()
        try {
            val documents = examViewModel.getExamsList().await()
            for (document in documents) {
                val examName: String? = document.getString(constants.FIELD_EXAM_NAME)
                examName?.let { result.add(examName) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }*/












}
