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

    suspend fun getStudentsList(className: String,sectionID: String): Task<QuerySnapshot> {

        return repo.getStudentsList(className,sectionID)
    }
    suspend fun getStudentsList(sectionID: String): Task<QuerySnapshot> {

        return repo.getStudentsList(sectionID)
    }


    fun putStudentList_Cache(list:List<StudentModel>): Boolean {
        return sharedPrefManager.putStudentList(list)
    }
    fun getStudentList_Cache(): List<StudentModel>{
        return sharedPrefManager.getStudentList()
    }






}