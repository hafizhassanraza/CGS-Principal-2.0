package com.enfotrix.cgs_principal.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.enfotrix.cgs_principal.Data.Repo
import com.enftorix.cgs_principal.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class AnnouncementViewModel (context: Application) : AndroidViewModel(context) {
    private val constants = Constants()
    private val repo =  Repo(context)
    private val context = Constants()
    private val announcementSnapshot = MutableLiveData<QuerySnapshot>()

    fun getAnnouncementModel(): Task<QuerySnapshot> {
        return repo.getAnnouncementModel()


    }

}