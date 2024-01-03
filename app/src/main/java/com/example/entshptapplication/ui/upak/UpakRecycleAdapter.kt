package com.example.entshptapplication.ui.upak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.NaryadItemBinding
import com.example.entshptapplication.models.Naryad

class UpakRecycleAdapter: RecyclerView.Adapter<UpakRecycleAdapter.UpakViewHolder>() {
    var naryadList = mutableListOf<Naryad>()
    var onItemClick: ((Naryad) -> Unit)? = null

    fun setNaryads(naryads: List<Naryad>){
        this.naryadList = naryads.toMutableList()
        notifyDataSetChanged()
    }

    inner class UpakViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = NaryadItemBinding.bind(item)
         fun bind(naryad: Naryad) = with(binding){
             naryadItemShet.text = naryad.shet
             naryadItemShetDate.text = naryad.shetDateStr
             naryadItemNum.text = naryad.num
             naryadItemNumInOrder.text = naryad.numInOrder.toString()
             naryadItemCost.text = naryad.upakCost.toString()
         }
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(naryadList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpakViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.naryad_item, parent, false)
        return UpakViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpakViewHolder, position: Int) {
        holder.bind(naryadList.get(position))
    }

    override fun getItemCount(): Int {
        return naryadList.size
    }
}