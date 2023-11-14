package com.enfotrix.cgs_principal_portal.Adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal_portal.Models.AttendenceModel
import com.enfotrix.cgs_principal_portal.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class AttendanceAdapter(private val attendanceList: ArrayList<AttendenceModel>) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.attendence_item_view, parent, false)
        return ViewHolder(itemView)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = attendanceList[position]

        holder.attendanceDate.text = currentItem.Date
        holder.attendanceDay.text = LocalDate.parse(currentItem.Date, DateTimeFormatter.ofPattern("dd-MM-yyyy")).dayOfWeek.toString()

        // Check if attendance is "Present" (case-insensitive) and change text color to green
        if (currentItem.Status.equals("Present", ignoreCase = true)) {
            holder.attendance.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
            holder.attendance.text=currentItem.Status
        }
        else if (currentItem.Status.equals("Absent", ignoreCase = true)) {
            holder.attendance.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            holder.attendance.text=currentItem.Status
        }
        else {
            holder.attendance.text=currentItem.Status
        }
    }

    override fun getItemCount(): Int {
        return attendanceList.size
    }

    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val attendanceDate: TextView = itemView.findViewById(R.id.attendanceDate)
        val attendanceDay: TextView = itemView.findViewById(R.id.attendanceDay)
        val attendance: TextView = itemView.findViewById(R.id.attendance)
    }
}
