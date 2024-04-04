package com.fitmate.fitmate.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
): ViewModel() {
    private val _onboardingInquiryStatus = MutableLiveData<Boolean>()
    val onboardingInquiryStatus: LiveData<Boolean>
        get() = _onboardingInquiryStatus

    fun saveOnBoardingStateInPref() {
        sharedPreferences.edit().putBoolean("state", true).apply{
            _onboardingInquiryStatus.value = true
            apply()
        }
    }
}