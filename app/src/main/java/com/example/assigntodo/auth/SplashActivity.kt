package com.example.assigntodo.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.lifecycleScope
import com.example.assigntodo.BossMainActivity
import com.example.assigntodo.EmployeeMainActivity
import com.example.assigntodo.R
import com.example.assigntodo.Users
import com.example.assigntodo.utils.utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //  Handler - for delay &  Looper - ensures that this delay is handled on the main UI thread.
        Handler(Looper.getMainLooper()).postDelayed( {

            val currentUser = FirebaseAuth.getInstance().currentUser?.uid
            if (currentUser != null){
                lifecycleScope.launch {
                    try {
//                        addListenerForSingleValueEvent:
//                        Fetches the data once and stops listening after onDataChange listner is further destroyed
                        FirebaseDatabase.getInstance().getReference("Users").child(currentUser).addListenerForSingleValueEvent(object   :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val currentUserData = snapshot.getValue(Users::class.java)

                                if(currentUserData?.usertype=="Boss"){
                                    startActivity(Intent(this@SplashActivity, BossMainActivity::class.java))
                                    finish()
                                }
                                if(currentUserData?.usertype=="Employee"){
                                    startActivity(Intent(this@SplashActivity, EmployeeMainActivity::class.java))
                                    finish()
                                }

//                            while (currentUserData?.usertype=="Boss"){
//                               startActivity(Intent(this@SigninActivity,BossMainActivity::class.java))
//                                finish()
//                            }
//                            while (currentUserData?.usertype=="Employee"){
//                                startActivity(Intent(this@SigninActivity,EmployeeMainActivity::class.java))
//                                finish()
//                            }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                utils.hideDialog()
                                utils.showtoast(this@SplashActivity,error.message)
                            }


                        })

                    }
                    catch (e:Exception){
                        utils.showtoast(this@SplashActivity,e.message!!)
                    }
                }
            }
            else {
                startActivity(Intent(this, SigninActivity::class.java))
                finish()
            }
        },1500)
    }
}