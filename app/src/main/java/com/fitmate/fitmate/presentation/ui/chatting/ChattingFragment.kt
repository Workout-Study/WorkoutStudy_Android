package com.fitmate.fitmate.presentation.ui.chatting

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentChattingBinding
import com.fitmate.fitmate.util.HeightProvider

class ChattingFragment : Fragment(R.layout.fragment_chatting) {

    private lateinit var binding: FragmentChattingBinding
    private lateinit var heightProvider: HeightProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChattingBinding.bind(view)
        binding.containerExtraFunction.layoutTransition = null
        binding.toolbarFragmentChatting.setupWithNavController(findNavController())

        setupClickListeners()
        initHeightProvider()
    }

    private fun setupClickListeners() {
        binding.imageViewChattingToolbarForDrawerLayout.setOnClickListener {
            toggleDrawer()
        }

        listOf(
            binding.buttonFragmentChattingFitMateProgress to R.id.action_chattingFragment_to_groupProgressFragment,
            binding.buttonFragmentChattingVote to R.id.action_chattingFragment_to_groupVoteFragment,
            binding.buttonFragmentChattingFine to R.id.action_chattingFragment_to_groupFineFragment,
            binding.buttonFragmentChattingFitOff to R.id.action_chattingFragment_to_groupFitOffFragment,
            binding.buttonFragmentChattingCertification to R.id.certificateFragment2
        ).forEach { pair ->
            pair.first.setOnClickListener {
                hideKeyboard()
                findNavController().navigate(pair.second)
            }
        }

        binding.buttonFragmentChattingTransfer.setOnClickListener {
            hideKeyboard()
            Toast.makeText(context, "Group 계좌가 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.imageViewChattingFragmentOpenContentList.setOnClickListener {
            toggleExtraFunctionContainer()
        }
    }

    private fun toggleDrawer() {
        if (binding.drawerLayoutForFragmentChatting.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayoutForFragmentChatting.closeDrawer(GravityCompat.END)
        } else {
            binding.drawerLayoutForFragmentChatting.openDrawer(GravityCompat.END)
        }
    }

    private fun toggleExtraFunctionContainer() {
        binding.containerExtraFunction.visibility = if (binding.containerExtraFunction.visibility == View.GONE) {
            View.VISIBLE
        } else {
            View.GONE
        }
        if (binding.containerExtraFunction.visibility == View.VISIBLE) hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun initHeightProvider() {
        activity?.let {
            heightProvider = HeightProvider(it).init().setHeightListener { height ->
                if (height > 0) {
                    binding.containerExtraFunction.visibility = View.GONE
                    binding.containerExtraFunction.layoutParams.height = height
                }
            }
        }
    }
}
