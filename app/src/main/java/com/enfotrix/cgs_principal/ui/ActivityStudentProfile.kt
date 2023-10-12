package com.enfotrix.cgs_principal.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.databinding.ActivityStudentProfileBinding
import com.enftorix.cgs_principal.Constants
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class ActivityStudentProfile : AppCompatActivity() {
    private lateinit var binding: ActivityStudentProfileBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private var constants = Constants()

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

    }
    private fun openDialer(phoneNumber: String) {
        val uri = Uri.parse("tel:$phoneNumber")
        val intent = Intent(Intent.ACTION_DIAL, uri)
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


}