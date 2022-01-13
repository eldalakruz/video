package com.example.videoapp.ui.post

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.videoapp.databinding.FragmentPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask.*


 class PostFragment : Fragment() {


    private var _binding: FragmentPostBinding? = null

    private lateinit var imageUri : Uri
    private lateinit var videoUri : Uri
    private lateinit var storePost : StorageReference
    private lateinit var storeVideoPost : StorageReference
    private var myUrl = ""
    private var selectedImage : Boolean = false
     private  var selectedVideo : Boolean = false
     private lateinit var titleText : EditText


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
        storeVideoPost = FirebaseStorage.getInstance().reference.child("postVideo")


        titleText = _binding!!.contentTitle

        _binding!!.browseFileImg.setOnClickListener {

            _binding!!.firebaseVideo.visibility = View.GONE
            _binding!!.firebaseImg.visibility = View.VISIBLE
            selectImg()

        }
        _binding!!.uploadButton.setOnClickListener {

            when {
                selectedImage -> {
                    uploadImg()
                }
                selectedVideo -> {
                    uploadVideo()
                }
                else -> {
                    Toast.makeText(this@PostFragment.requireContext(), " please select image",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        _binding!!.browseFileVideo.setOnClickListener {

            _binding!!.firebaseImg.visibility = View.INVISIBLE
            _binding!!.firebaseVideo.visibility = View.VISIBLE


            selectVideo()

        }






        return root

    }


     private fun uploadVideo() {

         val progressDialog: ProgressDialog = ProgressDialog(this@PostFragment.requireContext())
         progressDialog.setTitle("Uploading")
         progressDialog.setMessage("Please wait")
         progressDialog.setCanceledOnTouchOutside(false)

         val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
         val fileRef = storeVideoPost.child(System.currentTimeMillis().toString() + ".mp4")

         var videoTitle : String = _binding!!.contentTitle.text.toString()
         if(TextUtils.isEmpty(videoTitle))
         {
             videoTitle = "untiled video"
         }

         val uploadTask: StorageTask<*>
         uploadTask = fileRef.putFile(videoUri)

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
                 myUrl = null.toString()
                 myUrl = downloadUri.toString()
                 selectedVideo = false

                 val ref = FirebaseDatabase.getInstance().reference.child("Videos")
                 val postId = ref.push().key
                 val userMap = HashMap<String, Any>()

                 userMap["publisher"] = currentUserId
                 userMap["postVideo"] = myUrl
                 userMap["postId"] = postId!!
                 userMap["title"] = videoTitle


                 ref.child(postId).setValue(userMap).addOnCompleteListener { task ->
                     if (task.isSuccessful) {
                         progressDialog.dismiss()
                         Toast.makeText(
                             this@PostFragment.requireContext(),
                             "your video post added successful", Toast.LENGTH_SHORT
                         ).show()

                     }
                 }


             } else {
                 Toast.makeText(
                     this@PostFragment.requireContext(),
                     "your video post added failed", Toast.LENGTH_SHORT
                 ).show()
             }
         }


     }

     private fun uploadImg() {


            val progressDialog: ProgressDialog = ProgressDialog(this@PostFragment.requireContext())
            progressDialog.setTitle("Uploading")
            progressDialog.setMessage("Please wait")
            progressDialog.setCanceledOnTouchOutside(false)

            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            val fileRef = storePost.child(System.currentTimeMillis().toString() + ".img")

         var imgTitle : String = _binding!!.contentTitle.text.toString()
         if(TextUtils.isEmpty(imgTitle))
         {
             imgTitle = "untiled post"
         }

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
                    myUrl = null.toString()
                    myUrl = downloadUri.toString()
                    _binding!!.firebaseImg.setImageURI(null)

                    val ref = FirebaseDatabase.getInstance().reference.child("Posts")
                    val postId = ref.push().key
                    val userMap = HashMap<String, Any>()

                    userMap["publisher"] = currentUserId
                    userMap["post"] = myUrl
                    userMap["postId"] = postId!!
                    userMap["title"] = imgTitle


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

     private fun selectVideo() {
         val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
         startActivityForResult(intent, 200)
     }


     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK){

            imageUri = data?.data!!
            val img =  _binding!!.firebaseImg
            img.setImageURI(imageUri)
            selectedVideo = false
            selectedImage = true
            titleText.visibility = View.VISIBLE

        }
        else if (requestCode == 200 && resultCode == Activity.RESULT_OK){

            videoUri = data?.data!!
            val  video = _binding!!.firebaseVideo
            val mediaController = MediaController(this@PostFragment.requireContext())
            mediaController.setAnchorView(video)
            video.setMediaController(mediaController)
            video.setVideoURI(videoUri)
            video.requestFocus()
            video.start()
            selectedImage = false
            selectedVideo = true
            titleText.visibility = View.VISIBLE


        }
    }





}