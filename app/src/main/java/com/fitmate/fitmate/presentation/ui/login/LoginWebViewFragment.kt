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
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentLoginWebviewBinding
import com.fitmate.fitmate.presentation.viewmodel.LoginViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginWebViewFragment : Fragment(R.layout.fragment_login_webview) {

    private lateinit var binding: FragmentLoginWebviewBinding
    private var loginUrl: String? = null
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loginUrl = it.getString("loginUrl")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginWebviewBinding.bind(view)
        (activity as? ControlActivityInterface)?.goneNavigationBar()
        getAuthorization()
        observeViewModel()
    }

    @Suppress("SetJavaScriptEnabled")
    private fun getAuthorization() {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()
                val uri = Uri.parse(url)
                if (uri.getQueryParameter("code") != null) {
                    val code = uri.getQueryParameter("code") ?: ""
                    val returnedState = uri.getQueryParameter("state") ?: ""
                    Log.d("auth_code", "Authorization Code: $code")
                    when(code.length) {
                        86 -> viewModel.login(code, "kakao")
                        18 -> viewModel.login(code,"naver")
                    }
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