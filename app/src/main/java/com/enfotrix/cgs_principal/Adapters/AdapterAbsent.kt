package com.enfotrix.cgs_principal.Adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.SectionModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.R

class AdapterAbsent(
    private val context: Context,
    private var studentList: List<StudentModel>,
    private val sectionlist: List<SectionModel>,
    private val absentList: MutableList<AttendenceModel>,
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
     Toast.makeText(parent.context, "debug", Toast.LENGTH_SHORT).show()
        val view = LayoutInflater.from(parent.context).inflate(R.layout.absent_students, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studentModel = studentList[position]
        val phone = studentModel.FatherPhoneNumber

        // Check if the student is absent by matching their StudentId
        val isAbsent = absentList.any { it.StudentID == studentModel.StudentId }

        if (isAbsent) {
            holder.regNO.text = studentModel.RegNumber
            holder.student.text = studentModel.FirstName
            holder.father.text = studentModel.FatherName
            holder.contactImage.setImageResource(R.drawable.baseline_call)

            holder.contactImage.setOnClickListener {
                phoneIconClickListener.onPhoneIconClick(phone)
            }

            val matchingSectionModel = sectionlist.find { it.ID == studentModel.CurrentSection }

            if (matchingSectionModel != null) {
                val sectionName = matchingSectionModel.SectionName
                val className = matchingSectionModel.ClassName
                holder.sections.text = "$sectionName / $className"
            } else {
                // If no matching SectionModel is found, display a default message
                holder.sections.text = "Unknown Section / Class"
            }
        } else {
            // If the student is not absent, hide the view or set any other appropriate behavior
            holder.itemView.visibility = View.GONE
        }
    }

    interface PhoneIconClickListener {
        fun onPhoneIconClick(phoneNumber: String)
    }
}
