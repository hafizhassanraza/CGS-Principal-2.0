package com.enfotrix.cgs_principal.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.AdapterStudentsList
import com.enfotrix.cgs_principal.Models.ClassViewModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.Models.StudentViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.databinding.ActivityStudentReportsBinding
import kotlinx.coroutines.launch

class ActivityStudentReports : AppCompatActivity() {
    private lateinit var binding: ActivityStudentReportsBinding
    private lateinit var rbName: RadioButton
    private lateinit var rbRegistration: RadioButton
    private lateinit var rbSection: RadioButton
    private lateinit var svName: SearchView
    private lateinit var svRegistration: SearchView
    private lateinit var spinnerSelectClass: AppCompatSpinner
    private lateinit var spinnerSelectSection: AppCompatSpinner
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var mContext: Context
    private lateinit var studentsAdapter:AdapterStudentsList
    private val classViewModel:ClassViewModel by viewModels()
    private val studentViewModel:StudentViewModel by viewModels ()
    private lateinit var studentsList: ArrayList<StudentModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStudentReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /////////initalize UI elements//////////
        init()

        ////////recycler view////////
        settingRV()

        // Set up listeners for radio buttons
        rbName.setOnClickListener {
            svName.visibility = SearchView.VISIBLE
            svRegistration.visibility = SearchView.GONE
            binding.spinnerClassLayout.visibility = View.GONE
            binding.spinnerSectionLayout.visibility = View.GONE
            settingRV()
        }

        rbRegistration.setOnClickListener {
            svName.visibility = SearchView.GONE
            svRegistration.visibility = SearchView.VISIBLE
            binding.spinnerClassLayout.visibility = View.GONE
            binding.spinnerSectionLayout.visibility = View.GONE
            settingRV()
        }

        rbSection.setOnClickListener {
            svName.visibility = SearchView.GONE
            svRegistration.visibility = SearchView.GONE
            binding.spinnerClassLayout.visibility = View.VISIBLE
            binding.spinnerSectionLayout.visibility = View.VISIBLE
        }

        /////////setting the spinner//////////
        fetchAndInitializeSpinner()
    }
    fun init(){
        rbName = findViewById(R.id.rbName)
        rbRegistration = findViewById(R.id.rbRegistration)
        rbSection = findViewById(R.id.rbSection)
        svName = findViewById(R.id.svName)
        svRegistration = findViewById(R.id.svRegistration)
        spinnerSelectClass = findViewById(R.id.spinnerSelectClass)
        spinnerSelectSection = findViewById(R.id.spinnerSelectSection)
        mContext=this@ActivityStudentReports
        sharedPrefManager= SharedPrefManager(mContext)
        studentsList= sharedPrefManager.getStudentList() as ArrayList<StudentModel>

    }
    fun settingRV(){
        ///////////set the recycler view//////////////
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        studentsList.clear()
        studentsList= sharedPrefManager.getStudentList() as ArrayList<StudentModel>
        studentsAdapter = AdapterStudentsList(studentsList)
        recyclerView.adapter = studentsAdapter
    }
    private fun fetchAndInitializeSpinner() {

            // Remove duplicates and update classList
            val dis = sharedPrefManager.getClassList().distinct()
        val distinct=dis.map { it.ClassName }
        //Toast.makeText(mContext, ""+distinct.size, Toast.LENGTH_SHORT).show()

            // Set up the Spinner
            val adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, distinct)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSelectClass.adapter = adapter

            // Select the first item in the spinner by default (assuming classList is not empty)
            if (distinct.isNotEmpty()) {
                spinnerSelectClass.setSelection(0)
            }

            // Add an item selected listener to toast the selected value
        spinnerSelectClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedValue = dis[position]
//                    Toast.makeText(mContext, "Class: $selectedValue", Toast.LENGTH_SHORT)
//                        .show()
                    getSection(selectedValue.Id)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle the case when nothing is selected (optional)
                }
            }
    }
    private fun getSection(classID:String) {
        lifecycleScope.launch {
            if (classID != null) {
                Toast.makeText(mContext, ""+classID, Toast.LENGTH_SHORT).show()
                val sections = classViewModel.getSectionList(classID)

                val distinct=sections.map { it.SectionName }
                //Toast.makeText(mContext, ""+distinct.size, Toast.LENGTH_SHORT).show()

                // Set up the Spinner
                val adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, distinct)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerSelectSection.adapter = adapter

                // Select the first item in the spinner by default (assuming classList is not empty)
                if (distinct.isNotEmpty()) {
                    spinnerSelectSection.setSelection(0)
                }

                // Add an item selected listener to toast the selected value
                spinnerSelectSection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedValue = sections[position]
                        val selectedClassId =selectedValue.ID
                        updateAdapterForSection(selectedClassId)
//                    Toast.makeText(mContext, "Class: $selectedValue", Toast.LENGTH_SHORT)
//                      .show()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Handle the case when nothing is selected (optional)
                    }
                }

            }
        }

    }
    fun updateAdapterForSection(selectedClass:String){
        val newStudentsList = studentViewModel.getStudentsList(selectedClass) as ArrayList<StudentModel>

        // Update the data source with the new list
        studentsList.clear()
        studentsList.addAll(newStudentsList)

        // Notify the adapter that the data has changed
        studentsAdapter.notifyDataSetChanged()
    }

}