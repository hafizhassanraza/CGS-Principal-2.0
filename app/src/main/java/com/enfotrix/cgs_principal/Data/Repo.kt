package com.enfotrix.cgs_principal.Data
import android.content.Context
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enftorix.cgs_principal.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class Repo(val context: Context) {
    private var constants = Constants()
    private var sharedPrefManager = SharedPrefManager(context)
    private val db = Firebase.firestore

//         private val token=sharedPrefManager.getToken()


    private var ADMIN_COLLECTION = db.collection(constants.ADMIN_COLLECTION)
    private var CLASS_COLLECTION = db.collection(constants.CLASS_COLLECTION)
    private var SECTION_COLLECTION = db.collection(constants.SECTION_COLLECTION)
    private var ATTENDANCE_COLLECTION=db.collection(constants.ATTENDANCE_REC_COLLECTION)
    private var STUDENT_COLLECTION=db.collection(constants.STUDENT_COLLECTION)
    private var DEVICE_TOKEN_COLLECTION=db.collection(constants.DEVICE_TOKEN_COLLECTION)
    private var EXAMS_COLLECTION=db.collection(constants.EXAMS_COLLECTIONS)
    private var SUBJECT_COLLECTION=db.collection(constants.SUBJECT_COLLECTION)
    private var RESULT_COLLECTION=db.collection(constants.RESULT_COLLECTION)


    fun checkLogin(Id: String,password:String):Task<QuerySnapshot>{

        return ADMIN_COLLECTION
                .whereEqualTo(constants.ADMINID,Id)
            .whereEqualTo(constants.PASSWORD,password)
            .get()
     }




    fun getAttendance(date:String): Task<QuerySnapshot> {
        return ATTENDANCE_COLLECTION.whereEqualTo(constants.FIELD_DATE,date).get()
    }
    fun getStudentAttendance(studentId: String , startOfMonth :String, endOfMonth :String): Task<QuerySnapshot> {
        return ATTENDANCE_COLLECTION.whereEqualTo(constants.STUDENT_ID, studentId)
            .whereGreaterThanOrEqualTo( constants.FIELD_ATTENDANCE_DATE_TIMESTAMP, startOfMonth)
            .whereLessThan(constants.FIELD_ATTENDANCE_DATE_TIMESTAMP, endOfMonth)
            .get()
    }
    fun getStudentAttendance(studentId: String ): Task<QuerySnapshot> {
        return ATTENDANCE_COLLECTION.whereEqualTo(constants.STUDENT_ID, studentId).get()
    }

    suspend fun getClasses(): Task<QuerySnapshot> {
        return CLASS_COLLECTION.get()
    }
    suspend fun getSections(): Task<QuerySnapshot> {
        return SECTION_COLLECTION.get()
    }
    suspend fun  getStudents() : Task<QuerySnapshot> {
        return STUDENT_COLLECTION.get()
    }
    fun getExams(): Task<QuerySnapshot> { return EXAMS_COLLECTION.get() }
    fun getSubjects():Task<QuerySnapshot>{ return SUBJECT_COLLECTION.get() }
    fun getResult(year:String,exam:String):Task<QuerySnapshot>{

        return RESULT_COLLECTION.whereEqualTo(constants.FIELD_RESULT_YEAR,year).whereEqualTo(constants.FIELD_RESULT_EXAM,exam).get()
    }
    fun getResult(studentID:String, year:String,exam:String):Task<QuerySnapshot>{

        return RESULT_COLLECTION
            .whereEqualTo(constants.STUDENT_ID,studentID)
            .whereEqualTo(constants.FIELD_RESULT_YEAR,year)
            .whereEqualTo(constants.FIELD_RESULT_EXAM,exam).get()
    }



    fun getDeviceToken(studentID:String): Task<QuerySnapshot> {


        return DEVICE_TOKEN_COLLECTION.whereEqualTo(constants.FIELD_STUDENT_ID,studentID).get()



    }



}