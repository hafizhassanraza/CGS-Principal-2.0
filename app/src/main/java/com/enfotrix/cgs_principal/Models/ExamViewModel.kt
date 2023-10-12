package com.enfotrix.cgs_principal.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.enfotrix.cgs_principal.Data.Repo
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enftorix.cgs_principal.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import javax.security.auth.Subject

class ExamViewModel(context: Application) : AndroidViewModel(context) {
    private val sharedPrefManager = SharedPrefManager(context)
    private val constants = Constants()
    private val repo = Repo(context)
    private val context = context

    suspend fun getExams(): Task<QuerySnapshot> { return repo.getExams() }
    suspend fun getSubjects(): Task<QuerySnapshot> { return repo.getSubjects() }
    suspend fun getResult(year:String,exam:String): Task<QuerySnapshot> { return repo.getResult(year, exam) }
    suspend fun getResult( studentID: String, year:String,exam:String): Task<QuerySnapshot> { return repo.getResult(studentID,year, exam) }
    fun putExamsList(list: List<ExamModel>){ sharedPrefManager.putExamsList(list) }
    fun putSubjectsList(list: List<ModelSubject>){ sharedPrefManager.putSubjectList(list) }
    fun getExamsList(): List<ExamModel>{ return sharedPrefManager.getExamsList()}
    fun getSubjectsList(): List<ModelSubject>{ return sharedPrefManager.getSubjectsList()}
    fun getSubjectsList(sectionID: String): List<ModelSubject>{ return sharedPrefManager.getSubjectsList().filter { subject-> subject.SectionID.equals(sectionID) }}



}