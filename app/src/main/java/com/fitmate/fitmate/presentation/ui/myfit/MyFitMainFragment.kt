package com.fitmate.fitmate.presentation.ui.myfit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.databinding.FragmentMyFitMainBinding
import com.fitmate.fitmate.databinding.FragmentMyfitBinding
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
        //내 그룹 통신 시작
        observeFitProgress()

    }

    private fun observeFitProgress() {
        observeNetworkMyProgress()
        viewModel.getMyFitProgress("567843")
    }

    private fun observeNetworkMyProgress() =
        viewModel.fitProgressItem.observe(viewLifecycleOwner) { fitProgressList ->
            if (fitProgressList.isEmpty()){
                binding.containerFitGroupItem.visibility = View.GONE
            }
            fitGroupProgressAdapter.submitList(fitProgressList) {
                binding.recyclerviewMyFitFragmentMyFitProgress.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                binding.recyclerviewMyFitFragmentMyFitProgress.adapter = fitGroupProgressAdapter
            }
            viewPagerScrollButtonVisible()
        }

    fun viewPagerScrollButtonVisible() {
        viewModel.fitProgressItem.value?.let { myFitGroupList ->
            binding.recyclerviewMyFitFragmentMyFitProgress.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    Log.d("testt","$position")
                    super.onPageSelected(position)
                    if (myFitGroupList.size <= 1){
                        binding.buttonCardViewScrollLeft.visibility = View.GONE
                        binding.buttonCardViewScrollRight.visibility = View.GONE
                    }else{
                        when{
                            position == 0 -> binding.buttonCardViewScrollLeft.visibility = View.GONE
                            position == myFitGroupList.size - 1 -> binding.buttonCardViewScrollRight.visibility = View.GONE
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

    fun clickButtonViewPagerScrollLeft(){
        viewModel.fitProgressItem.value?.let{ myFitGroupList->
            val viewPagerCurrentPosition = binding.recyclerviewMyFitFragmentMyFitProgress.currentItem
            if (viewPagerCurrentPosition != 0 ){
                binding.recyclerviewMyFitFragmentMyFitProgress.currentItem = viewPagerCurrentPosition - 1
            }
        }


    }

    fun clickButtonViewPagerScrollRight(){
        viewModel.fitProgressItem.value?.let{ myFitGroupList ->
            val viewPagerCurrentPosition = binding.recyclerviewMyFitFragmentMyFitProgress.currentItem
            if(viewPagerCurrentPosition != (myFitGroupList.size -1)){
                binding.recyclerviewMyFitFragmentMyFitProgress.currentItem = viewPagerCurrentPosition + 1
            }
        }

    }

}