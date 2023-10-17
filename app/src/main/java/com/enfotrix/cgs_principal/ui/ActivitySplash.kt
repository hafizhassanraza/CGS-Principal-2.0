package com.enfotrix.cgs_principal.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.enfotrix.cgs_principal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal.Models.ClassModel
import com.enfotrix.cgs_principal.Models.ClassViewModel
import com.enfotrix.cgs_principal.Models.ExamModel
import com.enfotrix.cgs_principal.Models.ExamViewModel
import com.enfotrix.cgs_principal.Models.ModelSubject
import com.enfotrix.cgs_principal.Models.ResultModel
import com.enfotrix.cgs_principal.Models.ResultViewModel
import com.enfotrix.cgs_principal.Models.SectionModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.Models.StudentViewModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

class ActivitySplash : AppCompatActivity() {

    private val classViewModel: ClassViewModel by viewModels()
    private val studentViewModel: StudentViewModel by viewModels()
    private val examViewModel: ExamViewModel by viewModels()
    private val resultViewModel: ResultViewModel by viewModels()
    private val attendanceViewModel: AttendanceViewModel by viewModels()

    private lateinit var mContext: Context
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager
    private val classesList = mutableListOf<ClassModel>()
    private val sectionList = mutableListOf<SectionModel>()
    private lateinit var dialog : Dialog



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        mContext = this@ActivitySplash;
        constants = Constants()
        sharedPrefManager = SharedPrefManager(mContext)
























        Timer().schedule(1000) {


            lifecycleScope.launch {

                classViewModel.getClasses().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sharedPrefManager.putClassList(task.result.map { it.toObject(ClassModel::class.java) })
                        lifecycleScope.launch {

                            resultViewModel.getReult().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    sharedPrefManager.putResultList(task.result.map {
                                        it.toObject(
                                            ResultModel::class.java
                                        )
                                    })


                                    lifecycleScope.launch {

                                        classViewModel.getSections().addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                sharedPrefManager.putSectionList(task.result.map {
                                                    it.toObject(
                                                        SectionModel::class.java
                                                    )
                                                })


                                                lifecycleScope.launch {
                                                    examViewModel.getSubjects()
                                                        .addOnCompleteListener { task ->
                                                            if (task.isSuccessful) {
                                                                sharedPrefManager.putSubjectList(
                                                                    task.result.map {
                                                                        it.toObject(ModelSubject::class.java)
                                                                    })




                                                                lifecycleScope.launch {
                                                                    examViewModel.getExams()
                                                                        .addOnCompleteListener { task ->
                                                                            if (task.isSuccessful) {
                                                                                sharedPrefManager.putExamsList(
                                                                                    task.result.map {
                                                                                        it.toObject(
                                                                                            ExamModel::class.java
                                                                                        )
                                                                                    })




                                                                                lifecycleScope.launch {
                                                                                    studentViewModel.getStudents()
                                                                                        .addOnCompleteListener { task ->
                                                                                            if (task.isSuccessful) {


                                                                                                sharedPrefManager.putStudentList(
                                                                                                    task.result.map {
                                                                                                        it.toObject(
                                                                                                            StudentModel::class.java
                                                                                                        )
                                                                                                    })

                                                                                                if (sharedPrefManager.isLoggedIn() == true) {
                                                                                                    startActivity(
                                                                                                        Intent(
                                                                                                            mContext,
                                                                                                            ActivityAttendance::class.java
                                                                                                        )
                                                                                                    )
                                                                                                    finish()
                                                                                                } else if (sharedPrefManager.isLoggedIn() == false) {
                                                                                                    startActivity(
                                                                                                        Intent(
                                                                                                            mContext,
                                                                                                            ActivityLogin::class.java
                                                                                                        )
                                                                                                    )
                                                                                                    finish()
                                                                                                }


                                                                                            }


                                                                                        }
                                                                                        .addOnFailureListener {
                                                                                            Toast.makeText(
                                                                                                mContext,
                                                                                                "Something went wrong",
                                                                                                Toast.LENGTH_SHORT
                                                                                            ).show()
                                                                                        }

                                                                                }


                                                                            }


                                                                        }.addOnFailureListener {
                                                                            Toast.makeText(
                                                                                mContext,
                                                                                "Something went wrong",
                                                                                Toast.LENGTH_SHORT
                                                                            ).show()
                                                                        }

                                                                }


                                                            }


                                                        }.addOnFailureListener {
                                                            Toast.makeText(
                                                                mContext,
                                                                "Something went wrong",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }

                                                }


                                            }


                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                mContext,
                                                "Something went wrong",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    }

                                }


                            }.addOnFailureListener {
                                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }


                    }


                }


                /*suspend fun load(){



         lifecycleScope.launch {
             delay(1000) // Wait for 1 second

             try {
                 val classesTask = classViewModel.getClasses().await()
                 if (classesTask.isSuccessful) {
                     sharedPrefManager.putClassList(classesTask.result.map { it.toObject(ClassModel::class.java) })

                     val sectionsTask = classViewModel.getSections().await()
                     if (sectionsTask.isSuccessful) {
                         sharedPrefManager.putSectionList(sectionsTask.result.map { it.toObject(SectionModel::class.java) })

                         val subjectsTask = examViewModel.getSubjects().await()
                         if (subjectsTask.isSuccessful) {
                             sharedPrefManager.putSubjectList(subjectsTask.result.map { it.toObject(ModelSubject::class.java) })

                             val examsTask = examViewModel.getExams().await()
                             if (examsTask.isSuccessful) {
                                 sharedPrefManager.putExamsList(examsTask.result.map { it.toObject(ExamModel::class.java) })

                                 val studentsTask = studentViewModel.getStudents().await()
                                 if (studentsTask.isSuccessful) {
                                     sharedPrefManager.putStudentList(studentsTask.result.map { it.toObject(StudentModel::class.java) })

                                     if (sharedPrefManager.isLoggedIn() == true) {
                                         startActivity(Intent(mContext, ActivityAttendance::class.java))
                                         finish()
                                     } else if (sharedPrefManager.isLoggedIn() == false) {
                                         startActivity(Intent(mContext, ActivityLogin::class.java))
                                         finish()
                                     }
                                 } else {
                                     Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                                 }
                             } else {
                                 Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                             }
                         } else {
                             Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                         }
                     } else {
                         Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                     }
                 } else {
                     Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                 }
             } catch (e: Exception) {
                 e.printStackTrace()
                 Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
             }
         }

    }*/


            }
        }
    }
}