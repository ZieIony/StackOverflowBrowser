package com.github.zieiony.stackoverflowbrowser

import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import carbon.widget.Snackbar
import com.github.zieiony.base.app.BaseFragment
import com.github.zieiony.base.app.Navigator
import com.github.zieiony.stackoverflowbrowser.di.ViewModelFactory

open class StackOverflowFragment(parentNavigator: Navigator) : BaseFragment(parentNavigator) {

    protected val stackOverflowApplication
        get() = context?.applicationContext as StackOverflowApplication?

    val isPhone
        get() = context!!.resources.getBoolean(R.bool.carbon_isPhone)

    private var snackbar: Snackbar? = null

    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stackOverflowApplication!!.component.inject(this)
        viewModelFactory = stackOverflowApplication!!.component.viewModelFactory.create(this)
    }

    fun <T : ViewModel> getViewModel(c: Class<T>) =
            ViewModelProviders.of(this, viewModelFactory).get(c)

    protected open fun showError(exception: Throwable) {
        val snackbar = Snackbar(context, exception.message ?: "Error", ERROR_DURATION)
        snackbar.style = Snackbar.Style.Docked
        snackbar.show(view as ViewGroup)
    }

    companion object {
        const val ERROR_DURATION = 3000
    }
}
