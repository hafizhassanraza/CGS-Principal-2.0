package com.enfotrix.cgs_principal.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.StudentRemarksModel
import com.enfotrix.cgs_principal.R

class AdapterStudentRemarks(private val remarksList: ArrayList<StudentRemarksModel>) : RecyclerView.Adapter<AdapterStudentRemarks.ViewHolder>() {
    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val remark: TextView = itemView.findViewById(R.id.remarkDetails)
        val remarkBy: TextView = itemView.findViewById(R.id.remarkedBy)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_student_remarks, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterStudentRemarks.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return remarksList.size
    }
}