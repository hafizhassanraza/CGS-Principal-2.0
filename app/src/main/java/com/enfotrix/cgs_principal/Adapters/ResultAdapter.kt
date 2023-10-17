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
import com.enfotrix.cgs_principal.R

class ResultAdapterprivate(
    private val context: Context,
    private val sectionList: List<SectionModel>,
    private val resultList: List<ResultModel>,
    private var res: List<AttendenceModel>,

) : RecyclerView.Adapter<ResultAdapterprivate.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvClassSection: TextView = view.findViewById(R.id.tvClassSection)
        val tvPass: TextView = view.findViewById(R.id.tvPass)
        val tvfail: TextView = view.findViewById(R.id.tvFail)
        val tvPercentage: TextView = view.findViewById(R.id.tvPercentage)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attandence_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {




        val sectionModel = sectionList[position]
        var counterPass : Int=0
        var counterfail : Int=0


        counterPass = resultList.filter { result-> result.status.equals("pass") && result.sectionId.equals(sectionModel.ID) }.count()
        counterfail = resultList.filter { result-> result.status.equals("fail") && result.sectionId.equals(sectionModel.ID) }.count()

        val total = counterPass + counterfail
        if (total > 0) {
            val percent = (counterPass.toFloat() / total) * 100
            holder. tvPercentage.text = percent.toInt().toString()
        }
        else holder. tvPercentage.text = "N/A"

        holder.tvClassSection.text=sectionModel.ClassName+"-"+sectionModel.SectionName
        holder.tvPass.text= counterPass.toString()
        holder.tvfail.text= counterfail.toString()

    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

}