package com.enfotrix.cgs_principal.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.enfotrix.cgs_principal.Data.Repo
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_teacher_portal.Constants
import com.enfotrix.cgs_teacher_portal.Data.Repo
import com.enfotrix.cgs_teacher_portal.SharedPrefManager
import com.enftorix.cgs_principal.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class ExamViewModel(context: Application) : AndroidViewModel(context) {
    private val sharedPrefManager = SharedPrefManager(context)
    private val constants = Constants()
    private val repo = Repo(context)
    private val context = context


   fun getExamsList(): Task<QuerySnapshot> {
        return repo.getExamsList()
    }

    fun getSubjectsList(sectionID:String): Task<QuerySnapshot> {

        return repo.getSubjectsList(sectionID)

    }


    fun saveSelectedExamInShared(examModel:ExamModel){

        sharedPrefManager.saveSelectedExamInShared(examModel)
    }

    fun getSelectedExamModel(examTerm:String): Task<QuerySnapshot> {
        return repo.getSelectedExamModel(examTerm)

    }

    fun getSubjectModel(subjectName:String,sectionID: String): Task<QuerySnapshot> {

        return repo.getSubjectModel(subjectName,sectionID)
    }


    fun saveSelectedSubjectInShared(subjectModel: SubjectModel){

        sharedPrefManager.saveSelectedSubjectInShared(subjectModel)
    }


      suspend  fun saveStudentResult(studentResult: ResultModel, documentID:String): Task<Void> {
      return repo.saveStudentResult(studentResult, documentID)


  }

    suspend fun  getResultList(selectedYear:String,subjectID:String,sectionID:String): Task<QuerySnapshot> {

        return repo.getResultList(selectedYear,subjectID,sectionID)

    }








}