import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enfotrix.cgs_principal.R
import com.enfotrix.cgs_principal.models.AttendanceModel
import kotlinx.android.os.Build
import androidx.annotation.RequiresApi
import com.enfotrix.cgs_principal.Models.AttendanceViewModel

class PresentFragment : Fragment() {

    // Initialize your ViewModel
    private val attendanceViewModel = AttendanceViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_present, container, false)

        // Call a function to get and display the count of "Present" students
        displayPresentStudentCount()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayPresentStudentCount() {
        val currentDate = getCurrentDate()

        // Retrieve attendance data for the current date
        attendanceViewModel.getAttendanceRec(currentDate).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val attendanceList = task.result.map { it.toObject(AttendanceModel::class.java) }
                val counterPresent = attendanceList.filter { it.Status.equals("Present") }.count()

                // Now you can display counterPresent in your fragment UI
                // For example, you can set it in a TextView
                val presentCountTextView = view?.findViewById<TextView>(R.id.presentCountTextView)
                presentCountTextView?.text = "Present Students: $counterPresent"
            } else {
                // Handle the case where retrieval was not successful
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
