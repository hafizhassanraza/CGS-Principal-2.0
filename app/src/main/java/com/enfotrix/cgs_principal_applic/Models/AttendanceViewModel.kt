package com.enfotrix.cgs_principal_applic.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.enfotrix.cgs_principal_applic.Data.Repo
import com.enfotrix.cgs_principal_applic.SharedPrefManager
import com.enfotrix.cgs_principal_applic.Utils
import com.enftorix.cgs_principal.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class AttendanceViewModel (context: Application) : AndroidViewModel(context) {
    private val sharedPrefManager = SharedPrefManager(context)

    private val constants = Constants()
    private val repo = Repo(context)
    private val context = context
    private val utils = Utils(context)

     fun    getAttendanceRec(date:String): Task<QuerySnapshot> { return repo.getAttendance(date) }
    suspend fun getMonthlyAttendanceRec(studentId: String , date:String): Task<QuerySnapshot> {
        return repo.getStudentAttendance(studentId,utils.startOfMonth(date),utils.endOfMonth(date))
    }
    fun getAttendence(studentId: String):Task<QuerySnapshot>{
        return repo.getAttendence(studentId)
    }



}