package com.fitmate.fitmate.presentation.ui.login

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentLoginWebviewBinding
import com.fitmate.fitmate.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginWebViewFragment : Fragment(R.layout.fragment_login_webview) {

    private lateinit var binding: FragmentLoginWebviewBinding
    private var loginUrl: String? = null
    private val viewModel: LoginViewModel by viewModels()
    private val TAG = "LoginWebViewFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loginUrl = it.getString("loginUrl")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginWebviewBinding.bind(view)
        (activity as MainActivity).goneNavigationBar()
        getAuthorization()
        observeViewModel()
    }

    @Suppress("SetJavaScriptEnabled")
    private fun getAuthorization() {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()
                val uri = Uri.parse(url)
                var platform = ""
                if (uri.getQueryParameter("code") != null) {
                    val code = uri.getQueryParameter("code") ?: ""
                    platform = when(code.length) {
                        86 -> "kakao"
                        18 -> "naver"
                        else -> ""
                    }
                    if (platform.isNotEmpty()) viewModel.login(code, "token is here", platform)
                    return true
                }
                return false
            }
        }

        binding.webView.settings.javaScriptEnabled = true
        loginUrl?.let { binding.webView.loadUrl(it) }
    }


    private fun observeViewModel() {
        viewModel.user.observe(viewLifecycleOwner) { loginResponse ->
            if (loginResponse != null) {
                val accessToken = loginResponse.accessToken
                val refreshToken = loginResponse.refreshToken
                val userId = loginResponse.userId
                val platform = viewModel.platform // ViewModel에 저장된 플랫폼 정보 가져오기
                Log.d(TAG, "[access]$accessToken \n[refresh]$refreshToken \n[userId]$userId \n[platform]$platform")
                (activity as MainActivity).saveUserPreference(accessToken, refreshToken, userId, platform!!)
                findNavController().navigate(R.id.action_loginWebViewFragment_to_homeMainFragment)
                Toast.makeText(context, "로그인이 성공하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }
}