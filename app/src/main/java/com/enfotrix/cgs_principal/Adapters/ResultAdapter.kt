package com.enfotrix.cgs_principal.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.ClassModel
import com.enfotrix.cgs_principal.Models.ResultModel
import com.enfotrix.cgs_principal.Models.SectionModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.R

class ResultAdapter(
    private val context: Context,
    private val sectionList: List<SectionModel>,
    private val resultList: List<ResultModel>,
    private var studentList: List<StudentModel>,

) : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvClassSection: TextView = view.findViewById(R.id.tvClassSection)
        val tvPass: TextView = view.findViewById(R.id.tvPass)
        val tvfail: TextView = view.findViewById(R.id.tvFail)
        //val tvPercentage: TextView = view.findViewById(R.id.tvPercentage)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.result_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        /*val sectionModel = sectionList[position]
        var counterPass: Int = 0
        var counterFail: Int = 0
        var tempSectionStudentList =
            studentList.filter { studentModel -> studentModel.CurrentSection.equals(sectionModel.ID) }
                .toList()

        for (student in tempSectionStudentList) {

            var tempStudentListResult =
                resultList.filter { resultModel -> resultModel.studentId.equals(student.StudentId) }

            var totalObtain: Int = 0;
            var total: Int = 0;
            for (result in tempStudentListResult) {
                total += result.totalMarks?.toInt() ?: 0
                totalObtain += result.obtainMarks?.toInt() ?: 0
            }
            if (total > 0) {
                val percent = (totalObtain.toFloat() / total) * 100
                var percent_int = percent.toInt()

                if (percent_int > 33) counterPass++
                else counterFail++
            }

        }*/

        val sectionModel = sectionList[position]
        var counterPass = 0
        var counterFail = 0

        val tempSectionStudentList = studentList.filter { it.CurrentSection == sectionModel.ID }

        for (student in tempSectionStudentList) {
            val tempStudentListResult = resultList.filter { it.studentId == student.StudentId }

            val totalObtain = tempStudentListResult.sumBy { it.obtainMarks?.toInt() ?: 0 }
            val total = tempStudentListResult.sumBy { it.totalMarks?.toInt() ?: 0 }

            if (total > 0) {
                val percent = (totalObtain.toFloat() / total) * 100
                val percentInt = percent.toInt()

                if (percentInt > 33) counterPass++
                else counterFail++
            }
        }








        /*val total = counterPass + counterfail
        if (total > 0) {
            val percent = (counterPass.toFloat() / total) * 100
            holder. tvPercentage.text = percent.toInt().toString()
        }
        else holder. tvPercentage.text = "N/A"*/





        holder.tvClassSection.text=sectionModel.ClassName+"-"+sectionModel.SectionName
        holder.tvPass.text= counterPass.toString()
        holder.tvfail.text= counterFail.toString()

    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

}