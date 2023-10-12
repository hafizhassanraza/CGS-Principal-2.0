import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.cgs_principal.Models.ClassModel
import com.enfotrix.cgs_principal.Models.SectionModel
import com.enfotrix.cgs_principal.R

class ClassesListAdapter(
    private val context: Context,
    private val classList: List<ClassModel>,
    private val sectionList: List<SectionModel>,
    private val attendanceList: MutableMap<String, Double>,
    private val attendanceClickListener: AttendanceClickListener

) : RecyclerView.Adapter<ClassesListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val classes: TextView = view.findViewById(R.id.classfield)
        val sections: TextView = view.findViewById(R.id.section)
        val percentage: TextView = view.findViewById(R.id.percentage)
        val card: CardView = view.findViewById(R.id.cardViewclass)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attandence_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sectionList = sectionList[position]
        val classModel = classList[position]

        holder.classes.text = classModel.ClassName
        holder.sections.text = sectionList.SectionName

        val percentage = attendanceList[sectionList.ID]
        if (percentage != null) {
            val formattedPercentage = String.format("%.2f%%", percentage)
            holder.percentage.text = formattedPercentage
        } else {
            holder.percentage.text = "N/A"
        }

        holder.card.setOnClickListener {
            attendanceClickListener.onAttendanceClicked(sectionList.ID)
        }
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }



interface AttendanceClickListener {
        fun onAttendanceClicked(SectionId: String)

    }

}







