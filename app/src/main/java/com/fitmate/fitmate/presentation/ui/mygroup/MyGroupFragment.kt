package com.fitmate.fitmate.presentation.ui.mygroup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.ChatActivity
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentMyGroupBinding
import com.fitmate.fitmate.util.ControlActivityInterface
import com.fitmate.fitmate.domain.model.FitGroup
import com.fitmate.fitmate.presentation.ui.mygroup.list.adapter.MyFitGroupAdapter
import com.fitmate.fitmate.presentation.viewmodel.ChattingViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyGroupFragment: Fragment(R.layout.fragment_my_group) {

    private lateinit var binding: FragmentMyGroupBinding
    private lateinit var adapter: MyFitGroupAdapter
    private lateinit var controlActivityInterface: ControlActivityInterface
    private val viewModel: ChattingViewModel by viewModels()
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreference = (activity as MainActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
        viewModel.retrieveFitGroup(userId.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyGroupBinding.inflate(layoutInflater)
        binding.fragment = this
        controlActivityInterface = activity as MainActivity
        controlActivityInterface.viewNavigationBar()

        adapter = MyFitGroupAdapter { fitGroup ->
            val intent = Intent(requireContext(), ChatActivity::class.java).apply {
                putExtra("fitGroupId", fitGroup.fitGroupId)
                putExtra("userId", fitGroup.fitMateId)
                Log.d("woojugoing", (fitGroup.fitGroupId).toString() + (fitGroup.fitMateId).toString())
            }
            startActivity(intent)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingButtonMakeFitGroup.setOnClickListener { findNavController().navigate(R.id.makeGroupFragment) }
        observeFitGroupData()
        observeLoadingState()
    }

    private fun observeFitGroupData() {
        lifecycleScope.launch {
            viewModel.fitGroup.collectLatest { networkFitGroups ->
                val uiModelList = mapNetworkDataToUiModel(networkFitGroups)
                updateListAdapter(uiModelList)
                updateEmptyViewVisibility(uiModelList)
            }

            viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                if(!errorMessage.isNullOrEmpty()) {
                    Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).show()
                    viewModel.clearErrorMessage()
                }
            }
        }
    }

    private fun observeLoadingState() {
        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                binding.loadingIndicatorMyGroup.visibility = if(!isLoading) View.GONE else View.VISIBLE
            }
        }
    }

    private fun updateListAdapter(fitGroups: List<FitGroup>) {
        adapter.submitList(fitGroups)
        binding.textViewMyFitGroupEmpty.visibility = if(fitGroups.isNotEmpty()) View.GONE else View.VISIBLE
        binding.recyclerViewMyFitGroupList.adapter = adapter
    }

    private fun mapNetworkDataToUiModel(networkFitGroups: List<com.fitmate.fitmate.data.model.dto.RetrieveFitGroup>): List<FitGroup> {
        return networkFitGroups.map { networkGroup ->
            FitGroup(
                networkGroup.fitGroupName,
                "${networkGroup.maxFitMate}명",
                networkGroup.createdAt,
                userId,
                networkGroup.id
            )
        }
    }

    private fun groupDateFormat(createdAt: String): String {
        return "Since ${createdAt.substring(0, 10).replace("-", ".")}"
    }

    private fun updateEmptyViewVisibility(fitGroups: List<FitGroup>) {
        binding.textViewMyFitGroupEmpty.visibility = if (fitGroups.isEmpty()) View.VISIBLE else View.GONE
    }
}