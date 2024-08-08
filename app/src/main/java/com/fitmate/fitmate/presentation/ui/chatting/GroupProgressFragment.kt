package com.fitmate.fitmate.presentation.ui.chatting

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentProgressBinding
import com.fitmate.fitmate.domain.model.FitProgressItem
import com.fitmate.fitmate.domain.model.ItemFitMateProgressForAdapter
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.GroupProgressAdapter
import com.fitmate.fitmate.presentation.viewmodel.GroupViewModel
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupProgressFragment : Fragment(R.layout.fragment_progress) {

    private lateinit var binding: FragmentProgressBinding
    private val viewModel: VoteViewModel by viewModels()
    private lateinit var adapter: GroupProgressAdapter
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
        binding = FragmentProgressBinding.bind(view)

        viewModel.getFitMateProgress(groupId)
        setupRecyclerView()
        setupObservers()

        binding.materialToolbarProgress.setupWithNavController(findNavController())
    }

    private fun setupRecyclerView() {
        adapter = GroupProgressAdapter {}
        binding.recyclerProgress.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.fitMateProgress.observe(viewLifecycleOwner) { fitMateProgress ->
            val newProgress = fitMateProgress.fitCertificationProgresses.map { mateList ->
                ItemFitMateProgressForAdapter(
                    fitMateUserId = mateList.fitMateUserId,
                    frequency = fitMateProgress.frequency,
                    fitMateUserNickname = mateList.fitMateUserNickname,
                    certificationCount = mateList.certificationCount
                )
            }
            adapter.submitList(newProgress)
        }
    }
}
