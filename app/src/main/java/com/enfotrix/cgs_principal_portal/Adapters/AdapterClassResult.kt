package com.enfotrix.cgs_principal_portal.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal_portal.Models.ResultModel
import com.enfotrix.cgs_principal_portal.Models.StudentModel
import com.enfotrix.cgs_principal_portal.R

class AdapterClassResult(
    private val context: Context,
    private val studentList: List<StudentModel>,
    private val resultList: List<ResultModel>,
    private val studentListener: onStudentClickListener

) : RecyclerView.Adapter<AdapterClassResult.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val regNO: TextView = itemView.findViewById(R.id.RegNumber)
        val studentName: TextView = itemView.findViewById(R.id.StudentName)
        val status: TextView = itemView.findViewById(R.id.StudentStatus)
        val marks: TextView = itemView.findViewById(R.id.tvmarks)
        val percentage: TextView = itemView.findViewById(R.id.percentage)
        val layout: LinearLayout = itemView.findViewById(R.id.layStudents)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.class_result, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < studentList.size) {
            val student = studentList[position]
//        val student = studentList[position]

            val tempStudentListResult = resultList.filter { it.studentId == student.StudentId }
            val totalObtain = tempStudentListResult.sumBy { it.obtainMarks?.toInt() ?: 0 }
            val total = tempStudentListResult.sumBy { it.totalMarks?.toInt() ?: 0 }

            if (total > 0) {
                val percent = (totalObtain.toFloat() / total) * 100
                val percentInt = percent.toInt()

                if (percentInt > 33) holder.status.text = "Pass"
                else holder.status.text = "Fail"

                holder.percentage.text = percent.toInt().toString()+"%"
                holder.marks.text = "$totalObtain/$total"
            }

            holder.regNO.text = student.RegNumber
            holder.studentName.text = student.FirstName
            holder.layout.setOnClickListener {
                studentListener .onStudentClicked( student.StudentId)
            }
        }
    }


    interface onStudentClickListener {
        fun onStudentClicked(studentId:String)

    }

}