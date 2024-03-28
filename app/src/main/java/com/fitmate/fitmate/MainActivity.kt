package com.fitmate.fitmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.databinding.ActivityMainBinding
import com.fitmate.fitmate.util.ControlActivityInterface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ControlActivityInterface {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    //온보딩으로 가기위한 임시 데이터
    companion object {
        var isStarted = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //바인딩 및 네비게이션 컨트롤러 초기화 작업
        initSetting()
        setContentView(binding.root)

        //온보딩으로 이동.
        if (isStarted) {
            navController.navigate(R.id.action_homeFragment_to_onboardingContainerFragment)
        }

        //바텀 navigation 설정
        setNavigation()
    }

    private fun initSetting() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerViewMain) as NavHostFragment
        navController = navHostFragment.navController
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
}