package com.github.zieiony.stackoverflowbrowser.navigation

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.Serializable

open class BaseFragment : Fragment(), NavigationParent {
    override fun navigate(fragmentClass: Class<out BaseFragment>, arguments: Map<String, Serializable>?) {
        var activity = BaseActivity::class.java
        val annotation = fragmentClass.getAnnotation(FragmentAnnotation::class.java)
        if (annotation != null)
            activity = annotation.activity.java as Class<BaseActivity>

        navigateToActivity(activity, fragmentClass, arguments)
    }

    private fun navigateToActivity(activityClass: Class<BaseActivity>, fragmentClass: Class<out BaseFragment>, arguments: Map<String, Serializable>?) {
        val intent = Intent(activity, activityClass)
        intent.putExtra(BaseActivity.FRAGMENT, fragmentClass)
        intent.putExtra(BaseActivity.ARGUMENTS, arguments as Serializable)
        startActivity(intent)
    }

    override fun getRoot(): NavigationParent = activity as NavigationParent

    override fun navigateBack() {
        activity.onBackPressed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val annotation = javaClass.getAnnotation(FragmentAnnotation::class.java) ?: return null
        return inflater.inflate(annotation.layout, container, false)
    }
}

