package com.enfotrix.cgs_principal.ui
import ClassesListAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.ClassViewModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.databinding.ActivityAttendanceBinding
import com.enftorix.cgs_principal.Constants
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ActivityAttendance : AppCompatActivity(), ClassesListAdapter.AttendanceClickListener {
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private val classViewModel: ClassViewModel by viewModels()


    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var recyclerView: RecyclerView
    var presentCount = 0
    var totalCount = 0
    private var percentageMap: MutableMap<String, Double> = mutableMapOf()
    private val constants = Constants()
    private val globalList = mutableListOf<AttendenceModel>()

    private var attendanceList = mutableListOf<AttendenceModel>()
    private lateinit var classAdapter: ClassesListAdapter
    // ...
    private var sectionPercentages: MutableMap<String, Double> = mutableMapOf() // Declare sectionPercentages at the class level

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityAttendance
        sharedPrefManager = SharedPrefManager(mContext)
        Toast.makeText(mContext, "sect6ion list is"+sharedPrefManager.getSectionFromShared(), Toast.LENGTH_LONG).show()

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)

        // Create the adapter and pass "this" as the AttendanceClickListener



        getAttendance()


    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun getAttendance(){

        lifecycleScope.launch {
            attendanceViewModel.getAttendanceRec(getCurrentDate())
                .addOnCompleteListener{task->
                    if(task.isSuccessful)
                    {

                        val attendanceList= task.result.map { it.toObject(AttendenceModel::class.java) }

                        var counterPresent : Int
                        var counterAbsent : Int
                        var counterLeave : Int

                        counterPresent = attendanceList.filter { attendance-> attendance.Status.equals("Present") }.count()
                        counterAbsent = attendanceList.filter { attendance-> attendance.Status.equals("Absent") }.count()
                        counterLeave = attendanceList.filter { attendance-> attendance.Status.equals("Leave") }.count()

                        val total = counterPresent + counterAbsent + counterLeave
                        if (total > 0) {
                            val percent = (counterPresent.toFloat() / total) * 100
                            binding.tvAttendanceHeader.text = "Today Attendance: %.2f%%".format(percent)
                        }

                        binding.studentsPresent.text= counterPresent.toString()
                        binding.studentsAbsent.text= counterAbsent.toString()
                        binding.studentsLeave.text= counterLeave.toString()



                        classAdapter = ClassesListAdapter(
                            mContext,
                            classViewModel.getClassList(),
                            classViewModel.getSectionList(),
                            attendanceList,
                            this@ActivityAttendance
                        )
                        recyclerView.adapter = classAdapter


                    }


                }
                .addOnFailureListener{

                }


        }





    }







    @RequiresApi(Build.VERSION_CODES.O)
   /* fun countSections() {
        val sections = sharedPrefManager.getSectionFromShared()

        for (section in sections) {
            var presentCount = 0
            var totalCount = 0

            attendanceViewModel.getTodayAttendance(getCurrentDate(), section.ID).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result
                    if (documents.size() > 0) {
                        for (document in documents) {
                            val status = document.getString("status")
                            if (status.equals("Present", ignoreCase = true)) {
                                presentCount++
                            }
                            totalCount++

                        }
                        Toast.makeText(this, ""+totalCount, Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, ""+presentCount, Toast.LENGTH_SHORT).show()

                        val percentage = calculatePercentage(presentCount, totalCount)
                        Toast.makeText(this, ""+percentage, Toast.LENGTH_SHORT).show()

                        sectionPercentages[section.ID] = percentage
                    }
                }
            }
        }
    }
*/
    // Calculate the percentage
    private fun calculatePercentage(presentCount: Int, totalCount: Int): Double {
        return if (totalCount > 0) {
            (presentCount.toDouble() / totalCount) * 100.0
        } else {
            0.0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }

    override fun onAttendanceClicked(sectionID:String , attendacneList:List<AttendenceModel>) {
        // Handle the item click event here
        // For example, open the ActivityStudentRegister activity.
        val intent = Intent(this, ActivityStudentAttendanceRegister::class.java)
        //intent.putExtra("SectionName",Sectionname)
        //intent.putExtra("Id",Id)
        //intent.putExtra("className",className)
        startActivity(intent)

    }

}


//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private  fun getTodayAttendanceList() {
//        lifecycleScope.launch {
//            Toast.makeText(mContext, ""+getCurrentDate(), Toast.LENGTH_SHORT).show()
//
//            attendanceViewModel.getTodayAttendance(getCurrentDate(),sec).addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val documents = task.result
//                    if (documents.size() > 0) {
//                        var attendenceModel: AttendenceModel? = null
//                        for (document in documents) {
//                            attendenceModel = document.toObject(AttendenceModel::class.java)
//                            attendanceList.add(attendenceModel)
//                        }
//
//                    }
//                    Toast.makeText(mContext, "vyg"+attendanceList.size, Toast.LENGTH_SHORT).show()
//
//                } else {
//                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//
//
//        }
//
//    }





