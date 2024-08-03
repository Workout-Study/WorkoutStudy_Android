package com.fitmate.fitmate.presentation.ui.point

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentPointBinding
import com.fitmate.fitmate.domain.model.PointType
import com.fitmate.fitmate.presentation.ui.point.list.adapter.PointHistoryAdapter
import com.fitmate.fitmate.presentation.viewmodel.PointViewModel
import com.fitmate.fitmate.util.customGetSerializable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PointFragment : Fragment(R.layout.fragment_point) {
    private lateinit var binding:FragmentPointBinding
    private lateinit var pointOwnerType: PointType
    private lateinit var adapter: PointHistoryAdapter
    private val viewModel: PointViewModel by viewModels()
    private var pointOwnerId: Int = -1
    private lateinit var createAt: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //번들 값에 따라 pointOwnerId, pointOwnerType, createAt값 설정하는 메서드
        getArgumentAndSettingCreateAt()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPointBinding.bind(view)

        //어뎁터 초기화 및 설정
        adapter = PointHistoryAdapter()
        binding.recyclerViewPointHistory.adapter = adapter

        //통신 시작(포인트 정보 및 포인트 기록 데이터)
        viewModel.getPointInfo(pointOwnerId, pointOwnerType.value)
        viewModel.getPagingPointHistory(pointOwnerId, pointOwnerType.value,null,null,0,10,null)

        //포인트 기록 데이터 통신 감시
        observePointHistory()

    }
    private fun getArgumentAndSettingCreateAt() {
        arguments?.let {
            it.customGetSerializable<PointType>("pointOwnerType")?.let {
                pointOwnerType = it
            }
            when (pointOwnerType) {
                PointType.GROUP -> {
                    it.getString("createAt")?.let { createString ->
                        createAt = createString
                    }
                    it.getInt("groupId").let { groupId ->
                        pointOwnerId = groupId
                    }
                }

                PointType.USER -> {
                    val userPreference = (activity as MainActivity).loadUserPreference()
                    pointOwnerId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
                    createAt = userPreference[4].toString()
                }
            }
        }
    }
    
    private fun observePointHistory(){
        viewModel.run {
            viewModelScope.launch {
                pagingData.collectLatest {
                    if (it != null){
                        Log.d("tlqkf",it.toString())
                        adapter.submitData(lifecycle, it)
                    }
                }
            }
            pointInfo.observe(viewLifecycleOwner) {
                Log.d("tlqkf",it.toString())
            }
        }
    }
}



