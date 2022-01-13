package com.example.videoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.videoapp.databinding.ActivityEditBinding
import com.example.videoapp.ui.account.AccountFragment
import com.google.android.gms.ads.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class EditActivity : AppCompatActivity() {

    //private lateinit var mAdView : AdView

    private lateinit var binding : ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

      /*  MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                Log.d("adErrors", "ad loaded")
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
               val errorDomain = adError.domain
                val errorCode = adError.code
                val errorMessage = adError.message
                val responseInfo = adError.responseInfo
                val cause = adError.cause
                Log.d("adErrors", adError.toString())
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        } */




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
                 TextUtils.isEmpty(editEmail) -> Toast.makeText(this, "email is required",
                     Toast.LENGTH_SHORT ).show()

                 else ->{
                     if (Patterns.EMAIL_ADDRESS.matcher(editEmail).matches())
                     {
                         updateAccount(editName,editEmail)
                     }
                     else
                         Toast.makeText(this, "enter valid email id",
                             Toast.LENGTH_SHORT).show()

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




