package com.enfotrix.cgs_principal.ui
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.ClassesListAdapter
import com.enfotrix.cgs_principal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.databinding.ActivityClassesBinding
import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates


class ActivityClasses : AppCompatActivity() {
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private lateinit var binding: ActivityClassesBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    var presentCount=0
    var totalCount=0
    var percentageMap: MutableMap<String, Int> = mutableMapOf() // Add a percentageMap property
    private val constants = Constants()
    private val globalList= mutableListOf<AttendenceModel>()

    private var attendanceList = mutableListOf<AttendenceModel>()
    private lateinit var classAdapter: ClassesListAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityClasses
        sharedPrefManager = SharedPrefManager(mContext)
        getTodayAttendanceList()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
                                                //save b
        classAdapter = ClassesListAdapter(mContext,sharedPrefManager.getClassList(),sharedPrefManager.getSectionFromShared(),attendanceList)
        recyclerView.adapter = classAdapter





    }


    @RequiresApi(Build.VERSION_CODES.O)
    private  fun getTodayAttendanceList() {
        lifecycleScope.launch {
            Toast.makeText(mContext, ""+getCurrentDate(), Toast.LENGTH_SHORT).show()

            attendanceViewModel.getTodayAttendance(getCurrentDate(),Id).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result
                    if (documents.size() > 0) {
                        var attendenceModel: AttendenceModel? = null
                        for (document in documents) {
                            attendenceModel = document.toObject(AttendenceModel::class.java)
                            attendanceList.add(attendenceModel)
                        }

                    }
                    Toast.makeText(mContext, "vyg"+attendanceList.size, Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

            }


        }

    }






    @RequiresApi(Build.VERSION_CODES.O)
     fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun counSections(){


        var sections=  sharedPrefManager.getSectionFromShared()
        for (section in sections){
            for (i in 1..sections.size ){
                attendanceViewModel.getTodayAttendance(getCurrentDate(),section.ID).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val documents = task.result
                        if (documents.size() > 0) {
                            var attendenceModel: AttendenceModel? = null
                            for (document in documents) {
                              var status=  document.getString("status")
                                if (status .equals("Present")){
                                    presentCount++
                                }

                                totalCount++

                            }




                        }
                    }
                }


            }
            percentageMap.put(section.ID, attandceCount())
        }

    }
    fun attandceCount(): Int {
        var precentage = presentCount / totalCount * 100
        return precentage
    }


}
