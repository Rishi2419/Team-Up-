package com.example.assigntodo

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.assigntodo.databinding.FragmentAssignworkBinding
import com.example.assigntodo.utils.utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import okhttp3.internal.Util
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class assignwork : Fragment() {
    val employeeData by navArgs<assignworkArgs>()
    private lateinit var binding: FragmentAssignworkBinding
    private var priority = "1"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAssignworkBinding.inflate(layoutInflater)
        binding.tbasswrk.apply {
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }

        }
        setpriority()
        setDate()
        binding.btnDone.setOnClickListener{
            assignWork()
        }

        return binding.root
    }

    private fun assignWork() {
        utils.showdialog(requireContext())

        val workTitle = binding.etTitle.text.toString()
        val workDescriptin = binding.WorkDesc.text.toString()
        val workLastDate = binding.tvData.text.toString()

        if (workTitle.isEmpty()){
            utils.apply {
                utils.hideDialog()
                utils.showtoast(requireContext(),"Please select work title")
            }

        }
        else if (workLastDate== "Last date : "){
            utils.apply {
                utils.hideDialog()
                utils.showtoast(requireContext(),"Please choose last date")
            }
        }
        else{
            val empId = employeeData.employeeDetail.userId
            val bossId = FirebaseAuth.getInstance().currentUser?.uid
            val workRoom = bossId+empId

            val work = Works(
                workTitle = workTitle,
                workDesc = workDescriptin,
                workPriority = priority,
                workLastDate = workLastDate,
                workStatus = "1"
            )

            FirebaseDatabase.getInstance().getReference("Works").child(workRoom).push().setValue(work)
                .addOnSuccessListener {
                    utils.hideDialog()
                    utils.showtoast(requireContext(),"Works has been assigned to ${employeeData.employeeDetail.userName}")
                    val action = assignworkDirections.actionAssignworkfragmentToWorkfragment(employeeData.employeeDetail)
                    findNavController().navigate(action)
                }

        }
    }


    private fun setDate() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener{view,year,month,dayOfMonth->
            myCalendar.apply {
                set(Calendar.YEAR,year)
                set(Calendar.MONTH,month)
                set(Calendar.DAY_OF_MONTH,dayOfMonth)
                updateLable(myCalendar)
            }
        }
        binding.datepicker.setOnClickListener{
            DatePickerDialog(requireContext(),datePicker,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.tvData.text=sdf.format(myCalendar.time)
    }

    private fun setpriority() {
        binding.apply {
            greenOval.setOnClickListener {
                utils.showtoast(requireContext(), "Priority : Low")
                priority = "1"
                binding.greenOval.setImageResource(R.drawable.green_oval)
                binding.yellowOval.setImageResource(R.drawable.yellow_selected)
                binding.redOval.setImageResource(R.drawable.red_selcted)
            }



            yellowOval.setOnClickListener {
                utils.showtoast(requireContext(), "Priority : Medium")
                priority = "2"
                binding.yellowOval.setImageResource(R.drawable.yellow_oval)
                binding.greenOval.setImageResource(R.drawable.green_seleted)
                binding.redOval.setImageResource(R.drawable.red_selcted)
            }



            redOval.setOnClickListener {
                utils.showtoast(requireContext(), "Priority : High")
                priority = "3"
                binding.redOval.setImageResource(R.drawable.red_oval)
                binding.yellowOval.setImageResource(R.drawable.yellow_selected)
                binding.greenOval.setImageResource(R.drawable.green_seleted)
            }
        }

    }
}



