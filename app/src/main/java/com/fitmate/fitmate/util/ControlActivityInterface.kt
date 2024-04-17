package com.fitmate.fitmate.util

import android.view.View

interface ControlActivityInterface {
    fun goneNavigationBar()
    fun viewNavigationBar()
    fun hideKeyboard()
    fun showKeyboard(view: View)
}