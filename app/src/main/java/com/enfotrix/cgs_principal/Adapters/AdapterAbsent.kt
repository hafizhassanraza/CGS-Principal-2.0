package com.enfotrix.cgs_principal.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.SectionModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.R

class AdapterAbsent(
    private val context: Context,
    private var studentList: List<StudentModel>,
    private val sectionlist:List<SectionModel>,
    private val phoneIconClickListener: PhoneIconClickListener
) : RecyclerView.Adapter<AdapterAbsent.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val regNO: TextView = itemView.findViewById(R.id.tvRegis)
        val student: TextView = itemView.findViewById(R.id.FstudentNames)
        val father: TextView = itemView.findViewById(R.id.ffatherName)
        val sections: TextView = itemView.findViewById(R.id.classes)
        val contactImage: ImageView = itemView.findViewById(R.id.contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.absent_students, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studentModel = studentList[position]

        // Set data from the StudentModel to the corresponding TextViews
        holder.regNO.text = studentModel.RegNumber
        holder.student.text = studentModel.FirstName
        holder.father.text = studentModel.FatherName
        val phone = studentModel.FatherPhoneNumber
        holder.contactImage.setImageResource(R.drawable.baseline_call)

        holder.contactImage.setOnClickListener {
            phoneIconClickListener.onPhoneIconClick(phone)
        }


        // Find the matching SectionModel based on the student ID
        val matchingSectionModel = sectionlist.find { it.ID == studentModel.CurrentSection }

        if (matchingSectionModel != null) {
            // Use the matching SectionModel to set section and class names
            val sectionName = matchingSectionModel.SectionName
            val className = matchingSectionModel.ClassName
            holder.sections.text = "$sectionName / $className"
        } else {
            // If no matching SectionModel is found, display a default message
            holder.sections.text = "Unknown Section / Class"
        }
    }
    interface PhoneIconClickListener {
        fun onPhoneIconClick(phoneNumber: String)
    }


}

