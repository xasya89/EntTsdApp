package com.example.entshptapplication.ui.statistics.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.StatNaryadItemBinding
import com.example.entshptapplication.databinding.StatisticsNaryadItemBinding
import com.example.entshptapplication.ui.statistics.models.NaryadStatisitcResponseModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mikepenz.fastadapter.items.AbstractItem

open class NaryadItemAdapter(val naryad: NaryadStatisitcResponseModel) : AbstractBindingItem<StatisticsNaryadItemBinding>() {
    override val type: Int
        get() = R.id.statistics_naryads_rc

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?
    ): StatisticsNaryadItemBinding {
        return StatisticsNaryadItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: StatisticsNaryadItemBinding, payloads: List<Any>) {
        binding.statisticsNaryadNaryadNum.text = naryad.naryadNum.toString()
        //binding.statisticsNaryadNumInOrder.text = naryad.numInOrder.toString()
    }

    override fun unbindView(binding: StatisticsNaryadItemBinding) {
        binding.statisticsNaryadNaryadNum.text = null
        //binding.statisticsNaryadNumInOrder.text = null
    }
}