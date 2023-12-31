package com.enfotrix.cgs_principal_applic.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal_applic.Adapters.AdapterClassResult
import com.enfotrix.cgs_principal_applic.Models.ClassViewModel
import com.enfotrix.cgs_principal_applic.Models.ExamViewModel
import com.enfotrix.cgs_principal_applic.Models.ResultModel
import com.enfotrix.cgs_principal_applic.Models.StudentModel
import com.enfotrix.cgs_principal_applic.Models.StudentViewModel
import com.enfotrix.cgs_principal_applic.SharedPrefManager
import com.enfotrix.cgs_principal_applic.Utils
import com.enfotrix.cgs_principal_applic.databinding.ActivityClassResultBinding
import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch

class ActivityClassResult : AppCompatActivity(),AdapterClassResult.onStudentClickListener {


    private val studentViewModel: StudentViewModel by viewModels()
    private val classViewModel: ClassViewModel by viewModels()
    private val examViewModel: ExamViewModel by viewModels()
    private lateinit var binding: ActivityClassResultBinding
    private lateinit var mContext: Context
    private lateinit var utils: Utils
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var resultAdapter: AdapterClassResult

    private lateinit var recyclerView:RecyclerView

    private val studentList: MutableList<StudentModel> = mutableListOf()
    private val resultList : MutableList<ResultModel> = mutableListOf()
    private  var sectionID: String=""
    private var constants = Constants()
    var selectedYear = ""
    var selectedTerm = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityClassResult
        sharedPrefManager = SharedPrefManager(mContext)
        utils = Utils(mContext)



                             //here we get intent values


        selectedYear = intent.getStringExtra("selectedYear").toString()
        selectedTerm = intent.getStringExtra("selectedTerm").toString()
        sectionID = intent.getStringExtra("Id").toString()
        getResultList()



    }

    fun getResultList() {
        utils.startLoadingAnimation()
        lifecycleScope.launch {


            examViewModel.getsectionResult(sectionID, selectedYear, selectedTerm)
                .addOnCompleteListener { task ->
                    val result = task.result
                    for (document in result) {


                        var result = document.toObject(ResultModel::class.java)
                        resultList.add(result)
                    }
                    utils.endLoadingAnimation()

                    studentList.addAll(studentViewModel.getStudentsList(sectionID))
                    val classCard = classViewModel.getSectionModel(sectionID).ClassName
                    val sectionCard = classViewModel.getSectionModel(sectionID).SectionName
                    binding.ClassName.text = classCard
                    binding.sectionName.text = sectionCard
                    //passing list to adapter
                    recyclerView = binding.recyclerView
                    recyclerView.layoutManager = LinearLayoutManager(mContext)
                    resultAdapter = AdapterClassResult(mContext,studentList,resultList,this@ActivityClassResult
                    )
                    recyclerView.adapter=resultAdapter
                }

        }
    }

    override fun onStudentClicked(studentId: String) {
        val intent = Intent(this, ActivityPersonalresult::class.java)
        intent.putExtra("Id",studentId)
        intent.putExtra("selectedYear", selectedYear)
        intent.putExtra("selectedTerm",selectedTerm )
        startActivity(intent)
    }
}
