package com.enfotrix.cgs_principal.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.AdapterStudentRemarks
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.Models.StudentRemarksModel
import com.enfotrix.cgs_principal.Models.StudentViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.databinding.ActivityStudentRemarksBinding
import com.google.common.reflect.TypeToken
import com.google.firebase.Timestamp
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ActivityStudentRemarks : AppCompatActivity() {
    private lateinit var binding:ActivityStudentRemarksBinding
    private lateinit var recyclerView: RecyclerView
    private val studentViewModel:StudentViewModel by viewModels()
    private lateinit var mContext: Context
    private var remarksList = ArrayList<StudentRemarksModel>()
    private lateinit var adapter: AdapterStudentRemarks
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStudentRemarksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext= this@ActivityStudentRemarks

        recyclerView = binding.recyclerViewRemarks
        ///////////////////////////////for recycler View/////////////////////////////////
        recyclerView.layoutManager = LinearLayoutManager(this)
        remarksList= ArrayList()
        adapter = AdapterStudentRemarks(remarksList)
        recyclerView.adapter=adapter

        val gson = Gson()
        var studentID:String=""
        val studentListJson = intent.getStringExtra("selectedStudentList")
        val type = object : TypeToken<List<StudentModel>>() {}.type
        val selectedStudentList = gson.fromJson<List<StudentModel>>(studentListJson, type)
        // Now you can access the elements in the studentList
        for (student in selectedStudentList) {
            studentID =student.StudentId
            // ... (access other properties as needed)
        }
        addRemarks(studentID)


        binding.addRemarks.setOnClickListener {
            dialogBox(studentID)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun dialogBox(studentID: String){
        // Create and customize the AlertDialog
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_student_remarks_entry, null)
        builder.setView(dialogView)
        val studentNameDialog = dialogView.findViewById<TextView>(R.id.message_box_header)
        val studentRegDialog = dialogView.findViewById<TextView>(R.id.message_box_content)
        val alert = dialogView.findViewById<TextView>(R.id.remarkAlert)
        val alertRemarks = dialogView.findViewById<TextView>(R.id.remarkDetailAlert)
        val remarkDetail = dialogView.findViewById<EditText>(R.id.remarkDetails)
        val buttonGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)
        val positiveButton = dialogView.findViewById<RadioButton>(R.id.rbPositive)
        val negativeButton = dialogView.findViewById<RadioButton>(R.id.rbNegative)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)

        // Set an OnClickListener for the Button to navigate to a new activity

        val selectedStudentList= mutableListOf<StudentModel>()
        selectedStudentList.add(studentViewModel.getStudentModel(studentID))
        val student=studentViewModel.getStudentModel(studentID)

        // Set the text to be displayed in the dialog
        studentNameDialog.text = student.FirstName
        studentRegDialog.text = student.RegNumber

        // Initialize a variable to store the selected radio button's ID
        var selectedRadioButtonId: String? = null

        positiveButton.setOnClickListener {
            selectedRadioButtonId="positive"
        }
        negativeButton.setOnClickListener {
            selectedRadioButtonId="negative"
        }

        btnSave.setOnClickListener {
            if (selectedRadioButtonId != null && remarkDetail.text.isNotEmpty()) {
                studentViewModel.saveRemarks(
                    StudentRemarksModel(
                    student.StudentId,
                    getCurrentDate(),
                    Timestamp.now(),
                    "",
                    remarkDetail.text.toString(),
                    "Principal",
                    selectedRadioButtonId.toString()
                )
                )
                addRemarks(student.StudentId)
                val dialog = builder.create()
                dialog.dismiss()
            }
            else if (selectedRadioButtonId==null){ alert.visibility= View.VISIBLE}
            else if (remarkDetail.text.isEmpty()){
                alertRemarks.visibility= View.VISIBLE
            }
            else {
                Toast.makeText(mContext, "Not saved", Toast.LENGTH_SHORT).show()}
        }


//        // Add a button to close the dialog
//        builder.setPositiveButton("Save") { dialog, _ ->
//
//        }
//
//        // Set a negative button to allow dismissal when clicking outside or pressing back
//        builder.setNegativeButton("Cancel"){dialog,_ ->
//            dialog.dismiss()
//        }

        val dialog = builder.create()
        dialog.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }
    fun addRemarks(studentID: String){
        lifecycleScope.launch {
            remarksList.clear()
            studentViewModel.getRemarks(studentID).addOnCompleteListener {task->
                val result=task.result
                for (document in result){
                    var remarks=document.toObject(StudentRemarksModel::class.java)
                    remarksList.add(remarks)
                }
                remarksList.sortBy { it.date }
                Toast.makeText(mContext, ""+remarksList.size, Toast.LENGTH_SHORT).show()
                adapter.notifyDataSetChanged()
            }
        }
    }
}