@file:JvmName("KeyboardUtil")

package com.github.zieiony.stackoverflowbrowser.ui

import android.app.Activity
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {

    fun hideKeyboard(view: View) {
        view.rootView.findFocus()?.let {
            val inputMethodManager = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun showKeyboard(view: View) {
        KeyboardRunnable(view).run()
    }
}

class KeyboardRunnable(var view: View) : Runnable {

    private var activity: Activity? = null

    init {
        var context = view.context
        while (context is ContextWrapper && context !is Activity)
            context = context.baseContext
        if (context is Activity)
            activity = context
    }

    override fun run() {
        if (activity == null)
            return

        val inputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        if (!(view.isFocusable && view.isFocusableInTouchMode)) {
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            return
        } else if (!view.requestFocus()) {
            post()
        } else if (!inputMethodManager.isActive(view)) {
            post()
        } else if (!inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)) {
            post()
        }

        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun post() {
        handler.postDelayed(this, INTERVAL_MS)
    }

    companion object {
        private val INTERVAL_MS = 100L
        private val handler = Handler(Looper.getMainLooper())
    }
}
