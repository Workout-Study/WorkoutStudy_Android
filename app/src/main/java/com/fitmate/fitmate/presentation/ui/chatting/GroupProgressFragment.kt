package com.fitmate.fitmate.presentation.ui.chatting

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentProgressBinding
import com.fitmate.fitmate.domain.model.FitProgressItem
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.GroupProgressAdapter
import com.fitmate.fitmate.presentation.viewmodel.GroupViewModel
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupProgressFragment : Fragment(R.layout.fragment_progress) {

    private lateinit var binding: FragmentProgressBinding
    private val viewModel: VoteViewModel by viewModels()
    private lateinit var adapter: GroupProgressAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProgressBinding.bind(view)

        viewModel.getFitMateProgress()
        setupRecyclerView()
        setupObservers()

        binding.materialToolbarProgress.setupWithNavController(findNavController())
    }

    private fun setupRecyclerView() {
        adapter = GroupProgressAdapter {}
        binding.recyclerProgress.layoutManager = LinearLayoutManager(context)
        binding.recyclerProgress.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.fitMateProgress.observe(viewLifecycleOwner) { fitMateProgress ->
            val newProgress = fitMateProgress.fitCertificationProgresses.mapIndexed { index, progress ->
                FitProgressItem(
                    itemId = index + 1,
                    itemName = progress.fitMateUserId,
                    thumbnailEndPoint = "",
                    cycle = fitMateProgress.cycle,
                    frequency = fitMateProgress.frequency,
                    certificationCount = progress.certificationCount
                )
            }
            Log.d("woojugoing_progress", newProgress.size.toString())
            adapter.submitList(newProgress)
        }

        viewModel.getFitMateProgress()
    }
}
