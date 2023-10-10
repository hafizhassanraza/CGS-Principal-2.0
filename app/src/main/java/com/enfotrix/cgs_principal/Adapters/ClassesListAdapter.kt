package com.enfotrix.cgs_principal.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.ClassModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_teacher_portal.Models.AttendenceModel

class ClassesListAdapter(
    private val context: Context,
    private val classList: ClassModel?,
    private val sectionList: ClassModel?,
    private val attandanceList: List<AttendenceModel>

    ) : RecyclerView.Adapter<ClassesListAdapter.ViewHolder>() {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val classes: TextView = view.findViewById(R.id.classfield)
        val Sections: TextView = view.findViewById(R.id.section)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attandence_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < classList.size) {
            val classModel = classList[position]
            holder.classes.text = classModel.ClassName
            holder.Sections.text = "" // Clear the section text if no section for this class
        } else {
            val sectionModel = sectionList[position - classList.size]
            holder.Sections.text = sectionModel.SectionName
            holder.classes.text = "" // Clear the class text if no class for this section
        }
    }


    override fun getItemCount(): Int {
        return sectionList.size+classList.size
    }


}
