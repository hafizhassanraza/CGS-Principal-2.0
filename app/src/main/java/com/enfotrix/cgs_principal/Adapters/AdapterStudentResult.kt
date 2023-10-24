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
    private val resultList: List<ResultModel>,
    private val subjectList: List<ModelSubject> // Change to List instead of MutableList
) : RecyclerView.Adapter<AdapterStudentResult.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.personal_result_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val resultModel = resultList[position] // loop
        val subjectName = subjectList.filter { it.ID == resultModel.subjectId }.first().SubjectName

        holder.subject.text = subjectName
        holder.obtainedMarks.text = resultModel.obtainMarks ?: ""
        holder.totalMarks.text = resultModel.totalMarks ?: ""



       /* // Check if a matching subject was found
        if (subjectModel != null) {
            // Set the subject name

        } else {
            // Handle the case where no matching subject is found
            holder.subject.text = "Subject Not Found"
            holder.obtainedMarks.text = result.obtainMarks ?: ""
            holder.totalMarks.text = result.totalMarks ?: ""
        }*/
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
