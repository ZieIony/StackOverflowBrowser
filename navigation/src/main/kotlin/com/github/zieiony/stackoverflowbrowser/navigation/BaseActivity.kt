package com.github.zieiony.stackoverflowbrowser.navigation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_base.*
import java.io.Serializable

open class BaseActivity : AppCompatActivity(), NavigationParent {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        if (savedInstanceState == null) {
            val fragment = intent.getSerializableExtra(FRAGMENT) as Class<BaseFragment>?
            val arguments = intent.getSerializableExtra(ARGUMENTS) as Map<String, Serializable>?
            if (fragment != null)
                navigate(fragment, arguments)
        }
    }

    override fun getRoot(): NavigationParent = this

    override fun navigate(fragmentClass: Class<out BaseFragment>, arguments: Map<String, Serializable>?) {
        var activityClass = BaseActivity::class.java
        val annotation = fragmentClass.getAnnotation(FragmentAnnotation::class.java)
        if (annotation != null)
            activityClass = annotation.activity.java as Class<BaseActivity>

        if (activityClass != javaClass) {
            navigateToActivity(activityClass, fragmentClass, arguments)
        } else {
            navigateToFragment(fragmentClass, arguments)
        }
    }

    private fun navigateToActivity(activity: Class<BaseActivity>, fragmentClass: Class<out BaseFragment>, arguments: Map<String, Serializable>?) {
        val intent = Intent(this, activity)
        intent.putExtra(FRAGMENT, fragmentClass)
        if (arguments != null)
            intent.putExtra(ARGUMENTS, arguments as Serializable)
        startActivity(intent)
    }

    private fun navigateToFragment(fragmentClass: Class<out BaseFragment>, arguments: Map<String, Serializable>?) {
        val fragment = fragmentClass.newInstance()
        val bundle = Bundle()
        if (arguments != null) {
            for ((key, value) in arguments.entries) {
                bundle.putSerializable(key, value)
            }
        }
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(base_container.id, fragment).commitNowAllowingStateLoss()
    }

    override fun navigateBack() {
        onBackPressed()
    }

    companion object {
        const val FRAGMENT = "fragment"
        const val ARGUMENTS = "arguments"
    }
}
