package com.example.assigntodo

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assigntodo.auth.SigninActivity
import com.example.assigntodo.databinding.ActivityEmployeeMainBinding
import com.example.assigntodo.databinding.CompleteDialogBinding
import com.example.assigntodo.databinding.ShowLogoutBinding
import com.example.assigntodo.databinding.StartProgressDialogBinding
import com.example.assigntodo.databinding.UnassignedDialogBinding
import com.example.assigntodo.utils.utils
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EmployeeMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeMainBinding
    private lateinit var employeeworkAdapter: EmployeeworkAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tbEmployee.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.Logout -> {
                    showlogout()
                    true
                }

                else -> false
            }
        }

        prepareRvForEmployeeWorkAdapter()
        showEmployeeWorks()
    }

    private fun showEmployeeWorks() {
        utils.showdialog(this)
        val empId = FirebaseAuth.getInstance().currentUser?.uid
        val workRef = FirebaseDatabase.getInstance()
        workRef.getReference("Works").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (workRooms in snapshot.children) {
                    if (workRooms.key?.contains(empId!!) == true) {
                        val empWorkref = workRef.getReference("Works").child(workRooms.key!!)
                        empWorkref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val workList = ArrayList<Works>()
                                for (works in snapshot.children) {
                                    val work = works.getValue(Works::class.java)
                                    workList.add(work!!)
                                }
                                employeeworkAdapter.differ.submitList(workList)
                                utils.hideDialog()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun prepareRvForEmployeeWorkAdapter() {
        employeeworkAdapter =
            EmployeeworkAdapter(::onProgressButtonClicked, ::onCompletedButtonClicked)
        binding.rvEmployeeWork.apply {
            layoutManager =
                LinearLayoutManager(this@EmployeeMainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = employeeworkAdapter
        }
    }

    private fun onProgressButtonClicked(works: Works, progressButton: MaterialButton) {

        if (progressButton.text != "In Progress") {

            val dialog = StartProgressDialogBinding.inflate(layoutInflater)

            val alertDialog = AlertDialog.Builder(this)
                .setView(dialog.root)
                .show()

            dialog.Yes.setOnClickListener {

                progressButton.apply {
                    text = "In Progress"
                }
                updateStatus(works, "2")
                alertDialog.dismiss()
            }
            dialog.No.setOnClickListener {
                alertDialog.dismiss()
            }
        } else {
            utils.showtoast(this, "Work is already in progress or completed")
        }

    }

    private fun onCompletedButtonClicked(works: Works, completedButton: MaterialButton) {
        if (completedButton.text != "Work Completed") {

            val dialog = CompleteDialogBinding.inflate(layoutInflater)

            val alertDialog = AlertDialog.Builder(this)
                .setView(dialog.root)
                .show()

            dialog.Yes.setOnClickListener {

                completedButton.apply {
                    text = "Work Completed"
                }
                updateStatus(works, "3")
                alertDialog.dismiss()
            }
            dialog.No.setOnClickListener {
                alertDialog.dismiss()
            }
        } else {
            utils.showtoast(this, "Work is already in progress or completed")
        }
    }

    private fun updateStatus(works: Works, status: String) {

        val empId = FirebaseAuth.getInstance().currentUser?.uid
        val workRef = FirebaseDatabase.getInstance()

        workRef.getReference("Works").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (workRooms in snapshot.children) {
                    if (workRooms.key?.contains(empId!!) == true) {
                        val empWorkref = workRef.getReference("Works").child(workRooms.key!!)
                        empWorkref.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                for (allworks in snapshot.children) {
                                    val work = allworks.getValue(Works::class.java)
                                    if (works.workId == work?.workId) {
                                        empWorkref.child(allworks.key!!).child("workStatus")
                                            .setValue(status)
                                    }
                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        val alertDialog = builder.create()
        builder.setTitle("Log Out")
            .setMessage("Are you sure you want to Logout?")
            .setPositiveButton("Yes") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, SigninActivity::class.java))
                this.finish()
            }
            .setNegativeButton("No") { _, _ ->
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

        dialog.LogoutYes.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, SigninActivity::class.java))
            this.finish()
        }
        dialog.Logoutno.setOnClickListener {
            alertDialog.dismiss()
        }

    }
}
