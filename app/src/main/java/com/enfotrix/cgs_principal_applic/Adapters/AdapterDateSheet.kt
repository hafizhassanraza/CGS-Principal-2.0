package com.enfotrix.cgs_principal_applic.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal_applic.Models.DateSheetModel
import com.enfotrix.cgs_principal_applic.R


class AdapterDateSheet(var datesheetList: ArrayList<DateSheetModel>) : RecyclerView.Adapter<AdapterDateSheet.ViewHolder>() {


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val date :TextView=itemView.findViewById(R.id.dateTextView)
        val day :TextView=itemView.findViewById(R.id.dayTextView)
        val timing :TextView=itemView.findViewById(R.id.timingTextView)
        val syllabus :TextView=itemView.findViewById(R.id.syllabusTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date_sheet, parent, false)

        return AdapterDateSheet.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datesheetList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateSheet=datesheetList[position]

        holder.date.text=dateSheet.date
        holder.day.text=dateSheet.day
        holder.timing.text=dateSheet.timing
        holder.syllabus.text=dateSheet.syllabus
    }
}