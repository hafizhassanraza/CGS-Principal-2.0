package com.enfotrix.cgs_principal_applic.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal_applic.Models.AttendenceModel
import com.enfotrix.cgs_principal_applic.Models.StudentModel
import com.enfotrix.cgs_principal_applic.R
import com.enfotrix.cgs_principal_applic.ui.ActivityStudentProfile
import com.google.gson.Gson

class AttendanceRecAdapter(private val context: Context, private var attendanceRecList: List<AttendenceModel>, private var studentList: List<StudentModel>, ) : RecyclerView.Adapter<AttendanceRecAdapter.ViewHolder>() {
    var sortedList=studentList.sortedBy { it.RegNumber }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attendance_list, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val attendenceModel = attendanceRecList[position]
        // Set data from the StudentModel to the corresponding TextViews
         val studentModel = studentList.firstOrNull { it.StudentId == attendenceModel.StudentID }

        if(studentModel!=null){
            holder.regNO.text = studentModel.RegNumber
            holder.studentName.text = studentModel.FirstName
            holder.status.text = attendenceModel.Status
            holder.layAttendance.setOnClickListener {
                val selectedStudentList = mutableListOf<StudentModel>()
                selectedStudentList.add(studentModel)

                val gson = Gson()
                val studentListJson = gson.toJson(selectedStudentList)
                val intent = Intent(context, ActivityStudentProfile::class.java)
                intent.putExtra("selectedStudentList", studentListJson)
                intent.putExtra("Section",studentModel.CurrentClass)
                context.startActivity(intent)
            }
            // Set text color based on status
            when (attendenceModel.Status) {
                "Present" -> holder.status.setTextColor(context.resources.getColor(R.color.green)) // Change R.color.green to your desired color resource
                "Absent" -> {
                    holder.status.setTextColor(context.resources.getColor(R.color.red))
                    //holder.regNO.setTextColor(context.resources.getColor(R.color.white))
                    //holder.studentName.setTextColor(context.resources.getColor(R.color.white))
                    //holder.layAttendance.setBackgroundColor(context.resources.getColor(R.color.red))
                } // Change R.color.red to your desired color resource
                "Leave" -> {
                    holder.status.setTextColor(context.resources.getColor(R.color.blue))
                    //holder.regNO.setTextColor(context.resources.getColor(R.color.white))
                    //holder.studentName.setTextColor(context.resources.getColor(R.color.white))
                    //holder.layAttendance.setBackgroundColor(context.resources.getColor(R.color.blue ))
                } // Change R.color.blue to your desired color resource
                else -> holder.status.setTextColor(context.resources.getColor(android.R.color.black))
            }
        }



    }

    override fun getItemCount(): Int {
        return attendanceRecList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val regNO: TextView = itemView.findViewById(R.id.RegNumber)
        val studentName: TextView = itemView.findViewById(R.id.StudentName)
        val status: TextView = itemView.findViewById(R.id.StudentStatus)
        val layAttendance: LinearLayout = itemView.findViewById(R.id.layAttendance)
    }


}