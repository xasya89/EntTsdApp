package com.example.entshptapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.StatChooseDateItemBinding

class StatChooseDateRecycleAdapter(private val dateList: List<String>, private val onChoose: ((String)->Unit)): RecyclerView.Adapter<StatChooseDateRecycleAdapter.StatChooseDateViewHolder>() {
    inner class StatChooseDateViewHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = StatChooseDateItemBinding.bind(item)
        fun bind(datestr: String){
            binding.statChooseDateItemTextView.text = datestr
        }
        init {
            item.setOnClickListener {
                onChoose.invoke(dateList.get(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatChooseDateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stat_choose_date_item, parent, false)
        return StatChooseDateViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatChooseDateViewHolder, position: Int) {
        holder.bind(dateList.get(position))
    }

    override fun getItemCount(): Int {
        return dateList.size
    }
}