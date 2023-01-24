package com.example.entshptapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.entshptapplication.Constant
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.StatNaryadItemBinding
import com.example.entshptapplication.models.Naryad
import com.example.entshptapplication.models.StatNaryad

class StatRecycleAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onDelete:((Naryad)->Unit)? = null
    private var naryadList = arrayListOf<StatNaryad?>()

    fun setNaryads(naryads: List<StatNaryad>){
        naryadList.clear()
        naryadList.addAll(naryads)
        notifyDataSetChanged()
    }

    fun appendNaryads(naryads: List<StatNaryad>){
        if(naryadList.size > 0)
            naryadList.removeAt(naryadList.size - 1)
        naryadList.addAll(naryads)
        notifyDataSetChanged()
    }

    fun addLoading(){
        naryadList.add(null)
        notifyItemInserted(naryadList.size - 1)
    }

    inner class StatViewHolder(item:View): RecyclerView.ViewHolder(item){
        val binding = StatNaryadItemBinding.bind(item)
        fun bind(naryad: StatNaryad) = with(binding){
            statNaryadItemShetTextView.text = naryad.shet
            statNaryadItemShetDateTextView.text = naryad.shetDateStr
            statNaryadItemNaryadTextView.text = naryad.naryadNum
            statNaryadItemNumInOrderTextView.text = naryad.numInOrder.toString()
            statNaryadItemDateComplite.text  =naryad.dateCompliteStr
            statNaryadItemCost.text = naryad.cost.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType==Constant.VIEW_TYPE_ITEM){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.stat_naryad_item, parent, false)
            StatViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.progress_loading, parent, false)
            ProgressLoadingViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(naryadList[position]==null){
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val naryad = naryadList.get(position)
        if(holder.itemViewType == Constant.VIEW_TYPE_ITEM && naryad!=null)
            (holder as StatViewHolder).bind(naryad!!)
    }

    override fun getItemCount(): Int {
        return naryadList.size
    }
}