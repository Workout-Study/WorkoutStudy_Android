package com.fitmate.fitmate.util

import android.view.View

interface ControlActivityInterface {
    fun goneNavigationBar()
    fun viewNavigationBar()
    fun hideKeyboard()
    fun showKeyboard(view: View)
    fun saveUserPreference(accessToken: String, refreshToken: String, userId: Int, createdAt: String, platform: String, isLogout: Boolean)
    fun loadUserPreference(): List<Any>
    fun killUserPreference()
}