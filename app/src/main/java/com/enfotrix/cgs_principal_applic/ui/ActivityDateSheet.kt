package com.enfotrix.cgs_principal_applic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal_applic.Adapters.AdapterDateSheet
import com.enfotrix.cgs_principal_applic.Models.DateSheetModel
import com.enfotrix.cgs_principal_applic.R
import com.enfotrix.cgs_principal_applic.databinding.ActivityDateSheetBinding


class  ActivityDateSheet : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dateSheetAdapter: AdapterDateSheet
    private lateinit var binding: ActivityDateSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDateSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            super.onBackPressed()
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)




        // Set up a LinearLayoutManager for the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Populate data
        val dateSheetData = arrayListOf(
            DateSheetModel("", "", "", ""),
            DateSheetModel("", "", "", ""),
            DateSheetModel("", "", "", ""),
            DateSheetModel("", "", "", ""),
            DateSheetModel("", "", "", ""),
            DateSheetModel("", "", "", ""),
            DateSheetModel("", "", "", " "),
            DateSheetModel("", "", "", ""),
            // Add more items as needed
        )

        dateSheetAdapter= AdapterDateSheet(dateSheetData)
        recyclerView.adapter = dateSheetAdapter

    }

}