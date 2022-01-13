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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    lateinit var textName : EditText
    lateinit var textEmail : EditText
    lateinit var textPassword : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        textName = findViewById(R.id.signUpName)
        textEmail = findViewById(R.id.signUpEmail)
        textPassword = findViewById(R.id.signUpPassword)

    }

    fun goSignIn(view: View) {

        startActivity(Intent(this, SignInActivity::class.java))
    }

    fun signUp(view: View) {

        val name = textName.text.toString()
        val email = textEmail.text.toString()
        val password = textPassword.text.toString()

        when{

            TextUtils.isEmpty(name) -> Toast.makeText(this, "name is required",
                Toast.LENGTH_SHORT ).show()
            TextUtils.isEmpty(email) -> Toast.makeText(this, "email is required",
                Toast.LENGTH_SHORT ).show()
            TextUtils.isEmpty(password) -> Toast.makeText(this, "password is required",
                Toast.LENGTH_SHORT ).show()
            !(Patterns.EMAIL_ADDRESS.matcher(email).matches()) -> Toast.makeText(this,
            "enter valid email address", Toast.LENGTH_SHORT).show()
            else ->{

                val progressDialog : ProgressDialog = ProgressDialog(this@SignUpActivity)
                progressDialog.setTitle("SignUp..")
                progressDialog.setMessage("please wait while")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
                    if (task.isSuccessful)
                    {

                        saveUserInfo(name, email, progressDialog)
                    }
                    else
                    {
                        val message = task.exception!!.toString()
                        val short = message.split(":")
                        val error : String = short[1]
                        Toast.makeText(this, error,
                            Toast.LENGTH_SHORT).show()
                        mAuth.signOut()
                        progressDialog.dismiss()
                    }
                }
            }
        }


    }

    private fun saveUserInfo(name: String, email: String, progressDialog: ProgressDialog) {

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef : DatabaseReference = FirebaseDatabase.getInstance().reference
            .child("Users")

        val userMap = HashMap<String, Any>()

        userMap["uid"] = currentUserId
        userMap["name"] = name
        userMap["email"] = email

        userRef.child(currentUserId).setValue(userMap).addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                progressDialog.dismiss()
                Toast.makeText(this, "account created successful",
                    Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            else
            {
                val message = task.exception!!.toString()
                val short = message.split(":")
                val error : String = short[1]
                Toast.makeText(this, error,
                    Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
                progressDialog.dismiss()

            }
        }


    }

}