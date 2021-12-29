package com.example.videoapp.ui.account

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.videoapp.EditActivity
import com.example.videoapp.MainActivity
import com.example.videoapp.R
import com.example.videoapp.SignInActivity
import com.example.videoapp.databinding.FragmentAccountBinding
import com.example.videoapp.ui.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
internal const val ARG_PARAM1 = "param1"
internal const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var logoutButton : Button
    private lateinit var textName : TextView
    private lateinit var textEmail : TextView
    private lateinit var editButton: Button
    private lateinit var firebaseUser: FirebaseUser

    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_account, container, false)

        logoutButton = v.findViewById(R.id.logoutButton)
        editButton = v.findViewById(R.id.editButton)

        logoutButton.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@AccountFragment.requireContext(), SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        editButton.setOnClickListener {

            startActivity(Intent(this@AccountFragment.requireContext(), EditActivity::class.java))

        }

        textName = v.findViewById(R.id.name_text)
        textEmail = v.findViewById(R.id.email_text)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        userIfo()

        return v


    }

    private fun userIfo(){


        val userRef = FirebaseDatabase.getInstance().reference.child("Users")
            .child(firebaseUser.uid)

        userRef.addValueEventListener(
             object : ValueEventListener {
                 override fun  onDataChange(Snapshot: DataSnapshot) {

                     textName.text = Snapshot.child("name").value.toString()
                     textEmail.text = Snapshot.child("email").value.toString()

                 }

                 override fun onCancelled(error: DatabaseError) {
                     TODO("Not yet implemented")
                 }
             }
         )
        


    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }


    }






}