package com.enfotrix.cgs_principal_applic.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.enfotrix.cgs_principal_applic.Adapters.AdapterStudentsList
import com.enfotrix.cgs_principal_applic.Models.ClassViewModel
import com.enfotrix.cgs_principal_applic.Models.StudentModel
import com.enfotrix.cgs_principal_applic.Models.StudentViewModel
import com.enfotrix.cgs_principal_applic.R
import com.enfotrix.cgs_principal_applic.SharedPrefManager
import com.enfotrix.cgs_principal_applic.databinding.ActivityStudentReportsBinding
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ActivityStudentReports : AppCompatActivity(),AdapterStudentsList.StudentClickListener {
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

        /////////initialize UI elements//////////
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

        svName.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search submission for svName here (optional)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterStudentsByName(newText.orEmpty())
                return true
            }
        })

        // Set up the query listener for svRegistration
        svRegistration.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search submission for svRegistration here (optional)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterStudentsByRegistration(newText.orEmpty())
                return true
            }
        })
    }

    private fun filterStudentsByName(nameQuery: String) {
        val filteredList = if (nameQuery.isBlank()) {
            studentsList
        } else {
            studentsList.filter { student ->
                student.FirstName.contains(nameQuery, ignoreCase = true)
            }
        }
        studentsAdapter.updateList(filteredList)
    }

    private fun filterStudentsByRegistration(registrationQuery: String) {
        val filteredList = if (registrationQuery.isBlank()) {
            studentsList
        } else {
            studentsList.filter { student ->
                student.RegNumber.contains(registrationQuery, ignoreCase = true)
            }
        }
        studentsAdapter.updateList(filteredList)
    }

    fun settingRV(){
        ///////////set the recycler view//////////////
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        studentsList.clear()
        studentsList= sharedPrefManager.getStudentList() as ArrayList<StudentModel>
        studentsList.sortBy { it.RegNumber }
        studentsAdapter = AdapterStudentsList(studentsList,this)
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
                //Toast.makeText(mContext, ""+classID, Toast.LENGTH_SHORT).show()
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
        studentsList.sortBy { it.RegNumber }

        // Notify the adapter that the data has changed
        studentsAdapter.notifyDataSetChanged()
    }
    fun dialogBox(studentID: String,studentName: String,studentReg: String){
        // Create and customize the AlertDialog
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_student_reports, null)
        builder.setView(dialogView)
        val studentNameDialog = dialogView.findViewById<TextView>(R.id.message_box_header)
        val studentRegDialog = dialogView.findViewById<TextView>(R.id.message_box_content)
        val profileCard = dialogView.findViewById<CardView>(R.id.cvProfile)
        val attendanceCard = dialogView.findViewById<CardView>(R.id.cvAttendance)
        val reportCard = dialogView.findViewById<CardView>(R.id.cvReport)
        val resultCard = dialogView.findViewById<CardView>(R.id.cvresult)

        // Set the text to be displayed in the dialog
        studentNameDialog.text = studentName
        studentRegDialog.text = studentReg

        // Set an OnClickListener for the Button to navigate to a new activity

        val selectedStudentList= mutableListOf<StudentModel>()
        selectedStudentList.add(studentViewModel.getStudentModel(studentID))

        profileCard.setOnClickListener {
            val gson = Gson()
            val studentListJson = gson.toJson(selectedStudentList)
            val intent = Intent(mContext, ActivityStudentProfile::class.java)
            intent.putExtra("selectedStudentList", studentListJson)
            startActivity(intent)
        }
        attendanceCard.setOnClickListener {
            val gson = Gson()
            val studentListJson = gson.toJson(selectedStudentList)
            val intent = Intent(mContext, ActivityStudentAttendance::class.java)
            intent.putExtra("selectedStudentList", studentListJson)
            startActivity(intent)
        }
        reportCard.setOnClickListener {
            val gson = Gson()
            val studentListJson = gson.toJson(selectedStudentList)
            val intent = Intent(mContext, ActivityStudentRemarks::class.java)
            intent.putExtra("selectedStudentList", studentListJson)
            startActivity(intent)
        }
        resultCard.setOnClickListener {
            val gson = Gson()
            val studentListJson = gson.toJson(selectedStudentList)
            val intent = Intent(mContext, ActivityReportResult::class.java)
            intent.putExtra("selectedStudentList", studentListJson)
            intent.putExtra("studentId", studentID)
            startActivity(intent)
        }

        // Add a button to close the dialog
        builder.setPositiveButton("Back") { dialog, _ ->
            dialog.dismiss()
        }

        // Set a negative button to allow dismissal when clicking outside or pressing back
        builder.setNegativeButton("", null)

        val dialog = builder.create()
        dialog.show()
    }

    override fun onStudentClick(studentID: String, studentName: String, studentReg: String) {
        dialogBox(studentID,studentName,studentReg)
    }

}