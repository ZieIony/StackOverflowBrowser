package com.github.zieiony.stackoverflowbrowser

import android.os.Bundle
import com.github.zieiony.stackoverflowbrowser.navigation.NavigationActivity
import com.github.zieiony.stackoverflowbrowser.search.SearchFragment

class SplashActivity : NavigationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigate(SearchFragment::class.java)
        finish()
    }

}

