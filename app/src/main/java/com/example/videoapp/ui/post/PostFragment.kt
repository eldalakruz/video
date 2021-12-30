package com.example.videoapp.ui.post

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.videoapp.R
import com.example.videoapp.databinding.FragmentPostBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.UploadTask.*
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import com.google.firebase.auth.FirebaseUser

class PostFragment : Fragment() {

    private lateinit var postViewModel: PostViewModel
    private var _binding: FragmentPostBinding? = null

    private lateinit var imageUri : Uri
    private lateinit var storePost : StorageReference
    private var myUrl = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val root: View = binding.root



        storePost = FirebaseStorage.getInstance().reference.child("postImg")

        val adapter = ArrayAdapter.createFromResource(this@PostFragment.requireContext(),
            R.array.list_item, android.R.layout.simple_list_item_1)

        _binding!!.typeSpinner.adapter = adapter

        _binding!!.browseFileImg.setOnClickListener {
            selectImg()

        }
        _binding!!.uploadButton.setOnClickListener {

            if (imageUri!= null) {
                uploadImg()
            }
            else{
                Toast.makeText(this@PostFragment.requireContext(), " please select image",
                    Toast.LENGTH_SHORT).show()
            }
        }

        return root







    }

    private fun uploadImg() {


            val progressDialog: ProgressDialog = ProgressDialog(this@PostFragment.requireContext())
            progressDialog.setTitle("Uploading")
            progressDialog.setMessage("Please wait")
            progressDialog.setCanceledOnTouchOutside(false)

            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            val fileRef = storePost.child(System.currentTimeMillis().toString() + ".img")

            val uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(imageUri)

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    myUrl = downloadUri.toString()
                    _binding!!.firebaseImg.setImageURI(null)

                    val ref = FirebaseDatabase.getInstance().reference.child("Posts")
                    val postId = ref.push().key
                    val userMap = HashMap<String, Any>()

                    userMap["publisher"] = currentUserId
                    userMap["post"] = myUrl
                    userMap["postId"] = postId!!


                    ref.child(postId).setValue(userMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            Toast.makeText(
                                this@PostFragment.requireContext(),
                                "your post added successful", Toast.LENGTH_SHORT
                            ).show()

                        }
                    }


                } else {
                    Toast.makeText(
                        this@PostFragment.requireContext(),
                        "your post added failed", Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }




    private fun selectImg() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK){

            imageUri = data?.data!!
            val img =  _binding!!.firebaseImg
            img.setImageURI(imageUri)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}