package com.enfotrix.cgs_principal_portal.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal_portal.Models.StudentModel
import com.enfotrix.cgs_principal_portal.R

class AdapterStudentsList(private var studentList: List<StudentModel>,
                          private val studentClickListener: StudentClickListener

): RecyclerView.Adapter<AdapterStudentsList.ViewHolder>() {
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
        holder.father.text=students.FatherName
        holder.section.text=students.CurrentClass

        holder.profile.setOnClickListener {
            studentClickListener.onStudentClick(students.StudentId,students.FirstName,students.RegNumber)
        }

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val regNO: TextView = itemView.findViewById(R.id.regNo)
        val name: TextView = itemView.findViewById(R.id.studentNames)
        val father: TextView = itemView.findViewById(R.id.ffatherName)
        val section: TextView = itemView.findViewById(R.id.section)
        val profile: CardView = itemView.findViewById(R.id.cardViewProfile)
    }

    interface StudentClickListener {
        fun onStudentClick(studentID: String, studentName:String, studentReg:String)
    }

    fun updateList(newList: List<StudentModel>) {
        studentList = newList
        notifyDataSetChanged()
    }


}