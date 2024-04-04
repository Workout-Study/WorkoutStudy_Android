package com.fitmate.fitmate.presentation.ui.myfit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentMyfitBinding
import com.fitmate.fitmate.domain.model.FitHistory
import com.fitmate.fitmate.domain.model.MyFitGroupProgress
import com.fitmate.fitmate.presentation.ui.myfit.list.adapter.MonthListAdapter
import com.fitmate.fitmate.ui.myfit.list.adapter.MyFitGroupProgressAdapter
import com.fitmate.fitmate.ui.myfit.list.adapter.MyFitHistoryAdapter
import com.fitmate.fitmate.util.ControlActivityInterface

class MyFitFragment : Fragment() {
    private lateinit var binding: FragmentMyfitBinding
    private lateinit var controlActivityInterface: ControlActivityInterface

    private val fitGroupProgressAdapter: MyFitGroupProgressAdapter by lazy { MyFitGroupProgressAdapter() }
    private lateinit var fitHistoryAdapter: MyFitHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyfitBinding.inflate(layoutInflater)
        controlActivityInterface = activity as MainActivity
        controlActivityInterface.viewNavigationBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //툴바 버튼 클릭 리스너 초기화
        initButtonClickListener()

        //그룹 fitProgress 리사이클러뷰 mock 데이터 연결
        initMockData()

        //운동 기록 리사이클러뷰 mock 데이터 연결
        initMockData2()

        // 캘린더 연결
        initCalendar()

    }

    private fun initCalendar() {
        binding.recyclerViewCalendar.run {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = MonthListAdapter()
            scrollToPosition(Int.MAX_VALUE / 2)
            PagerSnapHelper().attachToRecyclerView(this)
        }
    }

    private fun initMockData2() {
        val mockData = listOf<FitHistory>(
            FitHistory(
                fitTime = "01:22:44",
                groupList = arrayListOf("축구를 좋아하는 사람들", "3대600", "다이어트 하는 사람들")
            ),
            FitHistory(
                fitTime = "01:33:24",
                groupList = arrayListOf("축구를 좋아하는 사람들", "3대600", "다이어트 하는 사람들")
            )
        )
        fitHistoryAdapter = MyFitHistoryAdapter(requireContext())
        fitHistoryAdapter.submitList(mockData){
            binding.recyclerViewMyFitFragmentFitHistory.adapter = fitHistoryAdapter
        }

    }

    private fun initButtonClickListener() {

        binding.buttonFragmentMyFitFitOff.setOnClickListener {
            findNavController().navigate(R.id.action_myFitFragment_to_myFitOffFragment)
        }
        binding.buttonFragmentMyFitRecord.setOnClickListener {
            findNavController().navigate(R.id.action_myFitFragment_to_certificateFragment)
        }
    }

    private fun initMockData() {
        val mockData = listOf<MyFitGroupProgress>(
            MyFitGroupProgress(
                title = "축구를 좋아하는 사람들",
                maxProgress = 4,
                nowProgress = 2
            ), MyFitGroupProgress(title = "축구를 좋아하는 사람들", maxProgress = 7, nowProgress = 6)
        )
        fitGroupProgressAdapter.submitList(mockData) {
            if (mockData.isNotEmpty()) {
                binding.containerFragmentMyFitNotHasFitGroup.visibility = View.GONE
            }else{
                binding.containerFragmentMyFitNotHasFitGroup.visibility = View.VISIBLE
            }
            binding.recyclerviewMyFitFragmentMyFitProgress.adapter = fitGroupProgressAdapter
        }
    }
}