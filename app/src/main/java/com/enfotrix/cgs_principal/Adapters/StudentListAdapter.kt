package com.enfotrix.cgs_principal.Adapters
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.ui.ActivityStudentProfile
import com.google.gson.Gson


class StudentListAdapter(
    private val context:Context,
    private var studentList: List<StudentModel>,
    private val calling:String
) : RecyclerView.Adapter<StudentListAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val studentModel = studentList[position]
        // Set data from the StudentModel to the corresponding TextViews
        holder.regNO.text = studentModel.RegNumber
        holder.studentName.text = studentModel.FirstName.lowercase()
        holder.serialNo.text= (position+1).toString()
        holder.cardView.setOnClickListener {
            val selectedStudentList = mutableListOf<StudentModel>()
            selectedStudentList.add(studentModel)

            val gson = Gson()
            val studentListJson = gson.toJson(selectedStudentList)
            if(calling=="StudentListActivity"){
                val intent = Intent(context, ActivityStudentProfile::class.java)
                intent.putExtra("selectedStudentList", studentListJson)
                context.startActivity(intent)
            }
            else{
//                val intent = Intent(context, ActivityResultSet::class.java)
//                intent.putExtra("selectedStudentList", studentListJson)
//                context.startActivity(intent)

            }

        }

    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val regNO: TextView = itemView.findViewById(R.id.regNos)
        val serialNo:TextView=itemView.findViewById(R.id.serialNo)
        val studentName: TextView = itemView.findViewById(R.id.StudentName)
        val cardView: CardView = itemView.findViewById(R.id.cardViewProfile)
    }

    fun updateStudentList(newList: List<StudentModel>) {
        studentList = newList
        notifyDataSetChanged()
    }



}