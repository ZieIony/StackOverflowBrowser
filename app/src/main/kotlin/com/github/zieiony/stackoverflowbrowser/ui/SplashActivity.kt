package com.github.zieiony.stackoverflowbrowser.ui

import android.os.Bundle
import com.github.zieiony.stackoverflowbrowser.navigation.BaseActivity
import com.github.zieiony.stackoverflowbrowser.ui.search.SearchFragment

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigate(SearchFragment::class.java)
        finish()
    }

}

