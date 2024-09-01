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
import com.fitmate.fitmate.util.PendingTokenValue
import com.fitmate.fitmate.util.customGetSerializable
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginWebViewFragment : Fragment(R.layout.fragment_login_webview) {

    private lateinit var binding: FragmentLoginWebviewBinding
    private var loginUrl: String? = null
    private var pendingTokenValue: PendingTokenValue? = null
    private val viewModel: LoginViewModel by viewModels()
    private val TAG = "LoginWebViewFragment"
    private val defaultProfile = "https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=a4b124d6-0ba1-4585-a259-61d13c608b07"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loginUrl = it.getString("loginUrl")
            pendingTokenValue = it.customGetSerializable<PendingTokenValue>("pendingToken")
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
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest,
            ): Boolean {
                val url = request.url.toString()
                val uri = Uri.parse(url)
                var platform = ""
                if (uri.getQueryParameter("code") != null) {
                    val code = uri.getQueryParameter("code") ?: ""
                    platform = when (code.length) {
                        86 -> "kakao"
                        18 -> "naver"
                        else -> ""
                    }
                    if (platform.isNotEmpty()) {
                        FirebaseMessaging.getInstance().token.addOnCompleteListener(
                            OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                    return@OnCompleteListener
                                }

                                // Get new FCM registration token
                                val token = task.result
                                viewModel.login(code, token, defaultProfile, platform)
                            })
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
                val accessToken = loginResponse.accessToken
                val refreshToken = loginResponse.refreshToken
                val userId = loginResponse.userId
                val newUser = loginResponse.isNewUser
                val createdAt = loginResponse.createdAt
                val platform = viewModel.platform   // ViewModel에 저장된 플랫폼 정보 가져오기
                Log.d(TAG, "[access]$accessToken \n[refresh]$refreshToken \n[userId]$userId \n[createdAt]$createdAt \n[platform]$platform\n[newUser]$newUser")
                (activity as MainActivity).saveUserPreference(accessToken, refreshToken, userId, platform!!, createdAt, false)

                if(newUser == 0) {
                    val bundle = Bundle().apply {
                        putSerializable("pendingToken", pendingTokenValue)
                    }
                    findNavController().navigate(R.id.action_loginWebViewFragment_to_homeMainFragment, bundle)
                    Snackbar.make(binding.root, "로그인을 성공했습니다. [USERID ${userId}]", Snackbar.LENGTH_SHORT).show()
                } else if(newUser == 1) {
                    val bundle = Bundle()
                    bundle.putString("authorizationCode",loginResponse.accessToken)
                    findNavController().navigate(R.id.action_loginWebViewFragment_to_nicknameFragment, bundle)
                    Snackbar.make(binding.root, "회원가입을 성공했습니다. [USERID ${userId}]", Snackbar.LENGTH_SHORT).show()
                }
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