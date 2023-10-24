package com.enfotrix.cgs_principal.ui
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.AdapterStudentResult
import com.enfotrix.cgs_principal.Models.ExamViewModel
import com.enfotrix.cgs_principal.Models.ModelSubject
import com.enfotrix.cgs_principal.Models.ResultModel
import com.enfotrix.cgs_principal.Models.StudentViewModel
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.Utils
import com.enfotrix.cgs_principal.databinding.ActivityPersonalresultBinding
import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch

class ActivityPersonalresult : AppCompatActivity() {
    private lateinit var binding: ActivityPersonalresultBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private val examViewModel: ExamViewModel by viewModels()
    private val studetViewmodel: StudentViewModel by viewModels()
    private var examList: List<String>? = null
    private val resultList = mutableListOf<ResultModel>()
    private var subjectList: MutableList<ModelSubject> = mutableListOf()
    private val constants = Constants()
    var selectedExamTerm: String? = null
    var selectedExamId: String? = null
    private lateinit var utils: Utils
    lateinit var examtype:TextView

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterStudentResult
    var studentId =""
    var selectedExam =""
    var selectedyear =""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalresultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityPersonalresult
        sharedPrefManager = SharedPrefManager(mContext)
        utils = Utils(mContext)
        studentId = intent.getStringExtra("Id").toString()
        selectedyear = intent.getStringExtra("selectedYear").toString()
        selectedExam = intent.getStringExtra("selectedTerm").toString()


        // Log the values for debugging purposes
        Toast.makeText(mContext, "studentId: $studentId", Toast.LENGTH_SHORT).show()
        Toast.makeText(mContext, "examterm: $selectedExam", Toast.LENGTH_SHORT).show()
        Toast.makeText(mContext, "year: $selectedyear", Toast.LENGTH_SHORT).show()

        val examList = examViewModel.getExamsList()
        val selectedExamId = selectedExam
        val examTerm = examList.find { it.ID == selectedExamId }
        val examName = examTerm?.ExamName
        binding.tvExamType.text = examName.toString()
        recyclerView = binding.recyclerView

        // Initialize the adapter here

        recyclerView.layoutManager = LinearLayoutManager(mContext)


        binding.arrowBack.setOnClickListener {
            super.onBackPressed()
        }

        showResult()
        // Load subjects based on section

    }
    fun showResult(){




        lifecycleScope.launch {
//            utils.startLoadingAnimation()



            examViewModel.getResult(studentId,selectedyear,selectedExam)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(mContext,"reached",Toast.LENGTH_SHORT).show()
                        val documents = task.result
                            for (document in documents) {
                                val student = document.toObject(ResultModel::class.java)
                                resultList.add(student)
                                //Toast.makeText(mContext, "debug................", Toast.LENGTH_SHORT).show()
//                                utils.endLoadingAnimation()

                            }
                        //Toast.makeText(mContext, "size of resultList is=="+resultList.size, Toast.LENGTH_SHORT).show()
                        recyclerView.adapter = AdapterStudentResult(resultList, sharedPrefManager.getSubjectsList())



                    } else {
                        Toast.makeText(mContext,"not-reached",Toast.LENGTH_SHORT).show()

                        // Handle the error if fetching data fails
                    }
                }
                .addOnFailureListener {e->
                    Toast.makeText(mContext, ""+e.message, Toast.LENGTH_SHORT).show()
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
    fun getSubject(sectionId:String): MutableList<ModelSubject> {


        return sharedPrefManager.getSubjectsList().filter { it.SectionID==sectionId }.toMutableList()
        //Toast.makeText(mContext, "subjectList:"+subjectList, Toast.LENGTH_SHORT).show()

    }


}