package com.fitmate.fitmate.presentation.ui.category

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentCategoryBinding
import com.fitmate.fitmate.presentation.ui.category.list.adapter.CategoryAdapter
import com.fitmate.fitmate.presentation.viewmodel.GroupViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment: Fragment(R.layout.fragment_category) {

    private lateinit var binding: FragmentCategoryBinding
    private val viewModel: GroupViewModel by viewModels()
    private val TAG = "CategoryFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).viewNavigationBar()
        initView(view)
        observeModel()
        getChipFitGroups()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() activated")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() activated")
        //getAllFitGroups()
    }

    private fun initView(view: View) {
        binding = FragmentCategoryBinding.bind(view)
        binding.recyclerViewCategory.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCategory.adapter = CategoryAdapter(this) {}
    }

    private fun observeModel() {
        startShimmer()
        viewModel.run {
            viewModelScope.launch {
                pagingData.collectLatest {
                    if (it != null){
                        stopShimmer()
                        (binding.recyclerViewCategory.adapter as CategoryAdapter).submitData(it)
                    }
                }
            }


            errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                if(errorMessage != null) {
                    Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun getChipFitGroups() {
        val chipToCategoryMap = mapOf(
            R.id.chipCategory1 to 1, R.id.chipCategory2 to 2, R.id.chipCategory3 to 3,
            R.id.chipCategory4 to 4, R.id.chipCategory5 to 5, R.id.chipCategory6 to 6,
            R.id.chipCategory7 to 7, R.id.chipCategory8 to 8, R.id.chipCategory9 to 9
        )

        binding.chipGroupCategory.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) {
                Log.d(TAG, "connect in 0")
                viewModel.getGroups(true, 0)
            } else {
                val categoryId = chipToCategoryMap[checkedIds[0]] ?: return@setOnCheckedStateChangeListener
                Log.d(TAG, "connect in $categoryId")
                viewModel.getGroups(true, categoryId, 0)
            }
        }
    }


    private fun getAllFitGroups() {
        binding.chipGroupCategory.clearCheck()
        viewModel.getGroups(true,  0, 1000)
    }

    private fun startShimmer() {
        binding.categoryShimmer.startShimmer()
        binding.categoryShimmer.visibility = View.VISIBLE
        binding.recyclerViewCategory.visibility = View.GONE
    }

    private fun stopShimmer() {
        binding.categoryShimmer.stopShimmer()
        binding.categoryShimmer.visibility = View.GONE
        binding.recyclerViewCategory.visibility = View.VISIBLE
    }
}