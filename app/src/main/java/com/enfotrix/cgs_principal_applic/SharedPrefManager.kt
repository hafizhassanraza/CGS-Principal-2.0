package com.enfotrix.cgs_principal_applic

import android.content.Context
import android.content.SharedPreferences
import com.enfotrix.cgs_principal_applic.Models.AttendenceModel
import com.enfotrix.cgs_principal_applic.Models.ClassModel
import com.enfotrix.cgs_principal_applic.Models.ExamModel
import com.enfotrix.cgs_principal_applic.Models.ModelSubject
import com.enfotrix.cgs_principal_applic.Models.PrincipalModel
import com.enfotrix.cgs_principal_applic.Models.ResultModel
import com.enfotrix.cgs_principal_applic.Models.SectionModel
import com.enfotrix.cgs_principal_applic.Models.StudentModel
import com.enftorix.cgs_principal.Constants
import com.google.gson.Gson
import java.lang.reflect.Type

class SharedPrefManager(context: Context) {
    private var constants = Constants()

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun isLoggedIn(): Boolean {
        var isLoggedIn:Boolean=false
        if(sharedPref.getBoolean("isLoggedIn", false).equals(null)) isLoggedIn= false
        else if(sharedPref.getBoolean("isLoggedIn", false)==true) isLoggedIn =true
        return isLoggedIn
    }
    fun saveLoginAuth(principal: PrincipalModel?, loggedIn: Boolean){
        savePrincipal(principal)
        setLogin(loggedIn)
    }    fun setLogin(isLoggedIn: Boolean = false) {
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.commit()
    }
    fun savePrincipal(principal: PrincipalModel?) {
        if (principal != null) {
            editor.putString(constants.PRINCIPAL_MODEL, Gson().toJson(principal))
        } else {
            editor.remove(constants.PRINCIPAL_MODEL) // Remove the key if principal is null
        }
        editor.commit()
    }
    fun saveClass(classModel: MutableList<ClassModel>) {
        val jsonString = Gson().toJson(classModel)
        editor.putString(constants.KEY_CLASS_MODEL, jsonString)
        editor.apply() // Use apply() for asynchronous writes
    }



    fun clearSharedPref(){
        sharedPref.edit().clear().apply()
    }
    fun saveSectionInShared(Section: MutableList<SectionModel>) {
        sharedPref.edit().remove(constants.KEY_SECTION_MODEL)
        val jsonString = Gson().toJson(Section)
        editor.putString(constants.KEY_SECTION_MODEL, jsonString)
        editor.apply() // Use apply() for asynchronous writes
    }
    fun getSectionFromShared():SectionModel?{
        val jsonString = sharedPref.getString(constants.KEY_SECTION_MODEL, null)
        return if (jsonString != null) {
            Gson().fromJson(jsonString, SectionModel::class.java)
        } else {
            null
        }

    }
    fun getResult():ResultModel?{
        val jsonString = sharedPref.getString(constants.KEY_RESULT_MODEL, null)
        return if (jsonString != null) {
            Gson().fromJson(jsonString, ResultModel::class.java)
        } else {
            null
        }

    }
//    fun getSectionFromShared():SectionModel?{
//        val jsonString = sharedPref.getString(constants.KEY_SECTION_MODEL, null)
//        return if (jsonString != null) {
//            Gson().fromJson(jsonString, SectionModel::class.java)
//        } else {
//            null
//        }
//
//    }
    fun putStudentList(list:List<StudentModel>): Boolean {
        editor.putString("ListStudents", Gson().toJson(list))
        editor.commit()
        return true
    }
    fun putExamsList(list:List<ExamModel>): Boolean {
        editor.putString("ListExams", Gson().toJson(list))
        editor.commit()
        return true
    }
    fun putSubjectList(list:List<ModelSubject>): Boolean {
        editor.putString("ListSubjects", Gson().toJson(list))
        editor.commit()
        return true
    }
    fun putClassList(list:List<ClassModel>): Boolean {
        editor.putString("ListClasses", Gson().toJson(list))
        editor.commit()
        return true
    }
    fun putSectionList(list:List<SectionModel>): Boolean {
        editor.putString("ListSections", Gson().toJson(list))
        editor.commit()
        return true
    }
    fun putResultList(list:List<ResultModel>): Boolean {
        editor.putString("ListSections", Gson().toJson(list))
        editor.commit()
        return true
    }
    fun getresultList(): List<ResultModel>{

        val json = sharedPref.getString("ListResult", "") ?: ""
        val type: Type = object : com.google.gson.reflect.TypeToken<List<ResultModel?>?>() {}.getType()


        return if (!json.isNullOrEmpty()) {
            Gson().fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }
    fun putAttendanceListByDate(list:List<AttendenceModel>): Boolean {
        editor.putString("attendanceList", Gson().toJson(list))
        editor.commit()
        return true
    }
    fun getAttendanceListByDate(): List<AttendenceModel>{

        val json = sharedPref.getString("attendanceList", "") ?: ""
        val type: Type = object : com.google.gson.reflect.TypeToken<List<AttendenceModel?>?>() {}.getType()


        return if (!json.isNullOrEmpty()) {
            Gson().fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }


    fun getSectionList(): List<SectionModel>{

        val json = sharedPref.getString("ListSections", "") ?: ""
        val type: Type = object : com.google.gson.reflect.TypeToken<List<SectionModel?>?>() {}.getType()

        return if (!json.isNullOrEmpty()) {
            Gson().fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }
    fun getClassList(): List<ClassModel>{

        val json = sharedPref.getString("ListClasses", "") ?: ""
        val type: Type = object : com.google.gson.reflect.TypeToken<List<ClassModel?>?>() {}.getType()

        return if (!json.isNullOrEmpty()) {
            Gson().fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }
    fun getSubjectsList(): List<ModelSubject>{

        val json = sharedPref.getString("ListSubjects", "") ?: ""
        val type: Type = object : com.google.gson.reflect.TypeToken<List<ModelSubject?>?>() {}.getType()

        return if (!json.isNullOrEmpty()) {
            Gson().fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }
    fun getExamsList(): List<ExamModel>{

        val json = sharedPref.getString("ListExams", "") ?: ""
        val type: Type = object : com.google.gson.reflect.TypeToken<List<ExamModel?>?>() {}.getType()

        return if (!json.isNullOrEmpty()) {
            Gson().fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }
    fun getStudentList(): List<StudentModel>{

        val json = sharedPref.getString("ListStudents", "") ?: ""
        val type: Type = object : com.google.gson.reflect.TypeToken<List<StudentModel?>?>() {}.getType()

        return if (!json.isNullOrEmpty()) {
            Gson().fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }
    fun getClass(): ClassModel? {
        val jsonString = sharedPref.getString(constants.KEY_CLASS_MODEL, null)
        return if (jsonString != null) {
            Gson().fromJson(jsonString, ClassModel::class.java)
        } else {
            null
        }
    }



}