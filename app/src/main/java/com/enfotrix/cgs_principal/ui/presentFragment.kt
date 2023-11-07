import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Adapters.AdapterPresent
import com.enfotrix.cgs_principal.Adapters.StudentListAdapter
import com.enfotrix.cgs_principal.Models.AttendanceViewModel
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.StudentModel
import com.enfotrix.cgs_principal.Models.StudentViewModel
import com.enfotrix.cgs_principal.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class presentFragment : Fragment() {
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private val studentViewModel: StudentViewModel by viewModels()
    private var studentList = mutableListOf<StudentModel>()
    private lateinit var mContext: Context


    private lateinit var presentAdapter: AdapterPresent

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_present, container, false)

        // Set up the RecyclerView and adapter
        setUpRecyclerView(view)

        displayPresentStudent(view)

        return view
    }

    private fun setUpRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Use requireContext() to get the context
        presentAdapter = AdapterPresent(mContext, studentList,) // Replace with your actual student list
        recyclerView.adapter = presentAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayPresentStudent(view: View) {
        val currentDate = getCurrentDate()

        attendanceViewModel.getAttendanceRec(currentDate).addOnCompleteListener { attendanceTask ->
            if (attendanceTask.isSuccessful) {
                val attendanceList = attendanceTask.result.map { it.toObject(AttendenceModel::class.java) }
                studentViewModel.getStudents().addOnCompleteListener { studentTask ->
                    if (studentTask.isSuccessful) {
                        studentList= studentTask.result.map { it.toObject(StudentModel::class.java) }.toMutableList()

                        val presentStudentNames = attendanceList
                            .filter { it.Status == "Present" }
                            .mapNotNull { attendance ->
                                val matchingStudent = studentList.find { student ->
                                    student.StudentId == attendance.StudentID
                                }
                                matchingStudent?.StudentName
                            }

                        // Now you can display the names in your fragment UI
                        // For example, you can set them in a TextView
                        // val presentStudentNamesTextView = view.findViewById<TextView>(R.id.presentStudentNamesTextView)
                        // presentStudentNamesTextView.text = "Present Students:\n${presentStudentNames.joinToString("\n")}"
                    } else {
                        // Handle the case where student data retrieval was not successful
                    }
                }
            } else {
                // Handle the case where attendance data retrieval was not successful
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }
}
