package com.enfotrix.cgs_principal_applic.ui

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
import com.enfotrix.cgs_principal_applic.Adapters.AdapterReportResult
import com.enfotrix.cgs_principal_applic.Models.ExamViewModel
import com.enfotrix.cgs_principal_applic.Models.ResultModel
import com.enfotrix.cgs_principal_applic.SharedPrefManager
import com.enfotrix.cgs_principal_applic.Utils
import com.enfotrix.cgs_principal_applic.databinding.ActivityReportResultBinding
import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch

class ActivityReportResult : AppCompatActivity() {

    private lateinit var binding: ActivityReportResultBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private val examViewModel: ExamViewModel by viewModels()
    private val resultList = mutableListOf<ResultModel>()
    private val constants = Constants()
    private lateinit var utils: Utils
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterReportResult
    private var studentId = ""
    private lateinit var selectedYear: String
    private lateinit var examterm: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityReportResult
        sharedPrefManager = SharedPrefManager(mContext)
        utils = Utils(mContext)

        studentId = intent.getStringExtra("studentId").toString()
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(mContext)
       // recyclerView.adapter = adapter // Set the adapter to the RecyclerView

        val items = listOf("2023", "2024", "2025", "2026")
        val spinner = binding.spinnerYearSelection
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
        val examNames = Examterms.map { it.ExamName }

        val spinnerSelectTerm = binding.spinnerSelectTerm
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
                examterm = selectedExamTerm.ID
                showResult()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun showResult() {
        lifecycleScope.launch {


            examViewModel.getResult(studentId, selectedYear, examterm)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val documents = task.result
                        resultList.clear() // Clear the existing results
                        for (document in documents) {
                            val student = document.toObject(ResultModel::class.java)
                            resultList.add(student)
                        }
                        recyclerView.adapter = AdapterReportResult(resultList, sharedPrefManager.getSubjectsList())
                        calculateOverallStatistics()
                    } else {
                        // Handle the error if fetching data fails
                        Toast.makeText(mContext, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun calculateOverallStatistics() {
        val totalSubjects = resultList.size
        val totalObtainedMarks = resultList.sumBy { it.obtainMarks?.toIntOrNull() ?: 0 }
        val totalTotalMarks = resultList.sumBy { it.totalMarks?.toIntOrNull() ?: 0 }
        val averageMarks = totalObtainedMarks.toDouble() / totalSubjects
        val highestScore = resultList.maxByOrNull { it.obtainMarks?.toIntOrNull() ?: 0 }
        val lowestScore = resultList.minByOrNull { it.obtainMarks?.toIntOrNull() ?: 0 }
        val overallPercentage = (totalObtainedMarks.toDouble() / totalTotalMarks) * 100.0
        binding.averageMarks.text = averageMarks.toString()
        binding.highestScore.text = highestScore?.obtainMarks ?: "N/A"
        binding.lowestScore.text = lowestScore?.obtainMarks ?: "N/A"
        binding.overallPercentage.text = overallPercentage.toString()
    }
}
