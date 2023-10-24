package com.enfotrix.cgs_principal.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.ModelSubject
import com.enfotrix.cgs_principal.Models.ResultModel
import com.enfotrix.cgs_principal.R


class AdapterStudentResult(
    private val resultList: MutableList<ResultModel>,
    private val subjectList: List<ModelSubject> // Change to List instead of MutableList
) : RecyclerView.Adapter<AdapterStudentResult.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.personal_result_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < resultList.size) {
            val student = resultList[position]

            // Find the subject with the matching ID
            val subjectResult = subjectList.firstOrNull { it.ID == student.subjectId }

            // Check if a matching subject was found
            if (subjectResult != null) {
                // Set the subject name
                holder.subject.text = subjectResult.SubjectName
                // Set obtained and total marks from the ResultModel
                holder.obtainedMarks.text = student.obtainMarks ?: ""
                holder.totalMarks.text = student.totalMarks ?: ""
            } else {
                // Handle the case where no matching subject is found
                holder.subject.text = "Subject Not Found"
                holder.obtainedMarks.text = student.obtainMarks ?: ""
                holder.totalMarks.text = student.totalMarks ?: ""
            }
        }
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
