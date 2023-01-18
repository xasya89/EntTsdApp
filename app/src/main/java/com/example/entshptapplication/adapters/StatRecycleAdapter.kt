package com.example.entshptapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.StatNaryadItemBinding
import com.example.entshptapplication.models.Naryad
import com.example.entshptapplication.models.StatNaryad

class StatRecycleAdapter(): RecyclerView.Adapter<StatRecycleAdapter.StatViewHolder>() {

    var onDelete:((Naryad)->Unit)? = null
    private var naryadList = listOf<StatNaryad>()
    fun setNaryads(naryads: List<StatNaryad>){
        naryadList = naryads
    }

    inner class StatViewHolder(item:View): RecyclerView.ViewHolder(item){
        val binding = StatNaryadItemBinding.bind(item)
        fun bind(naryad: StatNaryad) = with(binding){
            statNaryadItemShetTextView.text = naryad.shet
            statNaryadItemShetDateTextView.text = naryad.shetDateStr
            statNaryadItemNaryadTextView.text = naryad.num
            statNaryadItemNumInOrderTextView.text = naryad.numInOrder.toString()
            statNaryadItemDateComplite.text  =naryad.dateCompliteStr
            statNaryadItemCost.text = naryad.cost.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stat_naryad_item, parent, false)
        return StatViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {
        holder.bind(naryadList.get(position))
    }

    override fun getItemCount(): Int {
        return naryadList.size
    }
}