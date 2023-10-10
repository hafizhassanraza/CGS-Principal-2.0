package com.enfotrix.cgs_principal

import android.content.Context
import android.content.SharedPreferences
import com.enfotrix.cgs_principal.Models.ClassModel
import com.enfotrix.cgs_principal.Models.PrincipalModel
import com.enfotrix.cgs_principal.Models.SectionModel
import com.enftorix.cgs_principal.Constants
import com.google.gson.Gson

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
    fun getClass(): ClassModel? {
        val jsonString = sharedPref.getString(constants.KEY_CLASS_MODEL, null)
        return if (jsonString != null) {
            Gson().fromJson(jsonString, ClassModel::class.java)
        } else {
            null
        }
    }
    fun getSectionFromShared():SectionModel?{
        val jsonString = sharedPref.getString(constants.KEY_SECTION_MODEL, null)
        return if (jsonString != null) {
            Gson().fromJson(jsonString, SectionModel::class.java)
        } else {
            null
        }

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


}