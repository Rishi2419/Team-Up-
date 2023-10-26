package com.example.assigntodo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.assigntodo.databinding.FragmentWorkBinding


class work : Fragment() {

    private lateinit var binding: FragmentWorkBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkBinding.inflate(layoutInflater)
        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.action_work_to_assignwork)
        }
        return binding.root
    }
}





