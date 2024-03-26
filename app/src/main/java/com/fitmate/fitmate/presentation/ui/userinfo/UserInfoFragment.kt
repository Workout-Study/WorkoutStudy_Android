package com.fitmate.fitmate.presentation.ui.userinfo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentUserInfoBinding
import com.fitmate.fitmate.util.ControlActivityInterface

class UserInfoFragment: Fragment(R.layout.fragment_user_info) {

    private lateinit var binding: FragmentUserInfoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserInfoBinding.bind(view)

        (activity as ControlActivityInterface).viewNavigationBar()
        setClickListener()
    }

    private fun setClickListener() { listOf(
            binding.textViewUserInfoContent1, binding.textViewUserInfoContent2,
            binding.textViewUserInfoContent3, binding.textViewUserInfoContent4,
            binding.textViewUserInfoContent5, binding.textViewUserInfoContent6
        ).forEach { textView -> textView.setOnClickListener { handleOnClick(textView.id) } }
    }

    private fun handleOnClick(viewId: Int) {
        when (viewId) {
            R.id.textViewUserInfoContent1 -> null
            R.id.textViewUserInfoContent2 -> navigateTo(R.id.action_userInfoFragment_to_myFitOffFragment)
            R.id.textViewUserInfoContent3 -> Toast.makeText(context, "지정된 공지사항이 없습니다.", Toast.LENGTH_SHORT).show()
            R.id.textViewUserInfoContent4 -> navigateTo(R.id.action_userInfoFragment_to_licenseFragment)
            R.id.textViewUserInfoContent5 -> navigateTo(R.id.action_userInfoFragment_to_loginFragment)
            R.id.textViewUserInfoContent6 -> navigateTo(R.id.action_userInfoFragment_to_loginFragment)
        }
    }

    private fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }
}