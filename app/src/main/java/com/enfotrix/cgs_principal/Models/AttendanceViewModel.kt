package com.enfotrix.cgs_principal.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.enfotrix.cgs_principal.Data.Repo
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enftorix.cgs_principal.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class AttendanceViewModel (context: Application) : AndroidViewModel(context) {
    private val sharedPrefManager = SharedPrefManager(context)

    private val constants = Constants()
    private val repo = Repo(context)
    private val context = context


    suspend fun saveAttendance(attendenceModel: AttendenceModel): Task<Void> {
        return repo.saveAttendance(attendenceModel)

    }




    suspend fun getAttendanceRec(date:String,sectionID: String): Task<QuerySnapshot> {

        return repo.getAttendanceRec(date,sectionID)

    }


    fun getDeviceToken(studentID:String): Task<QuerySnapshot> {

        return repo.getDeviceToken(studentID)

    }








    fun getTodayAttendance(date: String, sectionID: String ): Task<QuerySnapshot> {
        return repo.getTodayAttendance(date,sectionID)
    }












}