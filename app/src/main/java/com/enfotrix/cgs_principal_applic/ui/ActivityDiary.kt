package com.enfotrix.cgs_principal_applic.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.enfotrix.cgs_principal_applic.Models.ClassModel
import com.enfotrix.cgs_principal_applic.Models.SectionModel
import com.enfotrix.cgs_principal_applic.R
import com.enfotrix.cgs_principal_applic.SharedPrefManager
import com.enfotrix.cgs_principal_applic.databinding.ActivityClassResultBinding
import com.enfotrix.cgs_principal_applic.databinding.ActivityDiaryBinding

class ActivityDiary : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var mContext: Context

    private lateinit var spSelectClass: AppCompatSpinner
    private lateinit var spSelectSection: AppCompatSpinner
    private lateinit var spSelectSubject: AppCompatSpinner
    private lateinit var getDiaryButton: Button
  var selectedClassID:String="null"
  var selectedSectionID:String="null"
    private var isUserSelection = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)

        setContentView(binding.root)
        mContext = this@ActivityDiary
        sharedPrefManager = SharedPrefManager(mContext)

        showDialog()


    }

    private fun showDialog() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_get_diary)

        spSelectClass = dialog.findViewById(R.id.spSelectClass)
        spSelectSection = dialog.findViewById(R.id.spSelectSection)
        getDiaryButton = dialog.findViewById(R.id.getDiary)

        val classList = sharedPrefManager.getClassList()
        val sectionList = sharedPrefManager.getSectionList()

        val classAdapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, classList.map { it.ClassName })
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spSelectClass.adapter = classAdapter

        val initialSectionAdapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, emptyList<String>())
        initialSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spSelectSection.adapter = initialSectionAdapter


        spSelectClass.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                val selectedClass = classList[position]
                 selectedClassID = selectedClass.Id

                val filteredSections = sectionList.filter { it.ClassID == selectedClassID }
                val sectionAdapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, filteredSections.map { it.SectionName })
                sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spSelectSection.adapter = sectionAdapter
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        })
        spSelectSection.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                selectedSectionID= sectionList[position].ID
                Toast.makeText(mContext, "its section name"+sectionList[position].SectionName , Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        })



        getDiaryButton.setOnClickListener {
            Toast.makeText(mContext, ""+selectedClassID+" "+selectedSectionID, Toast.LENGTH_SHORT).show()
            val filteredSection = sectionList.filter { it.ID == selectedSectionID }
            val filteredClass = classList.filter { it.Id == selectedClassID }
            binding.tvCLASS.text=filteredClass.map { it.ClassName }.toString()
            binding.tvSECTION.text=filteredSection.map { it.SectionName }.toString()

            var subjectList=sharedPrefManager.getSubjectsList()

            Toast.makeText(mContext, "its subjectSize"+subjectList.size, Toast.LENGTH_SHORT).show()

            var filterSubjectList=subjectList.filter { it.SectionID==filteredSection.map { it.ID }.toString() }
            Toast.makeText(mContext, "its filtered"+filterSubjectList.size, Toast.LENGTH_SHORT).show()

            binding.spinnerSelectSubject.adapter = ArrayAdapter(
                mContext,
                android.R.layout.simple_spinner_item,
                filterSubjectList.map { it.SubjectName }
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }




            binding.spinnerSelectSubject.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                    val selectedSubject = filterSubjectList[position]
                    val selectedSubjectID = selectedSubject.ID
                    getDiaries(selectedSectionID,selectedSubjectID)
                }
                override fun onNothingSelected(parentView: AdapterView<*>) {
                }
            })







            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getDiaries(selectedSectionID: String, selectedSubjectID: String) {

    }
}
