package com.example.entshptapplication.ui.statistics

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentStatisticsBinding
import com.example.entshptapplication.fragments.ActionsFragment
import com.example.entshptapplication.ui.statistics.adapters.NaryadItemAdapter
import com.example.entshptapplication.ui.statistics.models.NaryadStatisitcResponseModel
import com.google.android.material.tabs.TabLayout
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class StatisticsFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding
    private lateinit var creatorViewModel: StatisticsCreatorViewModel
    private lateinit var statisticsViewModel: StatisticsViewModel
    private lateinit var naryadItemAdapter:ItemAdapter<NaryadItemAdapter>
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
        creatorViewModel = StatisticsCreatorViewModelFactory.Create(this)
        val summary = creatorViewModel.summary.value
        statisticsViewModel = StatisticsViewModelFactory.Create(this)
        statisticsViewModel.setSummary(summary!!)
        setSummaryValuesInTextView()

        statisticsViewModel.naryads.observe(viewLifecycleOwner,{
            naryadItemAdapter.clear()
            naryadItemAdapter.add(it.map (::NaryadItemAdapter))
        })
        loadNaryads()

    }

    private fun setSummaryValuesInTextView() = with(binding){
        val summary = statisticsViewModel.getSummaryOnDay()
        statisticsLastActSum.text = summary.lastActSum.toString()
        statisticsPaymentsSum.text = summary.paymentsSumAll.toString()
        statisticsUpakSum.text = summary.upakSumAll.toString()
        statisticsShptSum.text = summary.shptSumAll.toString()
        val dateFormat = SimpleDateFormat("dd.MM.YY")

        statisticsSelectedDateTv.text =
            (if(statisticsViewModel.selectedDay.value==null) "от " else "") +
            dateFormat.format( statisticsViewModel.selectedDay.value ?: statisticsViewModel.dates.value!!.min())
    }

    private fun  loadNaryads(){
        statisticsViewModel.getNaryads()

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
                Log.d("Select tab", tab?.text.toString())
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

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