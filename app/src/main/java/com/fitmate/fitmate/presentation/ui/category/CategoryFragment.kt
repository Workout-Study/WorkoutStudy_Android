package com.fitmate.fitmate.presentation.ui.category

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    private var categoryNumber = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryNumber = arguments?.getInt("categoryNumber") ?: 0
        if(categoryNumber == 1) categoryNumber = 0
        Log.d(TAG, categoryNumber.toString())
        (activity as MainActivity).goneNavigationBar()
        initView(view)
        observeModel()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() activated")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() activated")
        //getAllFitGroups()
        viewModel.getGroups(true, categoryNumber, 0)
    }

    private fun initView(view: View) {
        binding = FragmentCategoryBinding.bind(view)
        binding.recyclerViewCategory.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCategory.adapter = CategoryAdapter(this) {}


        binding.toolbar.title = when(categoryNumber) {
            0 -> "#전체"   2 -> "#생활체육"    3 -> "#웨이트"
            4 -> "#수영"   5 -> "#축구"       6 -> "#농구"
            7 -> "#야구"   8 -> "#바이크"      9 -> "#클라이밍"
            10 -> "#헬스"  11 -> "#골프"      12 -> "#등산"
            else -> ""
        }

        startShimmer()
    }

    private fun observeModel() {
        viewModel.run {
            viewModelScope.launch {
                pagingData.collectLatest {
                    if (it != null){
                        stopShimmer()
                        (binding.recyclerViewCategory.adapter as CategoryAdapter).submitData(lifecycle, it)
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