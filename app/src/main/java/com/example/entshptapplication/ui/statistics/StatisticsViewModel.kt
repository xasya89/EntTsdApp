package com.example.entshptapplication.ui.statistics

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.ui.statistics.communications.StatisticsApi
import com.example.entshptapplication.ui.statistics.models.NaryadStatisitcResponseModel
import com.example.entshptapplication.ui.statistics.models.SummaryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class StatisticsViewModel(val  statisticsApi: StatisticsApi): ViewModel() {
    val summary = MutableLiveData<SummaryModel>()
    val dates = MutableLiveData<MutableList<Date>>(mutableListOf())
    val selectedDay = MutableLiveData<Date?>(null)
    val naryads = MutableLiveData<List<NaryadStatisitcResponseModel>>(listOf())
    val naryadCount = MutableLiveData<Int>(-1)
    val selectStep = MutableLiveData<Int>(7)

    fun setSummary(_summary: SummaryModel){
        summary.value=_summary
        val dayList = mutableListOf<Date>()
        summary.value!!.paymentDays.forEach {pay ->
            if(!dayList.any { it == pay.day })
                dayList.add(pay.day)
        }
        summary.value!!.upakDays.forEach {pay ->
            if(!dayList.any { it == pay.day })
                dayList.add(pay.day)
        }
        summary.value!!.shptDays.forEach {pay ->
            if(!dayList.any { it == pay.day })
                dayList.add(pay.day)
        }
        dayList.sort()
        dates.value = dayList
    }

    fun setSelectDay(day: Date?){
        selectedDay.value = day
        getNaryads(true)
    }

    fun getSummaryOnDay(): SummaryModel{
        val _summary = summary.value!!
        if(selectedDay.value==null)
            return _summary

        val paymentsSum = _summary.paymentDays.find { it.day==selectedDay.value!! }?.sum ?: 0.0
        val upakSum = _summary.upakDays.find { it.day==selectedDay.value!! }?.compliteSum ?: 0.0
        val upakCount = _summary.upakDays.find { it.day==selectedDay.value!! }?.compliteCount ?: 0
        val shptSum = _summary.shptDays.find { it.day==selectedDay.value!! }?.compliteSum ?: 0.0
        val shptCount = _summary.shptDays.find { it.day==selectedDay.value!! }?.compliteCount ?: 0
        return SummaryModel(
            _summary.workerId,
            _summary.lastActSum,
            paymentsSum,
            upakSum,
            shptSum,
            paymentDays = _summary.paymentDays,
            upakDays = _summary.upakDays,
            shptDays = _summary.shptDays
        )
    }

    private val _skip = MutableLiveData<Int>(0)
    fun setSelectStep(_step: Int){
        selectStep.value = _step
        naryads.value = listOf()
        naryadCount.value = -1
        getNaryads(true)
    }
    fun getNaryads(reset:Boolean = false){
        if(reset){
            naryads.value = listOf()
            naryadCount.value = -1
            _skip.value = 0
        }
        viewModelScope.launch {
            val _summary = summary.value!!
            val result = statisticsApi.getNaryads(_summary.workerId, dates.value!!.min(), selectStep.value!!, selectedDay.value, _skip.value, 100)
            val list = mutableListOf<NaryadStatisitcResponseModel>()
            list.addAll(naryads.value!!)
            list.addAll(result.naryadList)
            naryadCount.postValue(result.count)
            naryads.postValue(list)
            _skip.postValue(_skip.value!! + 200)
        }
    }
    fun cancelNaryadComplite(naryadCompliteId: Int, onSuccess: ()->Unit){
        viewModelScope.launch (Dispatchers.IO){
            statisticsApi.deleteNaryadComplite(summary.value!!.workerId, naryadCompliteId)
            viewModelScope.launch (Dispatchers.Main){
                onSuccess.invoke()
            }
        }
    }
}

class StatisticsViewModelFactory constructor(private val api: StatisticsApi): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatisticsViewModel(this.api) as T
    }

    companion object{
        fun Create(fragment: Fragment): StatisticsViewModel {
            val api = StatisticsApi.getInstance(HOSTED_NAME)
            return ViewModelProvider(fragment.requireActivity().viewModelStore, StatisticsViewModelFactory(api)).get(
                StatisticsViewModel::class.java)
        }
    }
}
