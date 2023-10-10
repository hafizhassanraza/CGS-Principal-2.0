package com.enfotrix.cgs_principal.Data
import android.content.Context
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
    fun checkLogin(Id: String,password:String):Task<QuerySnapshot>{

        return ADMIN_COLLECTION
                .whereEqualTo(constants.ADMINID,Id)
            .whereEqualTo(constants.PASSWORD,password)
            .get()
     }

    suspend fun getAllClasses(): Task<QuerySnapshot> {
        return CLASS_COLLECTION.get()
    }

    suspend fun getClass(className: String): Task<QuerySnapshot> {
        return CLASS_COLLECTION.whereEqualTo(constants.FIELD_CLASS_NAME, className).get()
    }

    suspend fun getSection(classID: String): Task<QuerySnapshot> {
        return SECTION_COLLECTION.get()
//        whereEqualTo(constants.FIELD_CLASS_ID, classID)
    }

}