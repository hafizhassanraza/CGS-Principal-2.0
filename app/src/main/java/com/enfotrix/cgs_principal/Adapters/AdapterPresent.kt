package com.enfotrix.cgs_principal.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.R

class AdapterPresent(
private val context: Context,
private var studentList: List<StudentModel>,
) : RecyclerView.Adapter<AdapterPresent>() {
    class ViewHolder {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPresent.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studentModel = studentList[position]
        // Set data from the StudentModel to the corresponding TextViews
       // holder.regNO.text = studentModel.RegNumber
      //  holder.studentName.text = studentModel.FirstName.lowercase()
     //   holder.serialNo.text= (position+1).toString()    }

}