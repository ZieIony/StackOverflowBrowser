package com.github.zieiony.stackoverflowbrowser.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import java.io.Serializable

open class NavigationFragment : DaggerFragment(), NavigationParent {
    init {
        if (arguments == null)
            arguments = Bundle()
    }

    override fun navigate(fragmentClass: Class<out NavigationFragment>, arguments: Map<String, Serializable>?) {
        var activity = NavigationActivity::class.java
        val annotation = fragmentClass.getAnnotation(FragmentAnnotation::class.java)
        if (annotation != null)
            activity = annotation.activity.java as Class<NavigationActivity>

        navigateToActivity(activity, fragmentClass, arguments)
    }

    private fun navigateToActivity(activityClass: Class<NavigationActivity>, fragmentClass: Class<out NavigationFragment>, arguments: Map<String, Serializable>?) {
        val intent = Intent(activity, activityClass)
        intent.putExtra(NavigationActivity.FRAGMENT, fragmentClass)
        intent.putExtra(NavigationActivity.ARGUMENTS, arguments as Serializable)
        startActivity(intent)
    }

    override fun getRoot(): NavigationParent = activity as NavigationParent

    override fun navigateBack() {
        activity!!.onBackPressed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val annotation = javaClass.getAnnotation(FragmentAnnotation::class.java) ?: return null
        return inflater.inflate(annotation.layout, container, false)
    }
}

