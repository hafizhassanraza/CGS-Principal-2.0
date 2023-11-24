package com.enfotrix.cgs_principal_applic.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.enfotrix.cgs_principal_applic.Data.Repo
import com.enfotrix.cgs_principal_applic.SharedPrefManager
import com.enftorix.cgs_principal.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class StudentViewModel(context: Application) : AndroidViewModel(context) {

    private val sharedPrefManager = SharedPrefManager(context)
    private val constants = Constants()
    private val repo = Repo(context)
    private val context = context
    fun getStudents(): Task<QuerySnapshot> { return repo.getStudents() }
    fun putStudentsList (listStudents:List<StudentModel>){ sharedPrefManager.putStudentList(listStudents) }
    fun saveRemarks (studentRemarksModel: StudentRemarksModel){ repo.saveRemarks(studentRemarksModel) }
    fun getStudentsList():  List<StudentModel> { return sharedPrefManager.getStudentList().sortedBy { it.RegNumber } }
    fun getStudentsList(sectionID: String):  List<StudentModel> { return sharedPrefManager.getStudentList().filter { studentModel -> studentModel.CurrentSection.equals(sectionID)  } }
    fun getStudentModel(studentID: String):  StudentModel { return sharedPrefManager.getStudentList().filter { studentModel -> studentModel.StudentId.equals(studentID) }.first()    }
    fun getStudentModel_ByRegNum(regNum: String):  StudentModel { return sharedPrefManager.getStudentList().filter { studentModel -> studentModel.RegNumber.equals(regNum) }.first()    }
    fun getStudentList_ByName(name: String):  List<StudentModel> { return sharedPrefManager.getStudentList().filter { studentModel -> studentModel.FirstName.toLowerCase().contains(name.toLowerCase()) } }
    fun getRemarks(studentID: String):  Task<QuerySnapshot> { return repo.getRemarks(studentID) }


}