package com.fitmate.fitmate.presentation.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentNicknameBinding
import com.fitmate.fitmate.util.ControlActivityInterface

class NicknameFragment: Fragment(R.layout.fragment_nickname) {

    lateinit var binding: FragmentNicknameBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNicknameBinding.bind(view)
        (activity as? ControlActivityInterface)?.goneNavigationBar()

        binding.buttonNicknameConfirm.setOnClickListener {
            // TODO 닉네임 유효성 검사
            // TODO 유효성 검사 성공 시 서버에 닉네임 데이터 전송
            // TODO 전송 성공 시 홈 화면으로 이동
            (activity as? ControlActivityInterface)?.hideKeyboard()
            val nickname = binding.editTextNickname.text.toString()
            findNavController().navigate(R.id.homeFragment)
            Toast.makeText(context, "${nickname}님 환영합니다!", Toast.LENGTH_SHORT).show()
        }
    }
}