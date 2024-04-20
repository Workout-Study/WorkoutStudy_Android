package com.fitmate.fitmate.presentation.ui.myfit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentMyfitBinding
import com.fitmate.fitmate.domain.model.MyFitRecordHistoryDetail
import com.fitmate.fitmate.presentation.ui.myfit.list.adapter.DayListAdapter
import com.fitmate.fitmate.presentation.ui.myfit.list.adapter.MonthListAdapter
import com.fitmate.fitmate.presentation.viewmodel.MyFitViewModel
import com.fitmate.fitmate.ui.myfit.list.adapter.MyFitGroupProgressAdapter
import com.fitmate.fitmate.ui.myfit.list.adapter.MyFitHistoryAdapter
import com.fitmate.fitmate.util.ControlActivityInterface
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.ZoneId

@AndroidEntryPoint
class MyFitFragment : Fragment() {
    private lateinit var binding: FragmentMyfitBinding
    private lateinit var controlActivityInterface: ControlActivityInterface

    private val fitGroupProgressAdapter: MyFitGroupProgressAdapter by lazy {
        MyFitGroupProgressAdapter(
            requireContext()
        )
    }

    private lateinit var calendarAdapter: MonthListAdapter
    private lateinit var fitHistoryAdapter: MyFitHistoryAdapter

    private val viewModel: MyFitViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyfitBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        controlActivityInterface = activity as MainActivity
        controlActivityInterface.viewNavigationBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //툴바 버튼 클릭 리스너 초기화
        initButtonClickListener()

        //그룹 fitProgress 리사이클러뷰 데이터 연결
        observeFitProgress()

        //운동 기록 리사이클러뷰 mock 데이터 연결
        initMyFitHistoryRecyclerView()

        // 캘린더 연결
        initCalendar()

        //캘린더에서 통신한 운동 기록 데이터를 감시
        viewModel.myFitRecordHistory.observe(viewLifecycleOwner) {
            calendarAdapter.calendarHandler.fitRecordHistoryDataResult = it
            calendarAdapter.notifyDataSetChanged()
        }

        //플로팅 버튼
        setToggleAppBar()
    }

    private fun initCalendar() {
        calendarAdapter = MonthListAdapter(CalendarHandler()) { fitHistoryList ->
            Log.d("tlqkf","$fitHistoryList")
            Log.d("tlqkf",fitHistoryAdapter.toString())
            fitHistoryAdapter.submitList(fitHistoryList.toMutableList())
        }
        binding.recyclerViewCalendar.run {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = calendarAdapter
            scrollToPosition(Int.MAX_VALUE / 2)
            PagerSnapHelper().attachToRecyclerView(this)
        }
        calendarAdapter.calendarHandler.myFitHistoryAdapter = fitHistoryAdapter
    }

    private fun initMyFitHistoryRecyclerView() {
        fitHistoryAdapter = MyFitHistoryAdapter(requireContext())
        binding.recyclerViewMyFitFragmentFitHistory.apply {
            fitHistoryAdapter = MyFitHistoryAdapter(requireContext())
            adapter = fitHistoryAdapter
        }
    }

    private fun initButtonClickListener() {

        binding.buttonFragmentMyFitFitOff.setOnClickListener {
            //findNavController().navigate(R.id.action_myFitFragment_to_myFitOffFragment)
            Toast.makeText(requireContext(), "추후 업데이트 예정입니다", Toast.LENGTH_SHORT).show()
        }
        binding.buttonFragmentMyFitRecord.setOnClickListener {
            findNavController().navigate(R.id.action_myFitFragment_to_certificateFragment)
        }
    }

    private fun observeFitProgress() {
        observeNetworkMyProgress()
        viewModel.getMyFitProgress("hyungoo")
    }

    private fun observeNetworkMyProgress() =
        viewModel.fitProgressItem.observe(viewLifecycleOwner) { fitProgressList ->
            fitGroupProgressAdapter.submitList(fitProgressList) {
                binding.recyclerviewMyFitFragmentMyFitProgress.adapter = fitGroupProgressAdapter
            }
        }

    private fun setToggleAppBar() {
        binding.floatingButtonScrollTop.setOnClickListener {
            binding.contentsNestedSScrollView.fullScroll(ScrollView.FOCUS_UP)
            binding.toolbarLayout.setExpanded(true, true)
        }
    }

    inner class CalendarHandler() {
        var innerAdapter: DayListAdapter? = null
        val vM = viewModel
        var fitRecordHistoryDataResult: List<MyFitRecordHistoryDetail> = emptyList()
        var myFitHistoryAdapter: MyFitHistoryAdapter? = null
        var tempMonth: Int? = null
        fun networkMyFitRecordHistory(year: Int, month: Int) {
            if (tempMonth != month){
                tempMonth = month
                val (startDate, endDate) = getStartAndEndInstantsForYearMonth(year, month)
                viewModel.getMyFitRecordHistory("hyungoo", startDate, endDate)
            }
        }

        fun getStartAndEndInstantsForYearMonth(year: Int, month: Int): Pair<String, String> {
            // 입력된 년도와 월로 LocalDate 객체 생성
            val firstDayOfMonth = LocalDate.of(year, month, 1)
            val lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1)

            // 해당 월의 첫 번째 날의 00:00:00과 마지막 날의 23:59:59의 Instant 구하기
            val startInstant =
                firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toString()
            val endInstant =
                lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusSeconds(1)
                    .toInstant().toString()

            return Pair(startInstant, endInstant)
        }
    }

}