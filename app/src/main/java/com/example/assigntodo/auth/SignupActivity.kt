package com.example.assigntodo.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.assigntodo.Boss
import com.example.assigntodo.Employee
import com.example.assigntodo.R
import com.example.assigntodo.databinding.ActivitySignupBinding
import com.example.assigntodo.utils.utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private var userImageUri :Uri? = null

    private var userType:String=""

    private val selectimage = registerForActivityResult(ActivityResultContracts.GetContent()){
        userImageUri = it
         binding.ivUserImage.setImageURI(userImageUri)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            ivUserImage.setOnClickListener{
                selectimage.launch("image/*")
            }


        binding.radiogroup.setOnCheckedChangeListener{_,checkedId->
            userType=findViewById<RadioButton>(checkedId).text.toString()
            Log.d("TT",userType)
        }

            binding.btnregistor.setOnClickListener{createuser()}
        }
    }

    private fun createuser() {
      utils.showdialog(this)

        val name = binding.etName.text.toString()
        val email= binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() ){
             if (userImageUri==null){
                 utils.hideDialog()
                 utils.showtoast(this,"Please select a profile photo")
             }
             else if (password == confirmPassword){
                 uploadImageUri(name,email,password)
             }
            else{
                 utils.hideDialog()
                utils.showtoast(this,"Password are not matching")
             }
        }
        else{
            utils.hideDialog()
            utils.showtoast(this,"Empty fields are not allowed")
        }

    }

    private fun uploadImageUri(name: String, email: String, password: String) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val storageReference = FirebaseStorage.getInstance().getReference("Profile").child(currentUserUid).child("Profile.jpg")

        lifecycleScope.launch {
            try {
                val uploadTask = storageReference.putFile(userImageUri!!).await()
                if(uploadTask.task.isSuccessful){
                    val downloadURL=storageReference.downloadUrl.await()
                    saveUserData(name,email,password,downloadURL)
                }
                else{
                    utils.hideDialog()
                    showToast("Upload failed: ${uploadTask.task.exception?.message}")
                }
            }
            catch (e:Exception){
                utils.hideDialog()
                showToast("Upload failed: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }

    }

    private fun saveUserData(name: String, email: String, password: String, downloadURL: Uri?) {
        if (userType=="Boss"){
            lifecycleScope.launch {
                val db = FirebaseDatabase.getInstance().getReference("Boss")
                try {
                    val firebaseAuth = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).await()

                    if (firebaseAuth.user != null){
                        val uId = firebaseAuth.user?.uid.toString()
                        val boss = Boss(uId,name,email,password,downloadURL.toString())
                        db.child(uId).setValue(boss).await()
                        utils.hideDialog()
                        utils.showtoast(this@SignupActivity,"Signed Up Successfully!")
                        val intent = Intent(this@SignupActivity,SigninActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        utils.hideDialog()
                        utils.showtoast(this@SignupActivity,"Signed Up Failed")
                    }
                }
                catch (e:Exception){
                        utils.hideDialog()
                    utils.showtoast(this@SignupActivity,e.message.toString())
                }
            }
        }
        if (userType=="Employee"){
            lifecycleScope.launch {
                val db = FirebaseDatabase.getInstance().getReference("Employee")
                try {
                    val firebaseAuth = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).await()

                    if (firebaseAuth.user != null){
                        val uId = firebaseAuth.user?.uid.toString()
                        val emp = Employee(uId,name,email,password,downloadURL.toString())
                        db.child(uId).setValue(emp).await()
                        utils.hideDialog()
                        utils.showtoast(this@SignupActivity,"Signed Up Successfully!")
                        val intent = Intent(this@SignupActivity,SigninActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        utils.hideDialog()
                        utils.showtoast(this@SignupActivity,"Signed Up Failed")
                    }
                }
                catch (e:Exception){
                        utils.hideDialog()
                    utils.showtoast(this@SignupActivity,e.message.toString())
                }
            }
        }
    }
}