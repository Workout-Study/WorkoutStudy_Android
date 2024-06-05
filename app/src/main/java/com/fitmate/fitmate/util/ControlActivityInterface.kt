package com.fitmate.fitmate.util

import android.view.View

interface ControlActivityInterface {
    fun goneNavigationBar()
    fun viewNavigationBar()
    fun hideKeyboard()
    fun showKeyboard(view: View)

    fun saveUserPreference(accessToken: String, refreshToken: String, userId: Int, platform: String)

    fun loadUserPreference(): List<Any>
}