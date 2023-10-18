package com.enfotrix.cgs_principal.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.ExamModel
import com.enfotrix.cgs_principal.Models.ResultModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.R

class AdapterClassResult(
    private val context: Context,
    private val resultList: List<ResultModel>,
    private val studentList: List<StudentModel>,
) : RecyclerView.Adapter<AdapterClassResult.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val regNO: TextView = itemView.findViewById(R.id.RegNumber)
        val studentName: TextView = itemView.findViewById(R.id.StudentName)
        val status: TextView = itemView.findViewById(R.id.StudentStatus)
        val percentage: LinearLayout = itemView.findViewById(R.id.percentage)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.class_result, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resultModel = resultList[position]

        val studentModel = studentList.firstOrNull { it.StudentId == resultModel.studentId }

        if (studentModel != null) {
            holder.regNO.text = studentModel.RegNumber
            holder.studentName.text = studentModel.FirstName
            holder.status.text = resultModel.status

        }
    }
}