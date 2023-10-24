package com.enfotrix.cgs_sargodha_student.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_sargodha_student.Models.AttendanceModel
import com.enfotrix.cgs_sargodha_student.Models.ResultModel
import com.enfotrix.cgs_sargodha_student.R

class AdapterResult(private val resultList: MutableList<ResultModel>): RecyclerView.Adapter<AdapterResult.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = resultList[position]

        // sets the text to the textview from our itemHolder class
        holder.subject.text = ItemsViewModel.subjectId
        holder.obtainedMarks.text = ItemsViewModel.obtainMarks
        holder.totalMarks.text = ItemsViewModel.totalMarks
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subject: TextView = itemView.findViewById(R.id.subjectTextView)
        val obtainedMarks: TextView = itemView.findViewById(R.id.obtainedMarksTextView)
        val totalMarks: TextView = itemView.findViewById(R.id.totalMarksTextView)
    }
}