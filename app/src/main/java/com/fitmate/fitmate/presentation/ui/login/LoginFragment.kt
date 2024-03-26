package com.fitmate.fitmate.presentation.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentLoginBinding
import com.fitmate.fitmate.util.ControlActivityInterface

class LoginFragment: Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        (activity as ControlActivityInterface).goneNavigationBar()
    }
}