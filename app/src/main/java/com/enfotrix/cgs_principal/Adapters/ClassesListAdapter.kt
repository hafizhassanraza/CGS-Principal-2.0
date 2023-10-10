package com.enfotrix.cgs_principal.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.ModelItem
import com.enfotrix.cgs_principal.Models.ClassModel
import com.enfotrix.cgs_principal.Models.SectionModel
import com.enfotrix.cgs_principal.R
import com.google.gson.Gson

class ClassesListAdapter(
    private val context: Context,
    private val itemList: List<ModelItem>,
    private val itemClickListener: (ModelItem) -> Unit
) : RecyclerView.Adapter<ClassesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attandence_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        when (item) {
            is ClassModel -> {
                holder.className.text = item.ClassName
                holder.sectionName.text = ""
            }
            is SectionModel -> {
                holder.className.text = item.ClassName
                holder.sectionName.text = item.SectionName
            }
        }

        holder.cardView.setOnClickListener { itemClickListener(item) }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val className: TextView = itemView.findViewById(R.id.classfield)
        val sectionName: TextView = itemView.findViewById(R.id.section)
        val percentage: TextView = itemView.findViewById(R.id.percentage)
        val cardView: CardView = itemView.findViewById(R.id.cardViewclass)
    }
}
