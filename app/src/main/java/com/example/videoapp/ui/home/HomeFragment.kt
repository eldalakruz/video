package com.example.videoapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videoapp.R
import com.example.videoapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var userName : TextView
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var profileId : String

    var postList : List<PostClass>? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        userName = _binding!!.userNameText
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        getUserData()


        var recyclerViewUploadedImages : RecyclerView = _binding!!.imageRecyclerView
        recyclerViewUploadedImages.setHasFixedSize(true)
        recyclerViewUploadedImages.layoutManager = GridLayoutManager(context, 3)



        return binding.root
    }

    private fun getUserData() {
        val userRef = FirebaseDatabase.getInstance().reference.child("Users")
            .child(firebaseUser.uid)

        userRef.addValueEventListener(
            object : ValueEventListener {
                override fun  onDataChange(Snapshot: DataSnapshot) {

                    userName.text = Snapshot.child("name").value.toString()


                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@HomeFragment.requireContext(), "Network error",
                        Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

     fun getPicture(){

         val userRef = FirebaseDatabase.getInstance().reference.child("Post")
             .child(firebaseUser.uid)

         userRef.addValueEventListener(
             object : ValueEventListener {
                 override fun  onDataChange(Snapshot: DataSnapshot) {




                 }

                 override fun onCancelled(error: DatabaseError) {
                     Toast.makeText(this@HomeFragment.requireContext(), "Network error",
                         Toast.LENGTH_SHORT).show()
                 }
             }
         )


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}