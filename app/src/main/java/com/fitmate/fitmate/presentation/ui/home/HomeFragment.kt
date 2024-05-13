package com.fitmate.fitmate.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    private val homeMainFragment = HomeMainFragment()
    private val homeCategoryFragment = HomeCategoryFragment()
    private val fragments = listOf(homeMainFragment, homeCategoryFragment)
    private var setViewPagerItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewPagerItem = arguments?.getInt("viewPagerPosition")?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        //바텀 네비게이션 설정
        (activity as MainActivity).viewNavigationBar()

        //뷰페이저2 및 탭 레이아웃 설정
        setupViewPagerAndTabs()
    }

    private fun setupViewPagerAndTabs() {
        val adapter = ViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, fragments)
        binding.pager2Main.adapter = adapter

        if (setViewPagerItem == 1) {
            binding.pager2Main.setCurrentItem(1, false)
        }

        //binding.pager2Main.isUserInputEnabled = false

        TabLayoutMediator(binding.tabs, binding.pager2Main) { tab, position ->
            tab.text = if (fragments[position] is HomeMainFragment) {
                "홈"
            } else {
                "카테고리"
            }
        }.attach()
    }

    inner class ViewPagerAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        private val fragments: List<Fragment>
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}