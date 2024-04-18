package com.fitmate.fitmate

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.databinding.ActivityMainBinding
import com.fitmate.fitmate.presentation.viewmodel.MainActivityViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ControlActivityInterface {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainActivityViewModel by viewModels()

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
        splash(splashScreen)
        //바인딩 및 네비게이션 컨트롤러 초기화 작업
        initSetting()
        setContentView(binding.root)
        //바텀 navigation 설정
        setNavigation()
        //온보딩 조회 결과 구독
        observeOnboardingState()
        //온보딩 조회 여부 확인
        viewModel.loadOnBoardingStateInPref()


        if (intent.getStringExtra("navigateTo") == "certificateFragment") navController.navigate(R.id.certificateFragment)

    }

    private fun observeOnboardingState() {
        viewModel.onboardingInquiryStatus.observe(this) { isTrue ->
            if (isTrue) {
                //TODO 엑세스 토큰 확인하는 작업 수행하고 이후 넘어오는 값에 따라 로그인처리핧지 로그인 화면으로 넘길지 결정
                //지금 현재 homeFragment
            } else {
                navController.navigate(R.id.action_homeFragment_to_onboardingContainerFragment)
            }
        }
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
}