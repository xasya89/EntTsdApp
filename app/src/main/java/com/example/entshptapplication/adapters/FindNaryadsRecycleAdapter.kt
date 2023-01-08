package com.example.entshptapplication.adapters

import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FindNaryadItemBinding
import com.example.entshptapplication.models.FindNaryadModel
import com.example.entshptapplication.models.Naryad

class FindNaryadsRecycleAdapter():RecyclerView.Adapter<FindNaryadsRecycleAdapter.FindNaryadViewHolder>() {
    var naryadList = mutableListOf<FindNaryadModel>()
    var onItemCheck: ((FindNaryadModel) -> Boolean)? = null

    fun setNaryads(naryads: List<FindNaryadModel>){
        this.naryadList = naryads.toMutableList()
        notifyDataSetChanged()
    }

    inner class FindNaryadViewHolder(item: View):RecyclerView.ViewHolder(item){
        val binding = FindNaryadItemBinding.bind(item)
        fun bind(naryad: FindNaryadModel) = with(binding){
            findNaryadNum.text = naryad.naryadNum
            findNaryadShet.text = naryad.shet + "  " + naryad.shetDateStr
            findNaryadNumInOrder.text = naryad.numInOrder.toString()
            findNaryadSvarka.visibility = if(naryad.svarkaComplite) VISIBLE else INVISIBLE
            findNaryadSborka.visibility = if(naryad.sborkaComplite) VISIBLE else INVISIBLE
            findNaryadColor.visibility = if(naryad.colorComplite) VISIBLE else INVISIBLE
            findNaryadUpak.visibility = if(naryad.upakComplite) VISIBLE else INVISIBLE
            findNaryadShpt.visibility = if(naryad.shptComplite) VISIBLE else INVISIBLE
            when(naryad.onChecked){
                true -> binding.finNaryadBtnCheck.setImageResource(R.drawable.ic_baseline_check_circle_35)
                false -> binding.finNaryadBtnCheck.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_35)
            }
        }
        init {
            //binding.finNaryadBtnCheck?.setOnLongClickListener(object : View.OnLongClickListener{
            binding.finNaryadBtnCheck?.setOnClickListener (object : View.OnClickListener{
                /*
                override fun onClick(p0: View?): Boolean {
                    val result = onItemCheck?.invoke(naryadList[adapterPosition]) ?: false
                    when(result){
                        true -> binding.finNaryadBtnCheck.setImageResource(R.drawable.ic_baseline_check_circle_35)
                        false -> binding.finNaryadBtnCheck.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_35)
                    }
                    /*
                    val naryad = naryadList.get(adapterPosition)
                    naryad.onChecked = !naryad.onChecked
                    if(naryad.onChecked){
                        binding.finNaryadBtnCheck.setImageResource(R.drawable.ic_baseline_check_circle_35)
                    } else
                        binding.finNaryadBtnCheck.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_35)
                     */
                    return true
                }

                 */
                override fun onClick(p0: View?) {
                    val result = onItemCheck?.invoke(naryadList[adapterPosition]) ?: false
                    when(result){
                        true -> binding.finNaryadBtnCheck.setImageResource(R.drawable.ic_baseline_check_circle_35)
                        false -> binding.finNaryadBtnCheck.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_35)
                    }
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindNaryadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.find_naryad_item, parent, false)
        return FindNaryadViewHolder(view)
    }

    override fun onBindViewHolder(holder: FindNaryadViewHolder, position: Int) {
        holder.bind(naryadList.get(position))
    }

    override fun getItemCount(): Int {
        return naryadList.size
    }
}

