package com.fitmate.fitmate.presentation.ui.login

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.BuildConfig
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentLoginBinding
import com.fitmate.fitmate.util.ControlActivityInterface
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    companion object {
        const val kakaoRestAPIKey = BuildConfig.KAKAO_REST_API_KEY
        const val naverClientId = BuildConfig.NAVER_CLIENT
    }

    private lateinit var binding: FragmentLoginBinding
    private var state = UUID.randomUUID().toString()
    private val redirectUrl = "https://fitmate.com/oauth"
    private val packageName = "com.fitmate.fitmate"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        (activity as? ControlActivityInterface)?.goneNavigationBar()
        setupLoginButtonListeners()
        setFinishWhenBackPressed()
    }

    private fun setupLoginButtonListeners() {
        naverLogin()
        kakaoLogin()
    }

    private fun naverLogin() {
        binding.buttonFragmentLoginNaver.setOnClickListener {
            showWebViewFragment("https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=$naverClientId&redirect_uri=$packageName&state=$state")
        }
    }

    private fun kakaoLogin() {
        binding.buttonFragmentLoginKakao.setOnClickListener {
            showWebViewFragment("https://kauth.kakao.com/oauth/authorize?client_id=$kakaoRestAPIKey&redirect_uri=${redirectUrl}&response_type=code&state=$state")
        }
    }

    private fun showWebViewFragment(loginUrl: String) {
        val bundle = Bundle().apply {
            putString("loginUrl", loginUrl)
        }
        findNavController().navigate(R.id.loginWebViewFragment, bundle)
    }

    private fun setFinishWhenBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object: OnBackPressedCallback(true
            ) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }
}
