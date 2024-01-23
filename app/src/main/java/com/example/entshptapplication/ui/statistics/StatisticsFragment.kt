package com.example.entshptapplication.ui.statistics

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentStatisticsBinding
import com.example.entshptapplication.dialogs.GenericConfirmDialog
import com.example.entshptapplication.ui.actions.ActionsFragment
import com.example.entshptapplication.ui.statistics.adapters.NaryadItemAdapter
import com.example.entshptapplication.ui.statistics.adapters.NaryadRecycleViewAdapter
import com.example.entshptapplication.ui.statistics.models.NaryadStatisitcResponseModel
import com.google.android.material.tabs.TabLayout
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding
    private val creatorViewModel by activityViewModels<StatisticsCreatorViewModel>()
    private val statisticsViewModel by activityViewModels<StatisticsViewModel>()
    private lateinit var naryadItemAdapter:ItemAdapter<NaryadItemAdapter>
    private lateinit var naryadRecycleViewAdapter: NaryadRecycleViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsBinding.inflate(inflater)
        initBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val summary = creatorViewModel.summary.value
        statisticsViewModel.setSummary(summary!!)
        setSummaryValuesInTextView()

        statisticsViewModel.naryads.observe(viewLifecycleOwner,{
            naryadRecycleViewAdapter.setNaryads(it)
            _isMoreLoading = false
        })
        statisticsViewModel.getNaryads(true)

    }

    private fun setSummaryValuesInTextView() = with(binding){
        val summary = statisticsViewModel.getSummaryOnDay()
        statisticsLastActSum.text = summary.lastActSum.toString()
        statisticsPaymentsSum.text = summary.paymentsSumAll.toString()
        statisticsUpakSum.text = summary.upakSumAll.toString()
        statisticsShptSum.text = summary.shptSumAll.toString()
        statisticsAllSum.text = (summary.lastActSum + summary.paymentsSumAll + summary.upakSumAll + summary.shptSumAll).toString()
        /*
        statisticsAllSum.text = if(statisticsViewModel.selectedDay?.value ==null)
            (summary.lastActSum + summary.paymentsSumAll + summary.upakSumAll + summary.shptSumAll).toString()
        else
            (summary.upakSumAll + summary.shptSumAll).toString()
         */
        val dateFormat = SimpleDateFormat("dd.MM.YY")

        statisticsSelectedDateTv.text =
            if(statisticsViewModel.selectedDay.value==null)
                "от " + dateFormat.format(statisticsViewModel.dates.value!!.filter { it!=null }.first())
            else
                dateFormat.format(statisticsViewModel.selectedDay.value)
    }

    private fun initBinding() = with(binding) {
        statisticsChooseDateBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, StatisticsChooseDateFragment.newInstance())
                setReorderingAllowed(true)
            }
        }

        statisticsChooseDateCloseBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                setReorderingAllowed(true)
            }
        }

        statisticsTabView.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.text.toString()){
                    "Упаковка" -> statisticsViewModel.setSelectStep(7)
                    "Погрузка" -> statisticsViewModel.setSelectStep(8)
                    else -> statisticsViewModel.setSelectStep(7)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        initNaryadsRecycleView()
    }

    private var _isMoreLoading: Boolean = false
    private fun initNaryadsRecycleView(){
        val layoutManager = LinearLayoutManager(context)
        binding.statisticsNaryadsRc.layoutManager = layoutManager
        naryadRecycleViewAdapter =  NaryadRecycleViewAdapter {
            GenericConfirmDialog(
                requireContext(),
                layoutInflater,
                "Удалить наряд " + it.shet + "/" + it.numInOrder,
                {
                    statisticsViewModel.cancelNaryadComplite(it.naryadCompliteId, {
                        naryadRecycleViewAdapter.delete(it.naryadCompliteId)
                    })
                }).show()
        }
        binding.statisticsNaryadsRc.adapter = naryadRecycleViewAdapter

        binding.statisticsNaryadsRc.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy <= 0) return
                val totalCount = layoutManager.itemCount
                val lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!_isMoreLoading && totalCount <= lastVisibleItem + 5) {
                    statisticsViewModel.getNaryads()
                    _isMoreLoading = true
                }
            }
        })
    }

    private fun initBindingFastAdapter(){
        binding.statisticsNaryadsRc.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val footerAdapter = GenericItemAdapter()
        naryadItemAdapter = ItemAdapter<NaryadItemAdapter>()
        val naryadFastItemAdapter = FastAdapter.with(listOf( naryadItemAdapter, footerAdapter))
        binding.statisticsNaryadsRc.adapter = naryadFastItemAdapter
        val naryads = mutableListOf<NaryadStatisitcResponseModel>()
        FastAdapterDiffUtil[naryadItemAdapter] = naryads.map(::NaryadItemAdapter)
        naryadFastItemAdapter.addEventHook(object : ClickEventHook<NaryadItemAdapter>(){
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                Log.d("viewholder",viewHolder.toString())

                return super.onBind(viewHolder)
            }
            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<NaryadItemAdapter>,
                item: NaryadItemAdapter
            ) {
                Log.d("click", view.toString())
            }

        })
        binding.statisticsNaryadsRc.addOnScrollListener(object : EndlessRecyclerOnScrollListener(footerAdapter){
            override fun onLoadMore(currentPage: Int) {
                Log.d("load more", "load")
                footerAdapter.clear()
                statisticsViewModel.getNaryads()
                //naryadItemAdapter.add(listOf( NaryadStatisitcResponseModel(0,0,"", Date(), 5,"", (1).toDouble())).map(::NaryadItemAdapter))
            }
            /*
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            if (dy > 0) {

                                val visibleItemCount = layoutManager.childCount;
                                val totalItemCount = layoutManager.itemCount;
                                val pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                                if (loading) {
                                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                        loading = false;
                                        Log.v("...", "Last Item Wow !");
                                        // Do pagination.. i.e. fetch new data
                                        loading = true;
                                    }
                                }
                            }
                        }
                        */
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            StatisticsFragment()
    }
}