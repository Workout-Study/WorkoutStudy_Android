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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.customGetSerializable<PointType>("pointOwnerType")?.let {
            pointOwnerType = it
        }
        adapter = PointHistoryAdapter()
        val userPreference = (activity as MainActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
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
        viewModel.getPagingPointHistory(723,"GROUP","2024-07-01T00:00:00Z","2024-07-31T23:59:59Z",0,5,"DEPOSIT")

        viewModel.pointInfo.observe(viewLifecycleOwner){
            Log.d("testt",it.toString())
        }
        observePointHistory()

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



