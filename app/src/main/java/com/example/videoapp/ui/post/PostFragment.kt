package com.example.videoapp.ui.post

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.videoapp.R
import com.example.videoapp.databinding.FragmentPostBinding

class PostFragment : Fragment() {

    private lateinit var postViewModel: PostViewModel
    private var _binding: FragmentPostBinding? = null

    private lateinit var imageUri : Uri

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postViewModel =
            ViewModelProvider(this)[PostViewModel::class.java]

        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        postViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })



        val adapter = ArrayAdapter.createFromResource(this@PostFragment.requireContext(),
            R.array.list_item, android.R.layout.simple_list_item_1)

        _binding!!.typeSpinner.adapter = adapter

        _binding!!.browseFileImg.setOnClickListener {
            selectImg()
        }
        _binding!!.uploadButton.setOnClickListener {
            uploadImg()
        }

        return root







    }

    private fun uploadImg() {
        TODO("Not yet implemented")
    }

    private fun selectImg() {

        val intent = Intent()
        intent.type = "images/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){

            imageUri = data?.data!!
            _binding!!.firebaseImg.setImageURI(imageUri)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}