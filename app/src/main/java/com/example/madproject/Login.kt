package com.example.madproject

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    private lateinit var emailTIL: TextInputLayout
    private lateinit var passwordTIL: TextInputLayout
    private var doubleBackToExit = false

    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
            return
        }
        doubleBackToExit = true
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExit = false }, 2000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference

        emailTIL = findViewById(R.id.login_email_til)
        passwordTIL = findViewById(R.id.login_password_til)
    }

    //Checking if user is logged in already or not
    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user != null && user.isEmailVerified) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
        window.statusBarColor = resources.getColor(R.color.mid_green)
    }

    private fun validateEmail(): Boolean {
        val email = emailTIL.editText!!.text.toString().trim()
        if (email.isEmpty()) {
            emailTIL.error = getString(R.string.field_empty)
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTIL.error = getString(R.string.invalid_email)
            return false
        }
        emailTIL.error = null
        return true
    }

    private fun validatePassword(): Boolean {

        val pass = passwordTIL.editText!!.text.toString().trim()

        if (pass.isEmpty()) {
            passwordTIL.error = getString(R.string.field_empty)
            return false
        }

        if (pass.length < 6) {
            passwordTIL.error = "Password is too short (Min. 6 Characters)"
            return false
        }

        passwordTIL.error = null
        return true
    }

    fun loginEmployee(view: View) {
        if (!validateEmail() or !validatePassword())
            return


        auth.signInWithEmailAndPassword(
            emailTIL.editText!!.text.toString(),
            passwordTIL.editText!!.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser!!.isEmailVerified) {
                        checkGenderSavedOrNot()
                    } else {

                        AlertDialog.Builder(this)
                            .setTitle("Email Verification")
                            .setMessage("Please verify your Email address.\nA verification link has been sent to your Email address")
                            .setPositiveButton(
                                "OK",
                                DialogInterface.OnClickListener { dialogInterface, _ ->
                                    dialogInterface.dismiss()
                                })
                            .setCancelable(false)
                            .create()
                            .show()
                    }
                }
            }

            .addOnFailureListener {
                AlertDialog.Builder(this)
                    .setTitle("Attention")
                    .setMessage("${it.message}")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    })
                    .setCancelable(false)
                    .create()
                    .show()

            }


    }
    private fun checkGenderSavedOrNot() {
        val user = auth.currentUser!!
        val empName = user.displayName!!

        databaseRef.child("Customers")
            .child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val empGender = snapshot.child("gender").value.toString()
                    if(empGender == "none") {
                        val intent = Intent(this@Login, Gender::class.java)
                        intent.putExtra("name", empName)
                        intent.putExtra("uid", user.uid)
                        startActivity(intent)
                        finish()
                    } else {
                        startActivity(Intent(this@Login, MainActivity::class.java))
                        finish()
                        Toast.makeText(this@Login, "Welcome to UrbanBites", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
    fun openRegisterActivity(view: View) {
        startActivity(Intent(this, Register::class.java))
        finish()
    }
    fun forgotpassword(view: View){
        startActivity(Intent(this,ForgotPassword::class.java))
        finish()
    }
}
