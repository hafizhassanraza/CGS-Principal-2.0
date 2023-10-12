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

    private  var ATTENDANCE_REC_COLLECTION=db.collection(constants.ATTENDANCE_REC_COLLECTION)

    fun checkLogin(Id: String,password:String):Task<QuerySnapshot>{

        return ADMIN_COLLECTION
                .whereEqualTo(constants.ADMINID,Id)
            .whereEqualTo(constants.PASSWORD,password)
            .get()
     }

    suspend fun getAllClasses(): Task<QuerySnapshot> {
        return CLASS_COLLECTION.get()
    }
     fun getTodayAttendance(date:String,Id:String): Task<QuerySnapshot> {
     return ATTENDANCE_COLLECTION.whereEqualTo(constants.FIELD_DATE,date)
         .whereEqualTo("sectionID",Id)
         .get()

    }


    suspend fun getClass(className: String): Task<QuerySnapshot> {
        return CLASS_COLLECTION.whereEqualTo(constants.FIELD_CLASS_NAME, className).get()
    }

    suspend fun getAllSections(): Task<QuerySnapshot> {
        return SECTION_COLLECTION.get()
    }
    suspend fun  getStudentsList(className: String,sectionID: String) : Task<QuerySnapshot> {

        return STUDENT_COLLECTION.whereEqualTo(constants.FIELD_CLASSNAME_IN_SECTION,className)
            .whereEqualTo(constants.FIELD_SECTION_ID,sectionID).get()

    }
    suspend fun  getStudentsList(sectionID: String) : Task<QuerySnapshot> {

        return STUDENT_COLLECTION.whereEqualTo(constants.FIELD_SECTION_ID,sectionID).get()

    }

    suspend fun saveAttendance(attendenceModel: AttendenceModel): Task<Void> {

        return db.collection(constants.ATTENDANCE_REC_COLLECTION).document(attendenceModel.Id).set(attendenceModel)

    }

    fun getAttendanceRec(date:String,sectionID: String): Task<QuerySnapshot> {
        return ATTENDANCE_REC_COLLECTION.whereEqualTo(constants.FIELD_SECTIONID, sectionID).whereEqualTo(constants.FIELD_DATE, date).get()

    }
    fun getDeviceToken(studentID:String): Task<QuerySnapshot> {


        return DEVICE_TOKEN_COLLECTION.whereEqualTo(constants.FIELD_STUDENT_ID,studentID).get()



    }



}