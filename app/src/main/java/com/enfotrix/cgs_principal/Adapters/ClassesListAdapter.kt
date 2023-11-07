import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.AttendenceModel
import com.enfotrix.cgs_principal.Models.ClassModel
import com.enfotrix.cgs_principal.Models.SectionModel
import com.enfotrix.cgs_principal.R

class ClassesListAdapter(
    private val context: Context,
    private val classList: List<ClassModel>,
    private val sectionList: List<SectionModel>,
    private var attendanceList: List<AttendenceModel>,
    private val attendanceClickListener: AttendanceClickListener

) : RecyclerView.Adapter<ClassesListAdapter.ViewHolder>() {
    val sortedList=sectionList.sortedBy { it.ClassName }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvClassSection: TextView = view.findViewById(R.id.tvClassSection)
        val tvPresent: TextView = view.findViewById(R.id.tvPresent)
        val tvAbsent: TextView = view.findViewById(R.id.tvAbsent)
        val tvLeave: TextView = view.findViewById(R.id.tvLeave)
        val tvPercent: TextView = view.findViewById(R.id.tvPercent)
        val layAttendance: LinearLayout = view.findViewById(R.id.layAttendance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attandence_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {




        val sectionModel = sortedList[position]
        var counterPresent : Int=0
        var counterAbsent : Int=0
        var counterLeave : Int=0

        counterPresent = attendanceList.filter { attendance-> attendance.Status.equals("Present") && attendance.SectionID.equals(sectionModel.ID) }.count()
        counterAbsent = attendanceList.filter { attendance-> attendance.Status.equals("Absent") && attendance.SectionID.equals(sectionModel.ID) }.count()
        counterLeave = attendanceList.filter { attendance-> attendance.Status.equals("Leave") && attendance.SectionID.equals(sectionModel.ID) }.count()


        val total = counterPresent + counterAbsent + counterLeave
        if (total > 0) {
            val percent = (counterPresent.toFloat() / total) * 100
            holder. tvPercent.text = percent.toInt().toString()+"%"
        }
        else holder. tvPercent.text = "N/A"


        holder.tvClassSection.text=sectionModel.ClassName+"-"+sectionModel.SectionName
        holder.tvPresent.text= counterPresent.toString()
        holder.tvAbsent.text= counterAbsent.toString()
        holder.tvLeave.text= counterLeave.toString()


        holder.layAttendance.setOnClickListener {
            val listAtten: List<AttendenceModel> = attendanceList.filter { attendance-> attendance.SectionID.equals(sectionModel.ID)}

            attendanceClickListener.onAttendanceClicked( sectionModel.ID, listAtten)
        }







    }
    fun updateAttendanceData(newAttendanceList: List<AttendenceModel>) {
        attendanceList = newAttendanceList // Update the attendanceList with the new data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }



    interface AttendanceClickListener {
        fun onAttendanceClicked(sectionID:String, attendacneList: List<AttendenceModel>)

    }

}







