package com.fitmate.fitmate.presentation.ui.userinfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentLicenseBinding
import com.fitmate.fitmate.presentation.ui.userinfo.list.LicenseData
import com.fitmate.fitmate.presentation.ui.userinfo.list.adapter.LicenseAdapter
import com.fitmate.fitmate.util.ControlActivityInterface

class LicenseFragment: Fragment(R.layout.fragment_license) {

    private lateinit var binding: FragmentLicenseBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        setupRecyclerView()
        loadLibraries()
    }

    private fun initView(view: View) {
        binding = FragmentLicenseBinding.bind(view)
        (activity as ControlActivityInterface).goneNavigationBar()
        binding.toolbarLicense.setupWithNavController(findNavController())
    }

    private fun setupRecyclerView() {
        binding.recyclerViewLicense.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewLicense.adapter = LicenseAdapter()
        val divider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        binding.recyclerViewLicense.addItemDecoration(divider)
    }

    private fun loadLibraries() {
        val libraryList = LicenseData.libraryList
        (binding.recyclerViewLicense.adapter as LicenseAdapter).submitList(libraryList)
    }
}