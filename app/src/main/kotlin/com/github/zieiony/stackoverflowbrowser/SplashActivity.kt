package com.github.zieiony.stackoverflowbrowser

import android.os.Bundle
import com.github.zieiony.stackoverflowbrowser.navigation.BaseActivity
import com.github.zieiony.stackoverflowbrowser.search.SearchFragment

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigate(SearchFragment::class.java)
        finish()
    }

}

