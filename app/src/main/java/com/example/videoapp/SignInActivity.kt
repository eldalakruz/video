package com.example.videoapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {


    lateinit var textEmail: EditText
    lateinit var textPassword: EditText

    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val  adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        textEmail = findViewById(R.id.signEmailText)
        textPassword = findViewById(R.id.signInPassword)



    }

    fun goSignUp(view: View) {

        startActivity(Intent(this, SignUpActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            startActivity(intent)
            finish()
        }
    }

    fun signIn(view: View) {

        val email = textEmail.text.toString()
        val password = textPassword.text.toString()

        when {
            TextUtils.isEmpty(email) -> Toast.makeText(this, "email is required",
                Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(password) -> Toast.makeText(this, "password is required",
                Toast.LENGTH_SHORT).show()
            !(Patterns.EMAIL_ADDRESS.matcher(email).matches()) -> Toast.makeText(this,
            "enter valid email address", Toast.LENGTH_LONG).show()

            else -> {

                val progressDialog: ProgressDialog = ProgressDialog(this@SignInActivity)
                progressDialog.setTitle("SignIn..")
                progressDialog.setMessage("please wait while")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        progressDialog.dismiss()
                        Toast.makeText(this, "login is successful",
                            Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()


                    }
                    else {
                        val message = task.exception.toString()
                        val short = message.split(":")
                        val error: String = short[1]
                        Toast.makeText(this, error,
                            Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut()
                        progressDialog.dismiss()
                    }
                }
            }

        }
    }
}