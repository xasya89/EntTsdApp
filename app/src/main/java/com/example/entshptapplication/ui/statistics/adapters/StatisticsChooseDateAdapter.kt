package com.example.entshptapplication.ui.statistics.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.StatisticsChooseDateItemBinding
import java.text.SimpleDateFormat
import java.util.Date

class StatisticsChooseDateAdapter(): RecyclerView.Adapter<StatisticsChooseDateAdapter.ChooseDateHolder>() {

    private var days: List<Date> = listOf()
    var onItemClick: ((date:Date) -> Unit)? = null

    fun setDays(_days: List<Date>){
        days = _days
        notifyDataSetChanged()
    }
    inner class ChooseDateHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = StatisticsChooseDateItemBinding.bind(item)
        fun bind(date: Date) = with(binding){
            val dateFormating = SimpleDateFormat("dd.MM.yy")
            statisticsChooseDateItemTv.text = dateFormating.format(date)
        }
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(days[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseDateHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.statistics_choose_date_item, parent, false)
        return ChooseDateHolder(view)
    }

    override fun getItemCount(): Int {
        return days.count()
    }

    override fun onBindViewHolder(holder: ChooseDateHolder, position: Int) {
        holder.bind(days.get(position))
    }
}