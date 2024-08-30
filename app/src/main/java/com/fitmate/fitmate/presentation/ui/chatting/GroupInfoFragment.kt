package com.fitmate.fitmate.presentation.ui.chatting

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.data.model.dto.GetFitGroupDetail
import com.fitmate.fitmate.databinding.FragmentGroupInfoBinding
import com.fitmate.fitmate.presentation.viewmodel.UpdateGroupViewModel
import com.fitmate.fitmate.util.customGetSerializable
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupInfoFragment : Fragment(R.layout.fragment_group_info) {

    private lateinit var binding: FragmentGroupInfoBinding
    private val viewModel: UpdateGroupViewModel by viewModels()
    private var groupInfoData: GetFitGroupDetail? = null
    private var userId: Int = -1
    private var updateState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupInfoData = arguments?.customGetSerializable<GetFitGroupDetail>("groupDetailData")
        if (groupInfoData == null) {
            Toast.makeText(requireContext(), "통신 오류로 화면을 전환합니다", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)//초기 설정

        loadUserPreferenceAndSetting()//내 아이디 가져오고 수정 버튼 visible 설정
    }

    private fun initView(view: View) {
        binding = FragmentGroupInfoBinding.bind(view)
        binding.viewModel = viewModel
        binding.materialToolbar.setupWithNavController(findNavController())
        viewModel.bindData(groupInfoData!!)

        binding.seekBarMakeGroupFitNum.value = groupInfoData!!.frequency.toFloat()
        binding.seekBarMakeGroupPeople.value = groupInfoData!!.maxFitMate.toFloat()

        viewModel.groupFitCycle.observe(viewLifecycleOwner) {
            binding.textViewMakeGroupFitNum.text =  requireContext().getString(R.string.make_group_scr_fit_num, it)
        }
        viewModel.groupFitMateLimit.observe(viewLifecycleOwner) {
            binding.textViewMakeGroupPeople.text =  requireContext().getString(R.string.make_group_scr_people, it)
        }

        binding.seekBarMakeGroupFitNum.addOnSliderTouchListener(object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}
            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.setGroupFitCycle(slider.value.toInt())
            }
        })

        binding.seekBarMakeGroupPeople.addOnSliderTouchListener(object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}
            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.setGroupFitMateLimit(slider.value.toInt())
            }
        })


        contentDisable()
    }


    private fun loadUserPreferenceAndSetting() {
        val userPreference = (activity as MainActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1

        if (userId.toString() == groupInfoData?.fitLeaderUserId) {
            binding.imageViewUpdateGroupInfo.visibility = View.VISIBLE
            binding.imageViewUpdateGroupInfo.setOnClickListener {
                if (updateState) return@setOnClickListener
                contentEnable()
            }
        } else {
            binding.imageViewUpdateGroupInfo.visibility = View.GONE
        }
    }

    private fun contentEnable() {
        updateState = true
        binding.apply {
            editTextSetGroupName.isEnabled = true //이름

            editTextCategorySelect.isEnabled = true //카테고리
            (editTextCategorySelect as? MaterialAutoCompleteTextView)?.setSimpleItems(R.array.group_category)

            seekBarMakeGroupFitNum.isEnabled = true //빈도

            seekBarMakeGroupPeople.isEnabled = true //최대 인원

            editTextSetGroupComment.isEnabled = true //코멘트

            cardViewItemCertificateStart.visibility = View.VISIBLE //카드뷰

            buttonMakeGroupConfirm.visibility = View.VISIBLE //수정 완료 버튼
        }
    }

    private fun contentDisable() {
        updateState = false
        binding.apply {
            editTextSetGroupName.isEnabled = false //이름

            editTextCategorySelect.isEnabled = false //카테고리


            seekBarMakeGroupFitNum.isEnabled = false //빈도

            seekBarMakeGroupPeople.isEnabled = false //최대 인원

            editTextSetGroupComment.isEnabled = false //코멘트

            cardViewItemCertificateStart.visibility = View.GONE //카드뷰

            buttonMakeGroupConfirm.visibility = View.GONE //수정 완료 버튼
        }
    }

}