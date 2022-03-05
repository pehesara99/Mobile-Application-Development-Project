package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPassword : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        window.statusBarColor = resources.getColor(R.color.mid_green)
    }
    override fun onBackPressed() {
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        findViewById<TextView>(R.id.btn_submit).setOnClickListener{
            val forgotemail = findViewById<TextView>(R.id.forgot_email)
            val email: String = forgotemail.text.toString().trim{it<=' '}

            if(email.isEmpty()){
                Toast.makeText(this, "Please provide your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            Toast.makeText(this@ForgotPassword, "Email sent successfully to reset your password!",
                            Toast.LENGTH_SHORT
                                ).show()
                            startActivity(Intent(this, Login::class.java))
                            finish()
                        }
                        else
                        {
                            Toast.makeText(this@ForgotPassword,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }


}