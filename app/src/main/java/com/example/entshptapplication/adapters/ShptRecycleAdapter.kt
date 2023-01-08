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
            when(showMorePositions[position]){
                true -> onMoreShow()
                false -> onMoreHide()
            }
        }
        init {
            binding.btnMore.setOnClickListener {
                val pos = adapterPosition
                val act = actList[pos]
                val showMoreFlag = showMorePositions[pos]
                //onMoreShow()
                /*
                when(showMoreFlag){
                    true -> onMoreShow()
                    false -> onMoreHide()
                }
                 */
                showMorePositions.set(pos, true)
                for( i in 0 .. showMorePositions.size - 1){
                    if(i!=pos)
                        showMorePositions.set(i, false)
                }
                notifyDataSetChanged()
            }
            binding.btnOpen.setOnClickListener {
                onOpenAct?.invoke(actList[adapterPosition])
            }
        }
        private fun onMoreShow() = with(binding){
            row1.visibility = View.VISIBLE
            row2.visibility = View.VISIBLE
            row3.visibility = View.VISIBLE
            row4.visibility = View.VISIBLE
            btnMore.visibility = View.GONE
            btnOpen.visibility = View.VISIBLE
        }
        private fun onMoreHide() = with(binding){
            row1.visibility = View.GONE
            row2.visibility = View.GONE
            row3.visibility = View.GONE
            row4.visibility = View.GONE
            btnMore.visibility = View.VISIBLE
            btnOpen.visibility = View.GONE
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