package com.enfotrix.cgs_principal_portal.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal_portal.Models.StudentRemarksModel
import com.enfotrix.cgs_principal_portal.R

class AdapterStudentRemarks(private val remarksList: ArrayList<StudentRemarksModel>) : RecyclerView.Adapter<AdapterStudentRemarks.ViewHolder>() {
    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val remark: TextView = itemView.findViewById(R.id.remarkDetails)
        val remarkBy: TextView = itemView.findViewById(R.id.remarkedBy)
        val positive: ImageView = itemView.findViewById(R.id.imgPositive)
        val negative: ImageView= itemView.findViewById(R.id.imgNegative)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_student_remarks, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val remarks=remarksList[position]
        holder.remark.text=remarks.remarks
        holder.remarkBy.text="By:"+remarks.remarkedBy
        holder.date.text=remarks.date

        if (remarks.remarkStatus=="positive") holder.positive.visibility= View.VISIBLE
        else if (remarks.remarkStatus=="negative") holder.negative.visibility=View.VISIBLE
    }

    override fun getItemCount(): Int {
        return remarksList.size
    }
}