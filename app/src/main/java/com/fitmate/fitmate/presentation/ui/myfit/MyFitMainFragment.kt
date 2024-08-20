package com.fitmate.fitmate.presentation.ui.myfit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentMyFitMainBinding
import com.fitmate.fitmate.presentation.viewmodel.MyFitViewModel
import com.fitmate.fitmate.ui.myfit.list.adapter.MyFitGroupProgressAdapter
import com.fitmate.fitmate.util.ControlActivityInterface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyFitMainFragment: Fragment() {
    private lateinit var binding: FragmentMyFitMainBinding
    private lateinit var controlActivityInterface: ControlActivityInterface

    private val viewModel: MyFitViewModel by viewModels()

    private val fitGroupProgressAdapter: MyFitGroupProgressAdapter by lazy {
        MyFitGroupProgressAdapter(
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyFitMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.myFitMainFragment = this
        controlActivityInterface = activity as MainActivity
        controlActivityInterface.viewNavigationBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //내 그룹 통신 시작 및 데이터 감시
        networkFitProgressAndObserve()

    }

    private fun networkFitProgressAndObserve() {
        observeNetworkMyProgress()
        val userPreference = (activity as MainActivity).loadUserPreference()
        val userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
        viewModel.getMyFitProgress(userId.toString())
    }

    //통신 데이터 구독
    private fun observeNetworkMyProgress() =
        viewModel.fitProgressItem.observe(viewLifecycleOwner) { fitProgressList ->
            if (fitProgressList.isEmpty()){
                binding.containerFitGroupItem.visibility = View.GONE
            }else{
                binding.containerFitGroupItem.visibility = View.VISIBLE
            }
            fitGroupProgressAdapter.submitList(fitProgressList) {
                binding.recyclerviewMyFitFragmentMyFitProgress.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                binding.recyclerviewMyFitFragmentMyFitProgress.adapter = fitGroupProgressAdapter
            }
            viewPagerScrollButtonVisible()
        }

    //뷰페이저 스크롤 버튼 visible 설정
    private fun viewPagerScrollButtonVisible() {
        viewModel.fitProgressItem.value?.let { myFitGroupList ->
            binding.recyclerviewMyFitFragmentMyFitProgress.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (myFitGroupList.size <= 1){
                        binding.buttonCardViewScrollLeft.visibility = View.GONE
                        binding.buttonCardViewScrollRight.visibility = View.GONE
                    }else{
                        when{
                            position == 0 -> {
                                binding.buttonCardViewScrollLeft.visibility = View.GONE
                                binding.buttonCardViewScrollRight.visibility = View.VISIBLE
                            }
                            position == myFitGroupList.size - 1 -> {
                                binding.buttonCardViewScrollLeft.visibility = View.VISIBLE
                                binding.buttonCardViewScrollRight.visibility = View.GONE
                            }
                            else -> {
                                binding.buttonCardViewScrollLeft.visibility = View.VISIBLE
                                binding.buttonCardViewScrollRight.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            })
        }
    }
    //뷰 페이저 좌측 스크롤 버튼 설정
    fun clickButtonViewPagerScrollLeft(){
        viewModel.fitProgressItem.value?.let{ myFitGroupList->
            val viewPagerCurrentPosition = binding.recyclerviewMyFitFragmentMyFitProgress.currentItem
            if (viewPagerCurrentPosition != 0 ){
                binding.recyclerviewMyFitFragmentMyFitProgress.currentItem = viewPagerCurrentPosition - 1
            }
        }


    }
    //뷰 페이저 우측 스크롤 버튼 설정
    fun clickButtonViewPagerScrollRight(){
        viewModel.fitProgressItem.value?.let{ myFitGroupList ->
            val viewPagerCurrentPosition = binding.recyclerviewMyFitFragmentMyFitProgress.currentItem
            if(viewPagerCurrentPosition != (myFitGroupList.size -1)){
                binding.recyclerviewMyFitFragmentMyFitProgress.currentItem = viewPagerCurrentPosition + 1
            }
        }
    }

    //내 운동 기록 화면(캘린더 화면)으로 화면 이돟
    fun clickEnterMyFitHistoryFragment() {
        findNavController().navigate(R.id.action_myFitMainFragment_to_myFitFragment)
    }

    fun clickEnterCertificationFragment() {
        findNavController().navigate(R.id.action_myFitMainFragment_to_certificateFragment)
    }

}