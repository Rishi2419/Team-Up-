package com.example.assigntodo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.assigntodo.databinding.ItemViewEmployeeWorkBinding
import com.google.android.material.button.MaterialButton

class EmployeeworkAdapter(
    val onProgressButtonClicked: (Works, MaterialButton) -> Unit,
    val onCompletedButtonClicked: (Works, MaterialButton) -> Unit
) : RecyclerView.Adapter<EmployeeworkAdapter.EmployeeWorkViewHolder>() {
    class EmployeeWorkViewHolder(val binding: ItemViewEmployeeWorkBinding) :
        RecyclerView.ViewHolder(binding.root)


    val diffUtil = object : DiffUtil.ItemCallback<Works>() {
        override fun areItemsTheSame(oldItem: Works, newItem: Works): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Works, newItem: Works): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeWorkViewHolder {
        return EmployeeWorkViewHolder(
            ItemViewEmployeeWorkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: EmployeeWorkViewHolder, position: Int) {
        val works = differ.currentList[position]
        val isExpanded = works.expanded
        holder.binding.apply {
            tvTitle.text = works.workTitle
            tvData.text = works.workLastDate
            tvWorkDescriptio.text = works.workDesc

            when (works.workPriority) {
                "1" -> ivOval.setImageResource(R.drawable.green_oval)
                "2" -> ivOval.setImageResource(R.drawable.yellow_oval)
                "3" -> ivOval.setImageResource(R.drawable.red_oval)
            }
            when (works.workStatus) {
                "1" -> holder.binding.tvStatus.text = "Pending"
                "2" -> holder.binding.tvStatus.text = "Progress"
                "3" -> holder.binding.tvStatus.text = "Completed"
            }
            tvWorkDescriptio.visibility = if (isExpanded) View.VISIBLE else View.GONE
            workDescT.visibility = if (isExpanded) View.VISIBLE else View.GONE
            btnProgress.visibility = if (isExpanded) View.VISIBLE else View.GONE
            btnCompleted.visibility = if (isExpanded) View.VISIBLE else View.GONE

            constraintlayout.setOnClickListener {
                isAnyItemExpanded(position)
                works.expanded = !works.expanded
                notifyItemChanged(position, 0)
            }

            if (tvStatus.text == "Progress" || tvStatus.text == "Completed"){
                btnProgress.text="In Progress"
            }
            if (tvStatus.text == "Completed"){
                btnCompleted.text="Work Completed"
            }

            btnProgress.setOnClickListener{ onProgressButtonClicked(works,btnProgress)}
            btnCompleted.setOnClickListener{onCompletedButtonClicked(works,btnCompleted)}



        }
    }

    private fun isAnyItemExpanded(positon: Int) {
        val expandedItemIndex = differ.currentList.indexOfFirst { it.expanded }
        if (expandedItemIndex >= 0 && expandedItemIndex != positon) {
            differ.currentList[expandedItemIndex].expanded = false
            notifyItemChanged(expandedItemIndex, 0)
        }
    }

    override fun onBindViewHolder(
        holder: EmployeeWorkViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads[0] == 0) {
            holder.binding.apply {
                tvWorkDescriptio.visibility = View.GONE
                workDescT.visibility = View.GONE
                btnProgress.visibility = View.GONE
                btnCompleted.visibility = View.GONE
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }


}