package com.fitmate.fitmate.presentation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.R
import com.fitmate.fitmate.data.model.dto.GroupDetailResponse
import com.fitmate.fitmate.databinding.FragmentGroupJoinBinding
import com.fitmate.fitmate.presentation.ui.home.list.adapter.ImageSliderAdapter
import com.fitmate.fitmate.presentation.viewmodel.GroupViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupJoinFragment: Fragment(R.layout.fragment_group_join) {

    private lateinit var binding: FragmentGroupJoinBinding
    private val viewModel: GroupViewModel by viewModels()
    private var groupId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments?.getInt("groupId") ?: -1
        if (groupId != -1) {
            Log.d("woojugoing_group_id", groupId.toString())
        } else {
            Log.d("woojugoing_group_id", groupId.toString())
            Toast.makeText(context, "Error: Group not found!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroupJoinBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        (activity as ControlActivityInterface).goneNavigationBar()
        binding.toolbarGroupJoin.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.buttonJoinGroupConfirm.setOnClickListener { findNavController().navigate(R.id.action_groupJoinFragment_to_homeFragment) }

        viewModel.groupDetail(groupId)
        viewModel.groupDetail.observe(viewLifecycleOwner) { groupDetail ->
            updateUI(groupDetail)
            setupImageSlider(groupDetail.multiMediaEndPoints)
        }
    }

    private fun updateUI(groupDetail: GroupDetailResponse) {
        val adapter = ImageSliderAdapter(requireContext(), groupDetail.multiMediaEndPoints)
        binding.imageViewItemDayCurrentDate.adapter = adapter
        binding.run {
            toolbarGroupJoin.title = groupDetail.fitGroupName
            textViewGroupDetailName.text = groupDetail.fitGroupName
            textViewGroupDetailBottomNow.text = " 현재 ${groupDetail.presentFitMateCount}명"
            textViewGroupDetailBottomCycle.text = " ${groupDetail.frequency}회 / 1주"
            textViewGroupDetailIntroduction.text = groupDetail.introduction
            textViewGroupDetailNow.text = "${groupDetail.presentFitMateCount}명"
            textViewGroupDetailMax.text = " / ${groupDetail.maxFitMate}명"
            textViewGroupDetailPresent.text = " 현재 ${groupDetail.presentFitMateCount}명"
            textViewGroupDetailCycle.text = " ${groupDetail.frequency}회 / 1주"
            chipGroupDetailCategory.text = when(groupDetail.category) {
                1 ->  "헬스"
                2 ->  "축구"
                3 ->  "농구"
                4 ->  "야구"
                5 ->  "배드민턴"
                6 ->  "필라테스"
                7 ->  "기타"
                else -> null
            }
        }
    }

    private fun setupImageSlider(images: List<String>) {
        val adapter = ImageSliderAdapter(requireContext(), images)
        binding.imageViewItemDayCurrentDate.adapter = adapter
        // Set initial value
        updateImageNum(0, images.size)
        binding.imageViewItemDayCurrentDate.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateImageNum(position, images.size)
            }
        })
    }

    private fun updateImageNum(currentPosition: Int, total: Int) {
        binding.textViewGroupDetailImageNum.text = "${currentPosition + 1} / $total"
    }
}