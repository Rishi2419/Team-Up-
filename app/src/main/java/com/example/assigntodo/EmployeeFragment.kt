package com.example.assigntodo

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assigntodo.auth.SigninActivity
import com.example.assigntodo.databinding.ForgotPasswordBinding
import com.example.assigntodo.databinding.FragmentEmployeeBinding
import com.example.assigntodo.databinding.FragmentWorkBinding
import com.example.assigntodo.databinding.ShowLogoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EmployeeFragment : Fragment() {

    private lateinit var binding: FragmentEmployeeBinding
    private lateinit var employeesAdaptor: EmployeesAdaptor



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployeeBinding.inflate(layoutInflater)

        binding.apply  {
            tbemployees.setOnMenuItemClickListener{
                when(it.itemId){
                    R.id.Logout->{
                        showlogout()
                        true
                    }
                    else-> false
                }
            }
        }


        prepareRvForEmployeeAdapter()
        showAllEmployess()
        return binding.root
    }

    private fun showAllEmployess() {
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val empList = arrayListOf<Users>()
                for (employees in snapshot.children){
                    val currentUser = employees.getValue(Users::class.java)
                    if (currentUser?.usertype == "Employee"){
                        empList.add(currentUser)
                    }
                }
                employeesAdaptor.differ.submitList(empList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



    }

    private fun prepareRvForEmployeeAdapter() {
        employeesAdaptor= EmployeesAdaptor()
        binding.rvEmployess.apply {
            layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter=employeesAdaptor
        }
    }

    private fun showLogoutDialog() {
        val builder= AlertDialog.Builder(requireContext())
        val alertDialog= builder.create()
        builder.setTitle("Log Out")
            .setMessage("Are you sure you want to Logout?")
            .setPositiveButton("Yes"){_,_->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(requireContext(),SigninActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton("No"){_,_->
                alertDialog.dismiss()
            }
            .show()
            .setCancelable(false)
    }

    private fun showlogout() {
        val dialog = ShowLogoutBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialog.root)
            .show()

        dialog.LogoutYes.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(),SigninActivity::class.java))
            requireActivity().finish()
        }
        dialog.Logoutno.setOnClickListener{
            alertDialog.dismiss()
        }

    }


}