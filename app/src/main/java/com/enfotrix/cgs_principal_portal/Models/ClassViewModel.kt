package com.enfotrix.cgs_principal_portal.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.enfotrix.cgs_principal_portal.Data.Repo
import com.enfotrix.cgs_principal_portal.SharedPrefManager
import com.enftorix.cgs_principal.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class ClassViewModel(context: Application) : AndroidViewModel(context) {

    private val sharedPrefManager = SharedPrefManager(context)
    private val constants = Constants()
    private val classRepo = Repo(context)
    private val context = context


    suspend fun getClasses(): Task<QuerySnapshot> { return classRepo.getClasses() }
    suspend fun getSections(): Task<QuerySnapshot> { return classRepo.getSections() }
    fun putClassList(list: List<ClassModel>){ sharedPrefManager.putClassList(list) }
    fun putSectionList(list: List<SectionModel>){ sharedPrefManager.putSectionList(list) }
    fun getClassList(): List<ClassModel>{ return sharedPrefManager.getClassList()}

    fun getSectionList(): List<SectionModel>{ return sharedPrefManager.getSectionList()}
    fun getSectionList(classID:String): List<SectionModel>{ return sharedPrefManager.getSectionList().filter { section-> section.ClassID.equals(classID) }}
    fun getSectionModel(sectionID:String): SectionModel{ return sharedPrefManager.getSectionList().filter { section-> section.ID.equals(sectionID) }.first()}
    fun getClassModel(classID:String): ClassModel{ return sharedPrefManager.getClassList().filter { class_-> class_.Id.equals(classID) }.first()}

}