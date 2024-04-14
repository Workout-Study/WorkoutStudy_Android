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
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentLoginWebviewBinding
import com.fitmate.fitmate.util.ControlActivityInterface

class LoginWebViewFragment : Fragment(R.layout.fragment_login_webview) {

    private lateinit var binding: FragmentLoginWebviewBinding
    private var loginUrl: String? = null

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
                    // activity?.supportFragmentManager?.popBackStack()
                    return when(code.length) {
                        86 -> {
                            Toast.makeText(context, "카카오를 통하여 로그인이 성공하였습니다.", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_loginWebViewFragment_to_homeFragment)
                            true
                        }

                        18 -> {
                            Toast.makeText(context, "네이버를 통하여 로그인이 성공하였습니다.", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_loginWebViewFragment_to_homeFragment)
                            true
                        }

                        else -> false
                    }
                }
                return false
            }
        }

        binding.webView.settings.javaScriptEnabled = true
        loginUrl?.let { binding.webView.loadUrl(it) }
    }
}
