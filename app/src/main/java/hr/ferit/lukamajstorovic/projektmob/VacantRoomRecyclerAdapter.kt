package hr.ferit.lukamajstorovic.projektmob

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.ParseException
import java.text.SimpleDateFormat


enum class ItemButtonClick {
    COMPLETE,
    REMOVE
}

class VacantRoomRecyclerAdapter(
    private var items: ArrayList<VacantRoom>,
    val listener: ContentListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoomViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_available_rooms, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RoomViewHolder -> {
                holder.bind(position, items[position], listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    fun addItem(item: VacantRoom) {
        items.add(item)
        notifyItemInserted(itemCount)
    }

    class RoomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val roomNumber = view.findViewById<TextView>(R.id.roomNumberVacantTextView)
        private val occupantName = view.findViewById<EditText>(R.id.nameEditText)
        private val occupantOib = view.findViewById<EditText>(R.id.oibEditText)
        private val startDate = view.findViewById<EditText>(R.id.startDate)
        private val endDate = view.findViewById<EditText>(R.id.endDate)
        private val completeButton = view.findViewById<Button>(R.id.vacateButton)

        fun bind(index: Int, room: VacantRoom, listener: ContentListener) {
            roomNumber.text = room.roomNumber
            occupantName.setText(room.occupantName)
            occupantOib.setText(room.occupantOIB)
            startDate.setText(room.startDate)
            endDate.setText(room.endDate)

            completeButton.setOnClickListener {
                room.occupantName = occupantName.text.toString()
                room.occupantOIB = occupantOib.text.toString()
                room.startDate = startDate.text.toString()
                room.endDate = endDate.text.toString()
                if (
                    room.occupantName == "" ||
                    room.occupantOIB == "" ||
                    room.startDate == "" ||
                    room.endDate == ""
                ) {
                    listener.onMissingInformation()
                } else if (
                    !IsDateFormatValid(room.startDate) ||
                    !IsDateFormatValid(room.endDate)
                ) {
                    listener.onInvalidDateFormat()
                } else {
                    listener.onCheckmarkClick(index, room, ItemButtonClick.COMPLETE)
                }
            }
        }

        fun IsDateFormatValid(date: String): Boolean {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            return try {
                val date = dateFormat.parse(date)
                true
            } catch (e: ParseException) {
                false
            }
        }

    }

    interface ContentListener {
        fun onCheckmarkClick(index: Int, item: VacantRoom, clickType: ItemButtonClick)
        fun onMissingInformation()
        fun onInvalidDateFormat()
    }
}
