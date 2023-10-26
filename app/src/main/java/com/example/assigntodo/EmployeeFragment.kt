package com.example.assigntodo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.assigntodo.databinding.FragmentEmployeeBinding
import com.example.assigntodo.databinding.FragmentWorkBinding

class EmployeeFragment : Fragment() {

    private lateinit var binding: FragmentEmployeeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployeeBinding.inflate(layoutInflater)
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_employeeFragment_to_work)
        }
        return binding.root
    }
}