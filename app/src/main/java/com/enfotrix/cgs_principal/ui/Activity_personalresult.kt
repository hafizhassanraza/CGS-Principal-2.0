package com.enfotrix.cgs_principal.ui
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.AdapterStudentResult
import com.enfotrix.cgs_principal.Models.ExamModel
import com.enfotrix.cgs_principal.Models.ExamViewModel
import com.enfotrix.cgs_principal.Models.ResultModel
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.Utils
import com.enfotrix.cgs_principal.databinding.ActivityPersonalresultBinding
import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch

class Activity_personalresult : AppCompatActivity() {
    private lateinit var binding: ActivityPersonalresultBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private val examViewModel: ExamViewModel by viewModels()
    private var examList: List<String>? = null
    private val resultList = mutableListOf<ResultModel>()
    private val constants = Constants()
    var selectedExamTerm: String? = null
    var selectedExamId: String? = null
    private lateinit var utils: Utils

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterStudentResult
    var studentId =""
    var selectedExam =""
    var selectedyear =""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalresultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@Activity_personalresult
        sharedPrefManager = SharedPrefManager(mContext)
        utils = Utils(mContext)
        studentId=intent.getStringExtra("Id").toString()
        selectedyear=intent.getStringExtra("selectedYear").toString()
        selectedExam=intent.getStringExtra("selectedTerm").toString()
        showResult()


        recyclerView = binding.recyclerView
        adapter = AdapterStudentResult(resultList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        binding.arrowBack.setOnClickListener {
            super.onBackPressed()
        }


    }
    fun showResult(){

        var list= mutableListOf<ExamModel>()



        lifecycleScope.launch {
            utils.startLoadingAnimation()



            val selectedExamN = list.find { it.ExamName == selectedExam }


            if (selectedExamN!=null){
                selectedExamId=selectedExamN.ID
                //Toast.makeText(mContext,""+selectedExamId,Toast.LENGTH_SHORT).show()
            }

            examViewModel.getResult(studentId,selectedyear,selectedExam)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Toast.makeText(mContext,"reached",Toast.LENGTH_SHORT).show()
                        try {
                            val documents = task.result
                            for (document in documents) {
                                val student = document.toObject(ResultModel::class.java)
                                resultList.add(student)

                            }
                            adapter.notifyDataSetChanged()
                            if (resultList.isEmpty())Toast.makeText(mContext,"Nothing to show",Toast.LENGTH_SHORT).show()
                            calculateOverallStatistics()
                        }
                        catch (e:Exception){
                            Toast.makeText(mContext,""+e.message, Toast.LENGTH_SHORT).show()
                        }

                    } else {

                        // Handle the error if fetching data fails
                    }
                }
        }
    }
    fun calculateOverallStatistics() {


                        // Now that you have resultList containing ResultModel objects,
                        // you can calculate statistics as needed.

                        val totalSubjects = resultList.size
                        val totalObtainedMarks = resultList.sumBy { it.obtainMarks?.toIntOrNull() ?: 0 }
                        val totalTotalMarks = resultList.sumBy { it.totalMarks?.toIntOrNull() ?: 0 }
                        val averageMarks = totalObtainedMarks.toDouble() / totalSubjects
                        val highestScore = resultList.maxByOrNull { it.obtainMarks?.toIntOrNull() ?: 0 }
                        val lowestScore = resultList.minByOrNull { it.obtainMarks?.toIntOrNull() ?: 0 }
                        val overallPercentage = (totalObtainedMarks.toDouble() / totalTotalMarks) * 100.0

                        // Now you can display or use the calculated statistics as needed.

                        binding.averageMarks.text = averageMarks.toString()
                        binding.highestScore.text = highestScore?.obtainMarks ?: "N/A"
                        binding.lowestScore.text = lowestScore?.obtainMarks ?: "N/A"
                        binding.overallPercentage.text = overallPercentage.toString()

    }


}