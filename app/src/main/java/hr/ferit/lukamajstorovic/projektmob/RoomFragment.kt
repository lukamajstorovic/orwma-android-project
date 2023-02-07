package hr.ferit.lukamajstorovic.projektmob

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RoomFragment : Fragment(), VacantRoomRecyclerAdapter.ContentListener,
    OccupiedRoomRecyclerAdapter.ContentListener {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var vacantRecyclerAdapter: VacantRoomRecyclerAdapter
    private lateinit var occupiedRecyclerAdapter: OccupiedRoomRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rooms, container, false)
        firestore = FirebaseFirestore.getInstance()
        val vacantButton = view.findViewById<Button>(R.id.vacantButton)
        val occupiedButton = view.findViewById<Button>(R.id.occupiedButton)

        vacantButton.setTypeface(null, Typeface.BOLD)
        occupiedButton.setTypeface(null, Typeface.NORMAL)

        val db = Firebase.firestore

        val recyclerView = view.findViewById<RecyclerView>(R.id.roomList)
        db.collection("vacant-rooms")
            .get()
            .addOnSuccessListener {
                val items: ArrayList<VacantRoom> = ArrayList()
                for (data in it.documents) {
                    val vacantRoom = VacantRoom(
                        id = data.id,
                        roomNumber = data.get("roomNumber").toString(),
                    )
                    items.add(vacantRoom)
                }
                items.sortBy { vacantRoom ->
                    vacantRoom.roomNumber
                }
                vacantRecyclerAdapter = VacantRoomRecyclerAdapter(items, this@RoomFragment)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = vacantRecyclerAdapter
                }
            }

        vacantButton.setOnClickListener {
            vacantButton.setTypeface(null, Typeface.BOLD)
            occupiedButton.setTypeface(null, Typeface.NORMAL)
            db.collection("vacant-rooms")
                .get()
                .addOnSuccessListener {
                    val items: ArrayList<VacantRoom> = ArrayList()
                    for (data in it.documents) {
                        val vacantRoom = VacantRoom(
                            id = data.id,
                            roomNumber = data.get("roomNumber").toString(),
                        )
                        items.add(vacantRoom)
                    }
                    items.sortBy { vacantRoom ->
                        vacantRoom.roomNumber
                    }
                    vacantRecyclerAdapter = VacantRoomRecyclerAdapter(items, this@RoomFragment)
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = vacantRecyclerAdapter
                    }
                }
        }

        occupiedButton.setOnClickListener {
            vacantButton.setTypeface(null, Typeface.NORMAL)
            occupiedButton.setTypeface(null, Typeface.BOLD)
            db.collection("occupied-rooms")
                .get()
                .addOnSuccessListener {
                    val items: ArrayList<OccupiedRoom> = ArrayList()
                    for (data in it.documents) {
                        val occupiedRoom = OccupiedRoom(
                            id = data.id,
                            roomNumber = data.get("roomNumber").toString(),
                            occupantName = data.get("occupantName").toString(),
                            occupantOIB = data.get("occupantOIB").toString(),
                            startDate = data.get("startDate").toString(),
                            endDate = data.get("endDate").toString(),
                        )
                        items.add(occupiedRoom)
                    }
                    items.sortBy { occupiedRoom ->
                        occupiedRoom.roomNumber
                    }
                    occupiedRecyclerAdapter = OccupiedRoomRecyclerAdapter(items, this@RoomFragment)
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = occupiedRecyclerAdapter
                    }
                }
        }
        return view
    }

    override fun onCheckmarkClick(index: Int, item: VacantRoom, clickType: ItemButtonClick) {
        val db = Firebase.firestore

        val occupiedRoom = hashMapOf(
            "roomNumber" to item.roomNumber,
            "occupantName" to item.occupantName,
            "occupantOIB" to item.occupantOIB,
            "startDate" to item.startDate,
            "endDate" to item.endDate,
        )
        db.collection("occupied-rooms")
            .add(occupiedRoom)

        db.collection("vacant-rooms")
            .document(item.id)
            .delete()

        vacantRecyclerAdapter.removeItem(index)
    }

    override fun onMissingInformation() {
        Toast.makeText(requireContext(), R.string.missing_information, Toast.LENGTH_SHORT).show()
    }

    override fun onInvalidDateFormat() {
        Toast.makeText(requireContext(), R.string.invalid_date, Toast.LENGTH_SHORT).show()
    }

    override fun onXMarkClick(index: Int, item: OccupiedRoom, clickType: ItemButtonClick) {
        val db = Firebase.firestore
        val vacantRoom = hashMapOf(
            "roomNumber" to item.roomNumber,
            "occupantName" to "",
            "occupantOIB" to "",
            "startDate" to "",
            "endDate" to "",
        )
        db.collection("vacant-rooms")
            .add(vacantRoom)

        db.collection("occupied-rooms")
            .document(item.id)
            .delete()

        occupiedRecyclerAdapter.removeItem(index)
    }
}
