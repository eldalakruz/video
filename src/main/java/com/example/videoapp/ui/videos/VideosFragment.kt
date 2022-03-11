package com.example.videoapp.ui.videos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.videoapp.R
import com.example.videoapp.databinding.FragmentVideosBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VideosFragment : Fragment() {

    private var _binding : FragmentVideosBinding? = null
    private lateinit var videoArrayList: ArrayList<ModelVideo>
    private lateinit var adapterVideo: AdapterVideo
    //private lateinit var recyclerView: RecyclerView


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentVideosBinding.inflate(inflater, container, false)


       // recyclerView = view?.findViewById(R.id.videosRecyclerView)!!
        loadVideos()

        return _binding!!.root
    }

    private fun loadVideos() {

        videoArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().reference.child("Videos")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                videoArrayList.clear()
                for (ds in snapshot.children)
                {
                    val modelVideo = ds.getValue(ModelVideo::class.java)
                    videoArrayList.add(modelVideo!!)
                }
                adapterVideo = AdapterVideo(this@VideosFragment.requireContext(),videoArrayList)

                _binding!!.videosRecyclerView.adapter = adapterVideo

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}