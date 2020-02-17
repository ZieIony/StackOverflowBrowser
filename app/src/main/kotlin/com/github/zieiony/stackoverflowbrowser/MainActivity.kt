package com.github.zieiony.stackoverflowbrowser

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.zieiony.base.app.BaseActivity
import com.github.zieiony.base.app.ScreenAnnotation
import com.github.zieiony.stackoverflowbrowser.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

@ScreenAnnotation(layoutId = R.layout.activity_main)
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null)
            navigateTo(SearchFragment(this))
    }

    override fun onNavigateTo(fragment: Fragment): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        if(resources.getBoolean(R.bool.carbon_isPhone)) {
            if (main_container.childCount != 0)
                transaction.addToBackStack(BACKSTACK_NAME)
            transaction.replace(R.id.main_container, fragment)
        }else{
            if(fragment is SearchFragment) {
                transaction.replace(R.id.main_container, fragment)
            }else{
                transaction.replace(R.id.detail_container, fragment)
            }
        }
        transaction.commit()
        return true
    }

    companion object {
        const val BACKSTACK_NAME = "main"
    }
}