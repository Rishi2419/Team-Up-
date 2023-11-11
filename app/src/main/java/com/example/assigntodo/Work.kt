package com.example.assigntodo

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assigntodo.databinding.FragmentWorkBinding
import com.example.assigntodo.databinding.UnassignedDialogBinding
import com.example.assigntodo.workArgs
import com.example.assigntodo.workDirections
import com.example.assigntodo.utils.utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class work : Fragment() {

    private lateinit var binding: FragmentWorkBinding
    val employeeDetail by navArgs<workArgs>()

    private lateinit var worksAdaptor: WorksAdaptor

    private lateinit var workRoom : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkBinding.inflate(layoutInflater)

        binding.btnadd.setOnClickListener {
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

        prepareRvForWorksAdapter()
        showAllworks()
        return binding.root
    }

    private fun showAllworks() {
        utils.showdialog(requireContext())
        val bossId = FirebaseAuth.getInstance().currentUser?.uid
        workRoom = bossId+employeeDetail.employeeData.userId

        FirebaseDatabase.getInstance().getReference("Works").child(workRoom)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val workList = ArrayList<Works>()
                   for (allWorks in snapshot.children){
                       val work = allWorks.getValue(Works::class.java)
                       workList.add(work!!)
                   }
                    worksAdaptor.differ.submitList(workList)
                    utils.hideDialog()

                    binding.emptyView.visibility = if (workList.isEmpty()){
                        View.VISIBLE
                    }else{
                        View.GONE
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun prepareRvForWorksAdapter() {
        worksAdaptor= WorksAdaptor(::onUnassignedButtonClicked)
        binding.rvWorks.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter=worksAdaptor
        }
    }

    fun onUnassignedButtonClicked(works : Works){

        val dialog = UnassignedDialogBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialog.root)
            .show()

        dialog.Yes.setOnClickListener{
           unassignedWork(works)
            alertDialog.dismiss()
        }
        dialog.No.setOnClickListener{
            alertDialog.dismiss()
        }


    }

    private fun unassignedWork(works: Works) {
        works.expanded = !works.expanded

        FirebaseDatabase.getInstance().getReference("Works").child(workRoom)
            .addValueEventListener(object  : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (allWorks in snapshot.children){
                        val currentWork = allWorks.getValue(Works::class.java)
                        if (currentWork==works){
                            allWorks.ref.removeValue()

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}





