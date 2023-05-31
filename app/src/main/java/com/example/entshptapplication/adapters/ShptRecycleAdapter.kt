package com.example.entshptapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.ActShptItemBinding
import com.example.entshptapplication.models.ActShpt

class ShptRecycleAdapter: RecyclerView.Adapter<ShptRecycleAdapter.ShptViewHolder>() {
    var actList = listOf<ActShpt>()
    var showMorePositions = mutableListOf<Boolean>()
    var onOpenAct: ((ActShpt) -> Unit)? = null

    fun  setList(acts: List<ActShpt>){
        actList = acts
        showMorePositions.clear()
        for(i in 0..acts.size-1)
            showMorePositions.add(false)
        notifyDataSetChanged()
    }

    inner class ShptViewHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = ActShptItemBinding.bind(item)
        fun  bind(actShpt: ActShpt, position: Int) = with(binding){
            shetTextView.text = actShpt.shet?:""
            customerTextView.text = actShpt.orgName ?: ""
            doorCountTextView.text = actShpt.doorCount?.toString() ?: ""
            actNumTextView.text = actShpt.actNum.toString()
            fahrerTextView.text = actShpt.fahrer
            carNumTextView.text = actShpt.carNum
        }
        init {
            binding.btnOpen.setOnClickListener {
                onOpenAct?.invoke(actList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShptViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.act_shpt_item, parent, false)
        return ShptViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShptViewHolder, position: Int) {
        holder.bind(actList[position], position)
    }

    override fun getItemCount(): Int {
        return actList.size
    }
}