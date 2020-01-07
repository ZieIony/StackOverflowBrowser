package com.github.zieiony.stackoverflowbrowser

import android.view.ViewGroup
import carbon.widget.Snackbar
import com.github.zieiony.base.app.BaseFragment
import com.github.zieiony.base.app.Navigator

open class StackOverflowFragment(parentNavigator: Navigator) : BaseFragment(parentNavigator) {

    protected val stackOverflowApplication
        get() = context?.applicationContext as StackOverflowApplication?

    protected open fun showError(exception: Throwable) {
        val snackbar = Snackbar(context, exception.message ?: "Error", ERROR_DURATION)
        snackbar.style = Snackbar.Style.Docked
        snackbar.show(view as ViewGroup)
    }

    companion object {
        const val ERROR_DURATION = 3000
    }
}
