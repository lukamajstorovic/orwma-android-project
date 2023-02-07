package hr.ferit.lukamajstorovic.projektmob

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.firestore.FirebaseFirestore

class SignInFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        firestore = FirebaseFirestore.getInstance()
        val db = FirebaseFirestore.getInstance()
        val items = ArrayList<User>()
        db.collection("users")
            .get()
            .addOnSuccessListener {
                for (data in it.documents) {
                    val user = User(
                        id = data.id,
                        username = data.get("username").toString(),
                        password = data.get("password").toString(),
                    )
                    items.add(user)
                }
            }
        val username = view.findViewById<EditText>(R.id.usernameInputEditText)
        val password = view.findViewById<EditText>(R.id.passwordInputEditText)

        val signInButton = view.findViewById<Button>(R.id.signInButton)
        signInButton.setOnClickListener {
            for (item in items) {
                if (username.text.toString() == item.username) {
                    if (password.text.toString() == item.password) {
                        val fragmentTransaction: FragmentTransaction? =
                            requireActivity().supportFragmentManager.beginTransaction()
                        fragmentTransaction?.replace(R.id.mainFragment, RoomFragment())
                        fragmentTransaction?.addToBackStack(null)
                        fragmentTransaction?.commit()
                    }else{
                        Toast.makeText(requireContext(), R.string.incorrect_username_or_password, Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(), R.string.incorrect_username_or_password, Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }
}
