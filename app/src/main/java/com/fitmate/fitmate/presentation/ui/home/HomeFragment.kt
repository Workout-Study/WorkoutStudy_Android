package com.fitmate.fitmate.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentHomeBinding
import com.fitmate.fitmate.util.ControlActivityInterface
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val tabName = arrayOf("홈", "카테고리")
    private val fragmentList = mutableListOf<Fragment>()
    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        (activity as ControlActivityInterface).viewNavigationBar()

        fragmentList.apply {
            if(isEmpty()) {
                add(HomeMainFragment())
                add(HomeCategoryFragment())
            }
        }

        activity?.let { fragmentActivity ->
            binding.pager2Main.isUserInputEnabled = false
            binding.pager2Main.adapter = TabAdapterClass(fragmentActivity)
            tabLayoutMediator?.detach()
            tabLayoutMediator = TabLayoutMediator(binding.tabs, binding.pager2Main) { tab, position -> tab.text = tabName[position] }.also { it.attach() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
    }

    inner class TabAdapterClass(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = fragmentList.size
        override fun createFragment(position: Int): Fragment = fragmentList[position]
    }
}