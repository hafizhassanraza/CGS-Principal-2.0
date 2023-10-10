package com.enfotrix.cgs_principal.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.enfotrix.cgs_principal.Models.ClassModel
import com.enfotrix.cgs_principal.Models.ClassViewModel
import com.enfotrix.cgs_principal.Models.SectionModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

class ActivitySplash : AppCompatActivity() {

    private val classViewModel: ClassViewModel by viewModels()

    private lateinit var mContext: Context
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager
    private val classesList = mutableListOf<ClassModel>()
    private val sectionList = mutableListOf<SectionModel>()
    private lateinit var dialog : Dialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        mContext = this@ActivitySplash;
        constants = Constants()
        sharedPrefManager = SharedPrefManager(mContext)
        getAllClasses()
        getAllSections()


        Timer().schedule(1500) {

            if (sharedPrefManager.isLoggedIn() == true) {
                startActivity(Intent(mContext, ActivityClasses::class.java))
                finish()
            } else if (sharedPrefManager.isLoggedIn() == false) {
                startActivity(Intent(mContext, ActivityLogin::class.java))
                finish()
            }

        }


    }

        fun getAllClasses(){

            lifecycleScope.launch {

                classViewModel.getAllClasses().addOnCompleteListener { task->
                    if(task.isSuccessful){
                        val documents=task.result
                        if (documents.size()>0){
                            var classModel:ClassModel?=null
                            for(document in documents){
                                classModel = document.toObject(ClassModel::class.java)
                                classesList.add(classModel)
                            }

                            sharedPrefManager.saveClass(classesList)
                            Toast.makeText(mContext, ""+classesList.size, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }

                }.addOnFailureListener {
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

            }
        }

    fun getAllSections(){

        lifecycleScope.launch {
            classViewModel.getAllSections().addOnCompleteListener { task->
                if(task.isSuccessful){
                    val documents=task.result
                    if (documents.size()>0){
                        var sectionModel:SectionModel?=null
                        for(document in documents){
                            sectionModel = document.toObject(SectionModel::class.java)
                            sectionList.add(sectionModel)
                        }

                        sharedPrefManager.saveSectionInShared(sectionList)
                    }
                }
                else{
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        }
    }






}