package com.fitmate.fitmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.databinding.ActivityMainBinding
import com.fitmate.fitmate.util.ControlActivityInterface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ControlActivityInterface {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //(온보딩 최초켰을 때 보여주기만 하면 되고 로그인은 토큰 )
        //앱을 킨적이 있는지 없는지 확인하는 작업
        //만약 없다면...->바텀 네비게이션 Gone, onBoarding화면 띄우기
        //권한 물어보는 화면
        //로그인 화면
        //로그인 완료시 백스택 전부 제거 후 바텀 네비게이션 Visible, Home화면 띄우기

        setContentView(binding.root)

        //바텀 navigation 설정
        setNavigation()
    }

    private fun setNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerViewMain) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationViewMainActivity.setupWithNavController(navController)
    }

    override fun goneNavigationBar() {
        binding.bottomNavigationViewMainActivity.visibility = View.GONE
    }

    override fun viewNavigationBar() {
        binding.bottomNavigationViewMainActivity.visibility = View.VISIBLE
    }
}