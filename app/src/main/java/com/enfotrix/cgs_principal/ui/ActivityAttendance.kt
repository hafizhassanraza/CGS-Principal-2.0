package com.enfotrix.cgs_principal.ui
import ClassesListAdapter
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


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

        binding.datePicker.setText(getCurrentDate())


        getAttendance(getCurrentDate())



        binding.datePicker.setOnClickListener {
            showDatePickerDialog();
        }



    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun getAttendance(date : String){

        lifecycleScope.launch {
            attendanceViewModel.getAttendanceRec(date)
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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val calendar: Calendar = Calendar.getInstance()
        val todayYear: Int = calendar.get(Calendar.YEAR)
        val todayMonth: Int = calendar.get(Calendar.MONTH)
        val todayDayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { datePicker, selectedYear, selectedMonth, selectedDay ->
                // Create a Calendar instance for the selected date
                val selectedDateCalendar = Calendar.getInstance()
                selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay)

                // Create a Calendar instance for today's date
                val todayCalendar = Calendar.getInstance()



                val selectedDateFormatted = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    .format(selectedDateCalendar.time)





                binding.datePicker.setText(selectedDateFormatted)
                getAttendance(selectedDateFormatted)





                /*// Check if the selected date is today or in the future
                if (selectedDateCalendar.after(todayCalendar)) {
                    // The selected date is tomorrow or a future date, show an error message
                    Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show()
                    binding.recyclerView.visibility= View.INVISIBLE
                } else {
                    // The selected date is valid, format it in "dd-MM-yyyy" and update the EditText
                    val selectedDateFormatted = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        .format(selectedDateCalendar.time)

                    binding.datePicker.setText(selectedDateFormatted)
                    attendanceViewModel.getAttendanceRec (selectedDateFormatted)
                }*/
            },
            todayYear,
            todayMonth,
            todayDayOfMonth
        )

        datePickerDialog.show()
    }


}

