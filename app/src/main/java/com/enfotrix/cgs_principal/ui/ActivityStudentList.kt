package com.enfotrix.cgs_principal.ui
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.StudentListAdapter
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.Models.StudentViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.Utils

import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch

class ActivityStudentList : AppCompatActivity() {
    private val studentViewModel: StudentViewModel by viewModels()
    //private lateinit var binding: ActivityStudentListBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private val constants = Constants()
    private lateinit var utils: Utils

    private var studentList = mutableListOf<StudentModel>()
    private val tempList = mutableListOf<StudentModel>()
    private lateinit var studentAdapter: StudentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // binding = ActivityStudentListBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        mContext = this@ActivityStudentList
        utils = Utils(mContext)

        sharedPrefManager = SharedPrefManager(mContext)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        studentAdapter = StudentListAdapter(mContext, sharedPrefManager.getStudentList()!!,"StudentListActivity")
        recyclerView.adapter = studentAdapter


        /*val searchBar = findViewById<SearchView>(R.id.searchBar)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission (if needed)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search query text changes
                if (newText != null) {
                    filterStudentList(newText)
                }
                return true
            }

        })*/


        // Load student data
        //getStudentsList()
       // binding.Class.text = sharedPrefManager.getClass()?.ClassName
       // binding.section.text = sharedPrefManager.getSectionFromShared()?.SectionName
    }


    private fun filterStudentList(query: String?) {
        if (query.isNullOrEmpty()) {
            // If the query is empty or null, display the original studentList
            studentAdapter.updateStudentList(studentList)
        } else {
            // Filter the studentList based on the query (e.g., by student name)
            val filteredList = studentList.filter { student ->
                student.FirstName?.contains(query, ignoreCase = true) == true ||
                        student.RegNumber?.contains(query, ignoreCase = true) == true
                // Add more fields as needed for searching
            }
            // Update the RecyclerView with the filtered list
            studentAdapter.updateStudentList(filteredList)
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun getStudentsList() {
        /*lifecycleScope.launch {
            utils.startLoadingAnimation()
            val className = sharedPrefManager.getClass()!!.ClassName
            val sectionID = sharedPrefManager.getSectionFromShared()!!.ID

           //if (sectionID != null && className != null) {
                studentViewModel.getStudentsList(className, sectionI D)
                    .addOnCompleteListener { task ->


                        if (task.isSuccessful) {
                            val documents = task.result
                            utils.endLoadingAnimation()
                            for (document in documents) {
                                val student = document.toObject(StudentModel::class.java)
                                studentList.add(student)
                            }

                            studentList.sortBy { it.RegNumber }


                            studentAdapter.notifyDataSetChanged()


                        } else {
                            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

          //  }

        }*/
    }
}
