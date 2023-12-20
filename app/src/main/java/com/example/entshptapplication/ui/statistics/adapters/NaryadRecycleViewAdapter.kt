package com.example.entshptapplication.ui.statistics.adapters

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.NaryadItemBinding
import com.example.entshptapplication.databinding.StatisticsNaryadItemBinding
import com.example.entshptapplication.ui.statistics.models.NaryadStatisitcResponseModel

class NaryadRecycleViewAdapter(val onClick: (naryad: NaryadStatisitcResponseModel) -> Unit): RecyclerView.Adapter<NaryadRecycleViewAdapter.NaryadViewHolder> (){
    private var naryads = listOf<NaryadStatisitcResponseModel>()

    fun clear(){
        naryads = listOf()
        notifyDataSetChanged()
    }
    fun setNaryads(_naryads: List<NaryadStatisitcResponseModel>){
        naryads = _naryads
        notifyDataSetChanged()
    }

    fun delete(naryadCompliteId: Int){
        val pos = naryads.indexOf(naryads.first { it.naryadCompliteId == naryadCompliteId })
        notifyItemRemoved(pos)
    }

    inner class NaryadViewHolder(item: View):RecyclerView.ViewHolder(item){
        private val binding = StatisticsNaryadItemBinding.bind(item)
        fun bind(naryad: NaryadStatisitcResponseModel) = with(binding){
            val dateFormat = SimpleDateFormat("dd.MM.yy")
            statisticsNaryadTitle.text = naryad.shet + "/" + naryad.numInOrder + " от " + dateFormat.format(naryad.shetDate)
            statisticsNaryadNaryadNum.text = naryad.naryadNum
            statisticsNaryadCost.text = naryad.cost.toString()
            var doorNote = naryad.doorName + "  ( " + naryad.h + "x"+naryad.w+
                    (if(naryad.s!=null)
                        " x " + naryad.s
                    else
                        if(naryad.sEqual)  " x равн"  else ""
                    ) + " )"
            statisticsNaryadDoorNote.text = doorNote
        }
        init {
            binding.statisticsNaryadBtnDelete.setOnClickListener {
                onClick.invoke(naryads[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NaryadViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.statistics_naryad_item, parent, false)
        return NaryadViewHolder(view)
    }

    override fun getItemCount(): Int {
        return naryads.count()
    }

    override fun onBindViewHolder(holder: NaryadViewHolder, position: Int) {
        holder.bind(naryads.get(position))
    }
}