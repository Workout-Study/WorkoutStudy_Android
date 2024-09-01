package com.fitmate.fitmate

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.fitmate.fitmate.databinding.ActivityMainBinding
import com.fitmate.fitmate.presentation.ui.login.LoginFragment
import com.fitmate.fitmate.presentation.viewmodel.MainActivityViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import com.fitmate.fitmate.util.PendingTokenValue
import com.fitmate.fitmate.util.customGetSerializable
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ControlActivityInterface {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var pendingToken: PendingTokenValue? = null
    private val viewModel: MainActivityViewModel by viewModels()
    private val sharedPreferences: SharedPreferences by lazy {
        val masterKeyAlies = MasterKey
            .Builder(applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            applicationContext,
            FILE_NAME,
            masterKeyAlies,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    //1. 온보딩 조회 여부
    //무조건 로그인 창으로(null이면)
    //2. 액세스 토큰 확인(data store)
    //아예 null인경우 로그인 창으로
    //존재한다면 서버로 넘김
    //넘겨받은 결과에 따라 유효하면 메인 프래그먼트로
    //유효하지 않으면 로그인 창으로
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splash(splashScreen)
        }

        //바인딩 및 네비게이션 컨트롤러 초기화 작업
        initSetting()
        setContentView(binding.root)
        //바텀 navigation 설정
        setNavigation()
        //온보딩 조회 결과 구독
        observeOnboardingState()
        //온보딩 조회 여부 확인
        viewModel.loadOnBoardingStateInPref()
        //펜딩 인텐트로 날아오는 토킅 값 가져오기

        pendingToken = intent.customGetSerializable("pendingToken", PendingTokenValue::class.java)
    }


    //TODO 엑세스 토큰 확인하는 작업 수행하고 이후 넘어오는 값에 따라 로그인처리핧지 로그인 화면으로 넘길지 결정
    //지금 현재 homeFragment
    private fun observeOnboardingState() {
        viewModel.onboardingInquiryStatus.observe(this) { isTrue ->
            if (isTrue) {
                val userPreference = loadUserPreference()
                if (userPreference.size > 3) {
                    val platform = userPreference[3]
                    val isLogout = userPreference[5] as Boolean
                    when(platform) {
                        "naver" -> if(!isLogout) showWebViewFragment("naver", pendingToken) else navController.navigate(R.id.action_homeFragment_to_loginFragment)
                        "kakao" -> if(!isLogout) showWebViewFragment("kakao", pendingToken) else navController.navigate(R.id.action_homeFragment_to_loginFragment)
                        else -> navController.navigate(R.id.action_homeFragment_to_onboardingContainerFragment)
                    }
                } else {
                    navController.navigate(R.id.action_homeFragment_to_onboardingContainerFragment)
                }
            } else {
                navController.navigate(R.id.action_homeFragment_to_onboardingContainerFragment)
            }
        }
    }

//    private fun showWebViewFragment(loginUrl: String, pendingToken:PendingTokenValue?) {
//        val bundle = Bundle().apply {
//            putString("loginUrl", loginUrl)
//            putSerializable("pendingToken", pendingToken)
//        }
//        navController.navigate(R.id.loginWebViewFragment, bundle)
//    }

    private fun showWebViewFragment(platform: String, pendingToken:PendingTokenValue?) {
        val state = UUID.randomUUID().toString()
        val redirectUrl = "https://fitmate.com/oauth"
        val naverLoginUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${LoginFragment.naverClientId}&redirect_uri=$packageName&state=$state"
        val kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?client_id=${LoginFragment.kakaoRestAPIKey}&redirect_uri=${redirectUrl}&response_type=code&state=$state"
        var loginUrl = if(platform.equals("naver")) naverLoginUrl else kakaoLoginUrl
        val bundle = Bundle().apply {
            putString("loginUrl", loginUrl)
            putSerializable("pendingToken", pendingToken)
        }
        navController.navigate(R.id.loginWebViewFragment, bundle)
    }

    private fun initSetting() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerViewMain) as NavHostFragment
        navController = navHostFragment.navController
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun splash(splashScreen: androidx.core.splashscreen.SplashScreen) {
        splashScreen.setOnExitAnimationListener {
            val objectAnnotation = ObjectAnimator.ofPropertyValuesHolder()
            objectAnnotation.duration = 2000
            objectAnnotation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    it.remove()
                }
            })
            objectAnnotation.start()
        }
    }

    private fun setNavigation() {
        binding.bottomNavigationViewMainActivity.setupWithNavController(navController)
    }

    override fun goneNavigationBar() {
        binding.bottomNavigationViewMainActivity.visibility = View.GONE
    }

    override fun viewNavigationBar() {
        binding.bottomNavigationViewMainActivity.visibility = View.VISIBLE
    }

    override fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, 0)
    }

    override fun saveUserPreference(accessToken: String, refreshToken: String, userId: Int, platform: String, createdAt: String, isLogout: Boolean) {
        val editor = sharedPreferences.edit()
        editor.run {
            putString(KEY_ACCESS, accessToken)
            putString(KEY_REFRESH, refreshToken)
            putInt(KEY_USER_ID, userId)
            putString(KEY_PLATFORM, platform)
            putString(KEY_CREATED_AT, createdAt)
            putBoolean(KEY_IS_LOGOUT, isLogout)
            apply()
        }
    }

    override fun loadUserPreference(): List<Any> {
        val userPreferences = mutableListOf<Any>()
        if (sharedPreferences.contains(KEY_ACCESS)) {
            val accessToken = sharedPreferences.getString(KEY_ACCESS, "")
            val refreshToken = sharedPreferences.getString(KEY_REFRESH, "")
            val userId = sharedPreferences.getInt(KEY_USER_ID, -1)
            val platform = sharedPreferences.getString(KEY_PLATFORM, "")
            val createdAt = sharedPreferences.getString(KEY_CREATED_AT, "")
            val isLogout = sharedPreferences.getBoolean(KEY_IS_LOGOUT, false)

            userPreferences.run {
                add(accessToken!!)
                add(refreshToken!!)
                add(userId)
                add(platform!!)
                add(createdAt!!)
                add(isLogout!!)
            }
        }

        return userPreferences
    }

    override fun killUserPreference() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val FILE_NAME= "user_preference"
        private const val KEY_ACCESS = "access_token"
        private const val KEY_REFRESH = "refresh_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_PLATFORM = "platform"
        private const val KEY_CREATED_AT = "createdAt"
        private const val KEY_IS_LOGOUT = "isLogout"
    }
}