package com.example.assigntodo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.assigntodo.databinding.ItemViewEmpProfileBinding

class EmployeesAdaptor :RecyclerView.Adapter<EmployeesAdaptor.EmplyoeeViewHolder>(){
    class EmplyoeeViewHolder (val binding: ItemViewEmpProfileBinding):ViewHolder(binding.root)


    val diffUtil = object : DiffUtil.ItemCallback<Users>(){
        override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
           return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
           return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmplyoeeViewHolder {
        return EmplyoeeViewHolder(ItemViewEmpProfileBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: EmplyoeeViewHolder, position: Int) {
       val empdata = differ.currentList[position]
        holder.binding.apply {
            Glide.with(holder.itemView).load(empdata.userImage).into(ivEmployeeProfile)
            tvEmployeeName.text=empdata.userName
        }
    }


}