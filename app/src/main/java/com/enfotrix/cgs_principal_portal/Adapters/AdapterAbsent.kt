package com.enfotrix.cgs_principal_portal.Adapters
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal_portal.Models.AttendenceModel
import com.enfotrix.cgs_principal_portal.Models.SectionModel
import com.enfotrix.cgs_principal_portal.Models.StudentModel
import com.enfotrix.cgs_principal_portal.R
import com.enfotrix.cgs_principal_portal.ui.ActivityStudentProfile
import com.google.gson.Gson

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
        val card:CardView=itemView.findViewById(R.id.cardViewProfile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.absent_students, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val  studentModel= studentList[position]

        // Check if the student is absent by matching their StudentId
        val isAbsent = absentList.find { it.StudentID == studentModel.StudentId }



        if (isAbsent != null) {
            // Get father's phone number from the student model
            val phone = studentModel.FatherPhoneNumber

            // Set other values in the ViewHolder
            holder.regNO.text = studentModel.RegNumber
            holder.student.text = studentModel.FirstName
            holder.father.text = studentModel.FatherName

            holder.contactImage.setOnClickListener {
                phoneIconClickListener.onPhoneIconClick(phone)
            }
            holder.card.setOnClickListener {
                val selectedStudentList = mutableListOf<StudentModel>()
                selectedStudentList.add(studentModel)

                val gson = Gson()
                val studentListJson = gson.toJson(selectedStudentList)
                val intent = Intent(context, ActivityStudentProfile::class.java)
                intent.putExtra("selectedStudentList", studentListJson)
                context.startActivity(intent)

            }

            val matchingSectionModel = sectionlist.find { it.ID == studentModel.CurrentSection }

            if (matchingSectionModel != null) {
                val sectionName = matchingSectionModel.SectionName
                val className = matchingSectionModel.ClassName
                holder.sections.text = "$className / $sectionName"
            } else {
                // If no matching SectionModel is found, display a default message
                holder.sections.text = "Unknown Section / Class"
            }
        } else {
            // If the student is not absent, hide the view or set any other appropriate behavior
            holder.card.visibility=View.GONE
        }
    }


    interface PhoneIconClickListener {
        fun onPhoneIconClick(phoneNumber: String)
    }
}
