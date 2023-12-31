package com.enfotrix.cgs_principal_applic.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.enfotrix.cgs_principal_applic.SharedPrefManager
import com.enftorix.cgs_principal.Constants
import com.enfotrix.cgs_principal_applic.Data.Repo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class PrincipalViewModel (context: Application) : AndroidViewModel(context)  {


    private val userRepo = Repo(context)
    private val sharedPrefManager = SharedPrefManager(context)
    private val contants = Constants()
    private val context = context
    fun checkLogin(name: String,password:String): Task<QuerySnapshot> {
        return userRepo.checkLogin(name,password)
    }
}