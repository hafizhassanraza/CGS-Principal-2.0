package com.enfotrix.cgs_principal.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.ClassModel
import com.enfotrix.cgs_principal.Models.SectionModel
import com.enfotrix.cgs_principal.R

class ClassesListAdapter(
    private val context: Context,
    private val classList: List<ClassModel>,
    private val sectionList: List<SectionModel>,
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
        val classModel = sectionList[position]

        holder.classes.text = classModel.ClassName
            holder.Sections.text = classModel.SectionName
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

}
