package com.enfotrix.cgs_principal

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.google.firebase.Timestamp
import com.google.type.Date

class Utils (val context: Context) {


    private var dialog= Dialog(context)

    fun startLoadingAnimation() {
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)
        dialog.show()
    }

    fun endLoadingAnimation() {
        dialog.dismiss()
    }

    fun startOfMonth  (date : String): String {

        // connvertion code
        return date }

    fun endOfMonth  (date : String): String {

        // connvertion code
        return date }



}