package com.example.madproject

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class UserProfile : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences


    override fun onStart() {
        super.onStart()
        window.statusBarColor = resources.getColor(R.color.yellow_theme_color)
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        supportActionBar?.hide();
        setContentView(R.layout.activity_user_profile)
        val gender = intent?.getStringExtra("gender")
        if(gender == "female") {
            findViewById<CircleImageView>(R.id.profile_user_icon).setImageDrawable(resources.getDrawable(R.drawable.user_female))
        }

        sharedPref = getSharedPreferences("user_profile_details", MODE_PRIVATE)
        loadUserProfile()
        findViewById<ImageView>(R.id.imageView2).setOnClickListener { onBackPressed() }
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
    private fun loadUserProfile() {

        if(sharedPref.getString("emp_name", "none") != "none") {
            //it will load the details from offline, so it doesn't need to connect with online database, it will be fast
            loadOfflineUserProfile()
        } else {
            //if not saved, then save it offline
            val editor = sharedPref.edit()

            val user = FirebaseAuth.getInstance().currentUser!!

            editor.putString("emp_name", user.displayName!!)
            editor.putString("emp_email", user.email!!)

            val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
            databaseRef.child("Customers")
                .child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {


                        editor.putString("emp_mobile_no", snapshot.child("mobile_no").value.toString())

                        editor.putString("emp_reg_id", snapshot.child("reg_id").value.toString())
                        editor.putString("emp_reg_date", snapshot.child("reg_date").value.toString())


                        editor.apply()
                        loadOfflineUserProfile() //now load offline, because it is saved
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }

    private fun loadOfflineUserProfile() {
        findViewById<TextView>(R.id.profile_top_name_tv).text = sharedPref.getString("emp_name", "11")
        findViewById<TextView>(R.id.profile_top_email_tv).text = sharedPref.getString("emp_email", "11")

        findViewById<TextView>(R.id.profile_mobile_no_tv).text = sharedPref.getString("emp_mobile_no", "11")

        findViewById<TextView>(R.id.profile_reg_id_tv).text = sharedPref.getString("emp_reg_id", "11")
        findViewById<TextView>(R.id.profile_reg_date_tv).text = sharedPref.getString("emp_reg_date", "11")



    }

    fun goBack(view: View) {onBackPressed()}
}