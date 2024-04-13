package com.fitmate.fitmate.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentHomeCategoryBinding
import com.fitmate.fitmate.domain.model.CategoryItem
import com.fitmate.fitmate.presentation.ui.home.list.adapter.CategoryAdapter
import com.fitmate.fitmate.presentation.viewmodel.GroupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeCategoryFragment: Fragment(R.layout.fragment_home_category) {

    private lateinit var binding: FragmentHomeCategoryBinding
    private val viewModel: GroupViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeCategoryBinding.bind(view)

        setupRecyclerView()
        observeModel()
        getFitGroups()
    }

    private fun getFitGroups() {
        viewModel.getFitGroups(true, 0, 1, 10)
        getChipFitGroups()
    }

    private fun getChipFitGroups() {
//            binding.chipGroupCategory.setOnCheckedStateChangeListener { _, checkedIds ->
//            /* TODO 해당 과정에서 네트워크 상 충돌이 되지 않는 부분에 대해 연구해야 함.*/
//            if (checkedIds.isEmpty()) {
//                /* TODO 전체 피트그룹을 보여주는 코드 */
//                viewModel.getFitGroups(true, 0, 1, 10)
//            } else {
//                when(checkedIds[0]) {
//                    R.id.chipCategory1 -> { viewModel.getFitGroups(false, 1, 1, 5) }
//                    R.id.chipCategory2 -> { viewModel.getFitGroups(false, 2, 1, 5) }
//                    R.id.chipCategory3 -> { viewModel.getFitGroups(false, 3, 1, 5) }
//                    R.id.chipCategory4 -> { viewModel.getFitGroups(false, 4, 1, 5) }
//                    R.id.chipCategory5 -> { viewModel.getFitGroups(false, 5, 1, 5) }
//                    R.id.chipCategory6 -> { viewModel.getFitGroups(false, 6, 1, 5) }
//                    R.id.chipCategory7 -> { viewModel.getFitGroups(false, 7, 1, 5) }
//                    R.id.chipCategory8 -> { viewModel.getFitGroups(false, 8, 1, 5) }
//                    R.id.chipCategory9 -> { viewModel.getFitGroups(false, 9, 1, 5) }
//                }
//            }
//        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCategory.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCategory.adapter = CategoryAdapter(this) {}
    }

    private fun observeModel() {
        lifecycleScope.launch {
            binding.categoryShimmer.startShimmer()
            binding.categoryShimmer.visibility = View.VISIBLE
            binding.recyclerViewCategory.visibility = View.GONE

            viewModel.fitGroups.observe(viewLifecycleOwner) { fitGroups ->
                binding.categoryShimmer.stopShimmer()
                binding.categoryShimmer.visibility = View.GONE
                binding.recyclerViewCategory.visibility = View.VISIBLE

                val categoryItems = fitGroups.content.map {
                    CategoryItem(
                        title = it.fitGroupName,
                        fitCount = "${it.frequency}회 / 1주",
                        peopleCount = "${it.presentFitMateCount} / ${it.maxFitMate}",
                        comment = it.introduction,
                        fitGroupId = it.fitGroupId
                    )
                }
                (binding.recyclerViewCategory.adapter as CategoryAdapter).submitList(categoryItems)
            }
        }
    }
}