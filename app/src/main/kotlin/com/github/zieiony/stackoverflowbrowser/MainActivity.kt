package com.github.zieiony.stackoverflowbrowser

import android.os.Bundle
import com.github.zieiony.base.app.BaseActivity
import com.github.zieiony.base.app.BaseFragment
import com.github.zieiony.base.app.ScreenAnnotation
import com.github.zieiony.stackoverflowbrowser.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

@ScreenAnnotation(layout = R.layout.activity_main)
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null)
            navigateTo(SearchFragment(this))
    }

    override fun onNavigateTo(fragment: BaseFragment): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        if (main_container.childCount != 0)
            transaction.addToBackStack(BACKSTACK_NAME)
        transaction.replace(R.id.main_container, fragment)
        transaction.commit()
        return true
    }

    companion object {
        const val BACKSTACK_NAME = "main"
    }
}