package com.fitmate.fitmate.presentation.ui.point

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.databinding.FragmentPointBinding
import com.fitmate.fitmate.domain.model.PointType
import com.fitmate.fitmate.presentation.ui.category.list.adapter.CategoryAdapter
import com.fitmate.fitmate.presentation.ui.point.list.adapter.PointHistoryAdapter
import com.fitmate.fitmate.presentation.viewmodel.PointViewModel
import com.fitmate.fitmate.util.customGetSerializable
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PointFragment : Fragment() {
    private lateinit var binding:FragmentPointBinding
    private lateinit var pointOwnerType: PointType
    private lateinit var adapter: PointHistoryAdapter
    private val viewModel: PointViewModel by viewModels()
    private var userId: Int = -1
    private lateinit var createAt: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreference = (activity as MainActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1

        //번들 값에 따라 pointOwnerType 및 createAt값 설정하는 메서드
        getArgumentAndSettingCreateAt(userPreference)

        adapter = PointHistoryAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPointBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewPointHistory.adapter = adapter
        viewModel.getPointInfo(63,"USER")
        viewModel.getPagingPointHistory(723,"GROUP",null,null,0,5,null)

        viewModel.pointInfo.observe(viewLifecycleOwner){
            Log.d("testt",it.toString())
        }
        observePointHistory()

    }
    private fun getArgumentAndSettingCreateAt(userInfo:List<Any>) {
        arguments?.let {
            it.customGetSerializable<PointType>("pointOwnerType")?.let {
                pointOwnerType = it
            }
            when (pointOwnerType) {
                PointType.GROUP -> {
                    it.getString("createAt")?.let { createString ->
                        createAt = createString
                    }
                }

                PointType.USER -> {
                    createAt = userInfo[4].toString()
                }
            }
        }
    }


    private fun observePointHistory(){
        viewModel.run {
            viewModelScope.launch {
                pagingData.collectLatest {
                    if (it != null){
                        adapter.submitData(lifecycle, it)
                    }
                }
            }
        }
    }
}



