package com.fitmate.fitmate.presentation.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.R
import com.fitmate.fitmate.data.model.dto.GetFitGroupDetail
import com.fitmate.fitmate.databinding.FragmentGroupJoinBinding
import com.fitmate.fitmate.presentation.ui.home.list.adapter.HomeViewPageAdapter
import com.fitmate.fitmate.presentation.viewmodel.GroupViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupJoinFragment: Fragment(R.layout.fragment_group_join) {

    private lateinit var binding: FragmentGroupJoinBinding
    private val TAG = "GroupJoinFragment"
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
        joinGroupConfirm()

        viewModel.getFitGroupDetail(groupId)
        viewModel.groupDetail.observe(viewLifecycleOwner) { groupDetail ->
            updateUI(groupDetail)
            setupImageSlider(groupDetail.multiMediaEndPoints)
        }

        observeErrorMessages()
    }

    private fun joinGroupConfirm() {
        binding.buttonJoinGroupConfirm.setOnClickListener {
            viewModel.registerFitMate(1, groupId) // requestUserId, fitGroupId
        }
    }

    private fun observeErrorMessages() {
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
                viewModel.clearErrorMessage() // 에러 메시지 초기화 메소드 추가 필요
            }
        }
    }


    private fun updateUI(groupDetail: GetFitGroupDetail) {
        binding.run {
            val adapter = HomeViewPageAdapter(requireContext(), groupDetail.multiMediaEndPoints)
            imageViewItemDayCurrentDate.adapter = adapter

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
                1 ->  "등산" 2 ->  "생활 체육" 3 ->  "웨이트" 4 ->  "수영" 5 ->  "축구" 6 ->  "농구" 7 ->  "야구" 8 -> "바이킹" 9 -> "클라이밍"
                else -> null
            }

            val rate = groupDetail.presentFitMateCount.toFloat() / groupDetail.maxFitMate.toFloat()
            val color = when {
                rate >= 0.8 -> R.color.red
                rate >= 0.4 -> R.color.turquoise
                else ->  R.color.dark_gray
            }
            textViewGroupDetailNow.setTextColor(ContextCompat.getColor(requireContext(), color))
        }
    }

    private fun setupImageSlider(images: List<String>) {
        val adapter = HomeViewPageAdapter(requireContext(), images)
        binding.imageViewItemDayCurrentDate.adapter = adapter
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