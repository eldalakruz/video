package com.example.videoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.videoapp.databinding.ActivityEditBinding
import com.example.videoapp.ui.account.AccountFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.log

class EditActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.editCancelButton.setOnClickListener {
            val intent = Intent(this, AccountFragment::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        val name = binding.newName
        val email = binding.newEmail


         binding.editUpdateButton.setOnClickListener {

             val editName = name.text.toString()
             val editEmail = email.text.toString()

             when{
                 TextUtils.isEmpty(editName) -> Toast.makeText(this, "name is required",
                     Toast.LENGTH_SHORT ).show()
                 TextUtils.isEmpty(editEmail) -> Toast.makeText(this, "name is required",
                     Toast.LENGTH_SHORT ).show()
                 else ->{
                     updateAccount(editName,editEmail)
                 }
             }



         }



        }

    private fun updateAccount(editName: String,editEmail: String) {

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef : DatabaseReference = FirebaseDatabase.getInstance().reference
            .child("Users")


        val userMap = HashMap<String, Any>()


        userMap["name"] = editName
        userMap["email"] = editEmail

        userRef.child(currentUserId).updateChildren(userMap).addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                Toast.makeText(this@EditActivity, "updated successful",
                    Toast.LENGTH_SHORT).show()
                val intent = Intent(this@EditActivity, AccountFragment::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            else
            {
                Toast.makeText(this@EditActivity, "error: update is failed ",
                    Toast.LENGTH_SHORT).show()
            }
        }

        }

    }




