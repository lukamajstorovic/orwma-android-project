package hr.ferit.lukamajstorovic.projektmob

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OccupiedRoomRecyclerAdapter(
    private var items: ArrayList<OccupiedRoom>,
    val listener: ContentListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoomViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_occupied_rooms, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
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

    fun addItem(item: OccupiedRoom) {
        items.add(item)
        notifyItemInserted(itemCount)
    }

    class RoomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val roomNumber = view.findViewById<TextView>(R.id.roomNumberOccupiedTextView)
        private val occupantName = view.findViewById<TextView>(R.id.nameTextView)
        private val occupantOib = view.findViewById<TextView>(R.id.oibTextView)
        private val startDate = view.findViewById<TextView>(R.id.startDateTextView)
        private val endDate = view.findViewById<TextView>(R.id.endDateTextView)
        private val vacateButton = view.findViewById<Button>(R.id.vacateButton)

        fun bind(index: Int, room: OccupiedRoom, listener: ContentListener) {
            roomNumber.text = room.roomNumber
            occupantName.text = room.occupantName
            occupantOib.text = room.occupantOIB
            startDate.text = room.startDate
            endDate.text = room.endDate

            vacateButton.setOnClickListener {
                listener.onXMarkClick(index, room, ItemButtonClick.REMOVE)
            }
        }
    }

    interface ContentListener {
        fun onXMarkClick(index: Int, item: OccupiedRoom, clickType: ItemButtonClick)
    }
}
