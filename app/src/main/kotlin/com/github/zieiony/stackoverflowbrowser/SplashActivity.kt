package com.github.zieiony.stackoverflowbrowser

import android.content.Intent
import android.os.Bundle
import com.github.zieiony.base.app.BaseActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
