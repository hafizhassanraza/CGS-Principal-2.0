package com.enfotrix.cgs_principal.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.Models.StudentRemarksModel
import com.enfotrix.cgs_principal.Models.StudentViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.databinding.ActivityStudentProfileBinding
import com.enftorix.cgs_principal.Constants
import com.google.common.reflect.TypeToken
import com.google.firebase.Timestamp
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ActivityStudentProfile : AppCompatActivity() {
    private lateinit var binding: ActivityStudentProfileBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private var constants = Constants()
    private val studentViewModel: StudentViewModel by viewModels ()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityStudentProfile
        sharedPrefManager = SharedPrefManager(mContext)



        binding.imageArrowBack.setOnClickListener {
            super.onBackPressed()
        }



        val gson = Gson()
        val studentListJson = intent.getStringExtra("selectedStudentList")
        val type = object : TypeToken<List<StudentModel>>() {}.type
        val selectedStudentList = gson.fromJson<List<StudentModel>>(studentListJson, type)

        if (selectedStudentList != null) {
//            Toast.makeText(mContext, ""+selectedStudentList.size, Toast.LENGTH_SHORT).show()
        }




        if (selectedStudentList != null) {
            displaySelectedStudents(selectedStudentList)
        } else {
            Toast.makeText(mContext, "No selected students found", Toast.LENGTH_SHORT).show()
        }

        binding.btnCall.setOnClickListener {
            for(student in selectedStudentList){
                val phone=student?.FatherPhoneNumber
                openDialer(phone.toString())
            }
        }
        binding.btnMessage.setOnClickListener {
            for (student in selectedStudentList) {
                val phone = student?.FatherPhoneNumber
                openMessagingApp(phone.toString())
            }
        }
        binding.btnWataApp.setOnClickListener {
            Toast.makeText(mContext, "Available soon", Toast.LENGTH_SHORT).show()
        }
        binding.addRemarks.setOnClickListener {
            var studentID:String=""
            for (student in selectedStudentList) {
                studentID=student.StudentId
            }
            dialogBox(studentID)
        }
    }
    private fun openDialer(phoneNumber: String) {
        val uri = Uri.parse("tel:$phoneNumber")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        startActivity(intent)
    }
    private fun openMessagingApp(phoneNumber: String) {
        val uri = Uri.parse("smsto:$phoneNumber")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        startActivity(intent)
    }






    private fun displaySelectedStudents(selectedStudentList: List<StudentModel>) {
        for (student in selectedStudentList) {
            binding.studentName.text = student?.FirstName ?: ""
            binding.studentRegNumber.text = student?.RegNumber ?: ""
            binding.studentAddress.text = student?.Address ?: ""
            binding.studentDob.text = student?.DOB ?: ""
            binding.contactNumber.text = student?.FatherPhoneNumber ?: ""
            binding.studentEmail.text = student?.Email ?: ""
            binding.EmergencyContact.text = student?.FatherPhoneNumber ?: ""
            binding.studentClass.text = intent.getStringExtra("Section")


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

            // Set the text to be displayed in the dialog
        studentNameDialog.text = binding.studentName.text
        studentRegDialog.text = binding.studentRegNumber.text

        // Set an OnClickListener for the Button to navigate to a new activity

        val selectedStudentList= mutableListOf<StudentModel>()
        selectedStudentList.add(studentViewModel.getStudentModel(studentID))
        val student=studentViewModel.getStudentModel(studentID)
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
                studentViewModel.saveRemarks(StudentRemarksModel(
                    student.StudentId,
                    getCurrentDate(),
                    Timestamp.now(),
                    "",
                    remarkDetail.text.toString(),
                    "Principal",
                    selectedRadioButtonId.toString()
                ))
                val dialog = builder.create()
                dialog.dismiss()
            }
            else if (selectedRadioButtonId==null){ alert.visibility=View.VISIBLE}
            else if (remarkDetail.text.isEmpty()){
                alertRemarks.visibility=View.VISIBLE
            }
            else {Toast.makeText(mContext, "Not saved", Toast.LENGTH_SHORT).show()}
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


}