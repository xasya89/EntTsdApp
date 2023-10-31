package com.example.entshptapplication.shpt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.ActShptDoorItemBinding
import com.example.entshptapplication.shpt.models.ActShptDoor

class ShptOneRecycleAdapter: RecyclerView.Adapter<ShptOneRecycleAdapter.ShptOneViewHolder>() {

    var doorList = listOf<ActShptDoor>()
    var onActionClick: ((ActShptDoor) -> Unit)? = null

    fun setDoors(doors: List<ActShptDoor>){
        doorList= doors
        notifyDataSetChanged()
    }

    inner class ShptOneViewHolder(item: View): RecyclerView.ViewHolder(item){
        val  binding = ActShptDoorItemBinding.bind(item)

        fun  bind(door: ActShptDoor) = with(binding){
            shptOneShetTextView.text = door.shet
            shptOneShetDateTextView.text = door.shetDateStr
            shptOneNaryadTextView.text = door.num
            shptOneNumInOrderTextView.text = door.numInOrder.toString()
            if(door.isInDb)
                binding.shptOneStatusUpload.visibility = View.VISIBLE
            else
                binding.shptOneStatusUpload.visibility = View.GONE
        }
        init {
            binding.shptOneActionsBtn.setOnClickListener {
                if(doorList.size>0)
                    onActionClick?.invoke(doorList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShptOneViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.act_shpt_door_item, parent, false)
        return ShptOneViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShptOneViewHolder, position: Int) {
        holder.bind(doorList[position])
    }

    override fun getItemCount(): Int {
        return doorList.size
    }
}