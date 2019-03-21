package com.github.zieiony.stackoverflowbrowser

import com.github.zieiony.stackoverflowbrowser.api.StackOverflowAPI
import com.github.zieiony.stackoverflowbrowser.navigation.NavigationFragment
import javax.inject.Inject

abstract class BaseFragment : NavigationFragment() {

    @Inject
    lateinit var api: StackOverflowAPI

    override fun onStop() {
        super.onStop()
        api.cancelRequests()
    }

}