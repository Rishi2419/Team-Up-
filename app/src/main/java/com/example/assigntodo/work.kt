package com.example.assigntodo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.assigntodo.databinding.FragmentWorkBinding


class work : Fragment() {

    private lateinit var binding: FragmentWorkBinding
    val employeeDetail by navArgs<workArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkBinding.inflate(layoutInflater)

        binding.button2.setOnClickListener {
            val action = workDirections.actionWorkToAssignwork(employeeDetail.employeeData)
            findNavController().navigate(action)
        }

        val empName = employeeDetail.employeeData.userName

        binding.tbEmpWork.apply {
            title=empName
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        return binding.root
    }
}





