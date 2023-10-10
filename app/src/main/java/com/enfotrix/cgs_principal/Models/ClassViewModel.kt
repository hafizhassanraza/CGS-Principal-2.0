package com.enfotrix.cgs_principal.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.enfotrix.cgs_principal.Data.Repo
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enftorix.cgs_principal.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class ClassViewModel(context: Application) : AndroidViewModel(context) {

    private val sharedPrefManager = SharedPrefManager(context)
    private val constants = Constants()
    private val classRepo = Repo(context)
    private val context = context


    suspend fun getAllClasses(): Task<QuerySnapshot> {

        return classRepo.getAllClasses()

    }

    suspend fun getClass(className: String): Task<QuerySnapshot> {

        return classRepo.getClass(className)

    }

//    fun SaveClass(Class: ClassModel) {
//
//        sharedPrefManager.saveClass(Class)
//    }
    fun clearSharedPref(){
        sharedPrefManager.clearSharedPref()
    }
    suspend fun getAllSections(): Task<QuerySnapshot> {
        return classRepo.getAllSections()
    }

//    fun saveSectionInShared(Section:SectionModel){
//        sharedPrefManager.saveSectionInShared(Section)
//    }



}