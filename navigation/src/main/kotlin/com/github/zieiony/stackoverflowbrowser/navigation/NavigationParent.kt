package com.github.zieiony.stackoverflowbrowser.navigation

import java.io.Serializable

interface NavigationParent {
    fun getRoot(): NavigationParent
    fun navigate(fragmentClass: Class<out BaseFragment>, arguments: Map<String, Serializable>? = null)
    fun navigateBack()

    fun navigate(step: NavigationStep) {
        navigate(step.fragmentClass, step.arguments)
    }

}