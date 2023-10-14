package com.enfotrix.cgs_principal.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.R

class AdapterStudentsList(private var studentList: List<StudentModel>): RecyclerView.Adapter<AdapterStudentsList.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterStudentsList.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.students_item, parent, false)
        return ViewHolder(view)    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: AdapterStudentsList.ViewHolder, position: Int) {

        val students=studentList[position]

        holder.regNO.text=students.RegNumber
        holder.name.text=students.FirstName
        holder.section.text=students.CurrentClass

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val regNO: TextView = itemView.findViewById(R.id.regNo)
        val name: TextView = itemView.findViewById(R.id.studentNames)
        val section: TextView = itemView.findViewById(R.id.section)
    }


    fun updateList(newList: List<StudentModel>) {
        studentList = newList
        notifyDataSetChanged()
    }


}