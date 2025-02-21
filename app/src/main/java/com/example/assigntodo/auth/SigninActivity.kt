package com.example.assigntodo.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.assigntodo.BossMainActivity
import com.example.assigntodo.EmployeeMainActivity
import com.example.assigntodo.Users
import com.example.assigntodo.databinding.ActivitySigninBinding
import com.example.assigntodo.databinding.ForgotPasswordBinding
import com.example.assigntodo.utils.utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnlogin.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            loginUser(email,password)
        }
        binding.tvSignUp.setOnClickListener{
            startActivity(Intent(this@SigninActivity,SignupActivity::class.java))
            finish()


        }

        binding.tvForgotPassword.setOnClickListener{ showforgotpasswordialog()}
    }

    private fun showforgotpasswordialog() {
      val dialog = ForgotPasswordBinding.inflate(LayoutInflater.from(this))
        val alertDialog= AlertDialog.Builder(this)
            .setView(dialog.root)
            .show()
        alertDialog.show()
        dialog.etEmail.requestFocus()

        dialog.btnforgotpss.setOnClickListener{
            val email= dialog.etEmail.text.toString()
            alertDialog.dismiss()
            resetpassword(email)
        }

    }

    private fun resetpassword(email: String) {
        lifecycleScope.launch {
            try {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
                utils.showtoast(this@SigninActivity,"Please check your email and reset your password")

            }
            catch (e:Exception){
                utils.showtoast(this@SigninActivity,e.message.toString())
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        utils.showdialog(this)
        val firebaseAuth= FirebaseAuth.getInstance()
        lifecycleScope.launch {
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(email,password).await()
                val currentUser = authResult.user?.uid


                val verifyEmail = firebaseAuth.currentUser?.isEmailVerified
                if (verifyEmail==true){
                    if (currentUser != null){
                        FirebaseDatabase.getInstance().getReference("Users").child(currentUser).addListenerForSingleValueEvent(object   : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val currentUserData = snapshot.getValue(Users::class.java)
                                if(currentUserData?.usertype=="Employee"){
                                    startActivity(Intent(this@SigninActivity, EmployeeMainActivity::class.java))
                                    finish()
                                }
                                if(currentUserData?.usertype=="Boss"){
                                    startActivity(Intent(this@SigninActivity, BossMainActivity::class.java))
                                    finish()
                                }
//                            if(currentUserData?.usertype=="Employee"){
//                                startActivity(Intent(this@SigninActivity,EmployeeMainActivity::class.java))
//                                finish()
//                            }

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
                                utils.showtoast(this@SigninActivity,error.message)
                            }


                        })
                    }
                    else{
                        utils.hideDialog()
                        utils.showtoast(this@SigninActivity,"User not found \n Please Sign up first")

                    }
                }
                else{
                    utils.hideDialog()
                    utils.showtoast(this@SigninActivity,"Email not verified")

                }
            }
            catch (e:Exception){
                utils.hideDialog()
                utils.showtoast(this@SigninActivity,e.message!!)
            }
        }
    }
}