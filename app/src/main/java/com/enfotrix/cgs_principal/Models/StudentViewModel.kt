package com.enfotrix.cgs_principal.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.enfotrix.cgs_principal.Data.Repo
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enftorix.cgs_principal.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class StudentViewModel(context: Application) : AndroidViewModel(context) {

    private val sharedPrefManager = SharedPrefManager(context)
    private val constants = Constants()
    private val repo = Repo(context)
    private val context = context
    suspend fun getStudents(): Task<QuerySnapshot> { return repo.getStudents() }
    fun putStudentsList (listStudents:List<StudentModel>){ sharedPrefManager.putStudentList(listStudents) }
    fun getStudentsList():  List<StudentModel> { return sharedPrefManager.getStudentList() }
    fun getStudentsList(sectionID: String):  List<StudentModel> { return sharedPrefManager.getStudentList().filter { studentModel -> studentModel.CurrentSection.equals(sectionID)  } }
    fun getStudentModel(studentID: String):  StudentModel { return sharedPrefManager.getStudentList().filter { studentModel -> studentModel.StudentId.equals(studentID) }.first()    }
    fun getStudentModel_ByRegNum(regNum: String):  StudentModel { return sharedPrefManager.getStudentList().filter { studentModel -> studentModel.RegNumber.equals(regNum) }.first()    }
    fun getStudentList_ByName(name: String):  List<StudentModel> { return sharedPrefManager.getStudentList().filter { studentModel -> studentModel.FirstName.toLowerCase().contains(name.toLowerCase()) } }


}