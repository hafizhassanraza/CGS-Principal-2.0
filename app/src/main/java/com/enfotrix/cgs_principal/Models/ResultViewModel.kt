package com.enfotrix.cgs_principal.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.enfotrix.cgs_principal.Data.Repo
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.Utils
import com.enftorix.cgs_principal.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class ResultViewModel (context: Application) : AndroidViewModel(context) {
    private val sharedPrefManager = SharedPrefManager(context)

    private val constants = Constants()
    private val classRepo = Repo(context)
    private val context = context
    private val utils = Utils(context)







   fun getReult(): Task<QuerySnapshot> { return classRepo.getRsult() }
    fun getResultList(ExamID: String,year: String):  List<ResultModel> { return sharedPrefManager.getresultList().filter { resultModel -> resultModel.exam.equals(ExamID) && year.equals(year) } }

//    suspend fun getClassResult(studentId: String , date:String): Task<QuerySnapshot> {
//        return classRepo.getResult(exam)
//    }

}