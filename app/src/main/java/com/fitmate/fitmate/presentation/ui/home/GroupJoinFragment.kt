package com.fitmate.fitmate.presentation.ui.home

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.MainActivity
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
    private var userId: Int = -1

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

        val userPreference = (activity as MainActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1

        viewModel.getFitGroupDetail(groupId)
        viewModel.groupDetail.observe(viewLifecycleOwner) { groupDetail ->
            updateUI(groupDetail)
            setupImageSlider(groupDetail.multiMediaEndPoints)
        }

        observeMessages()

        setMotionLayoutForBottomInfo()
    }

    private fun joinGroupConfirm() {
        binding.buttonJoinGroupConfirm.setOnClickListener {
            viewModel.registerFitMate(userId, groupId) // requestUserId, fitGroupId

            // Handler를 사용하여 1초 후에 네비게이션 동작을 실행
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().popBackStack()
            }, 500)
        }
    }

    private fun observeMessages() {
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
        }

        viewModel.successMessage.observe(viewLifecycleOwner) { successMessage ->
            if (!successMessage.isNullOrEmpty()) {
                Snackbar.make(binding.root, successMessage, Snackbar.LENGTH_SHORT).show()
                viewModel.clearSuccessMessage()
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

    //모션 레이아웃 설정
    private fun setMotionLayoutForBottomInfo (){
        binding.scrollView.setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
            val textViewY = binding.textViewGroupDetailPresent.top
            // textViewY까지 스크롤한 퍼센트를 계산
            val percentage = (scrollY.toFloat() / textViewY)
            val compareHeight = binding.rootView.height - binding.scrollView.height

            if (compareHeight > textViewY) {
                binding.motionLayout.progress = if (percentage < 1.0) percentage else 1.0f
            }
        }
    }
}