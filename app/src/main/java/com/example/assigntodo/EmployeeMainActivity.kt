package com.example.assigntodo

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.assigntodo.auth.SigninActivity
import com.example.assigntodo.databinding.ActivityEmployeeMainBinding
import com.example.assigntodo.databinding.ActivitySignupBinding
import com.example.assigntodo.databinding.ShowLogoutBinding
import com.google.firebase.auth.FirebaseAuth

class EmployeeMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEmployeeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tbEmployee.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.Logout->{
                    showlogout()
                    true
                }
                else-> false
            }
        }
    }

    private fun showLogoutDialog() {
        val builder= AlertDialog.Builder(this)
        val alertDialog= builder.create()
        builder.setTitle("Log Out")
            .setMessage("Are you sure you want to Logout?")
            .setPositiveButton("Yes"){_,_->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, SigninActivity::class.java))
              this.finish()
            }
            .setNegativeButton("No"){_,_->
                alertDialog.dismiss()
            }
            .show()
            .setCancelable(false)
    }

    private fun showlogout() {
        val dialog = ShowLogoutBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialog.root)
            .show()

        dialog.LogoutYes.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,SigninActivity::class.java))
            this.finish()
        }
        dialog.Logoutno.setOnClickListener{
            alertDialog.dismiss()
        }

    }
    }
