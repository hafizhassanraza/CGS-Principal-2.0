package com.enfotrix.cgs_principal.ui
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.ClassesListAdapter
import com.enfotrix.cgs_principal.ModelItem
import com.enfotrix.cgs_principal.Models.ClassModel
import com.enfotrix.cgs_principal.Models.ClassViewModel
import com.enfotrix.cgs_principal.Models.SectionModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.databinding.ActivityClassesBinding
import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch

class ActivityClasses : AppCompatActivity() {
    private val classes: ClassViewModel by viewModels()
    private lateinit var binding: ActivityClassesBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private val constants = Constants()

    private var itemList = mutableListOf<ModelItem>()
    private lateinit var classAdapter: ClassesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityClasses
        sharedPrefManager = SharedPrefManager(mContext)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        classAdapter = ClassesListAdapter(mContext, itemList) { item ->
            // Handle item click here
        }
        recyclerView.adapter = classAdapter

        // Load classes and sections data
        getClassesAndSectionsList()
    }

    private fun getClassesAndSectionsList() {
        Toast.makeText(this, "helooo", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            val className = sharedPrefManager.getClass()!!.ClassName
            val sectionID = sharedPrefManager.getSectionFromShared()!!.ID

            Toast.makeText(this@ActivityClasses, ""+className, Toast.LENGTH_SHORT).show()


            classes.getAllClasses(className, sectionID)

                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val documents = task.result
                        for (document in documents) {
                            val classes = document.toObject(SectionModel::class.java)
                            itemList.add(classes)
                        }
                        Toast.makeText(this@ActivityClasses, ""+itemList.size, Toast.LENGTH_SHORT).show()
                        classAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Error fetching data: ${task.exception}")
                    }
                }

        }
    }
}
