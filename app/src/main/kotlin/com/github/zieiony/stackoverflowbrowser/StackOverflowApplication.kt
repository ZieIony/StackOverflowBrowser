package com.github.zieiony.stackoverflowbrowser

import android.content.Context
import android.support.multidex.MultiDex
import com.github.zieiony.stackoverflowbrowser.di.DaggerAppComponent
import dagger.android.support.DaggerApplication


class StackOverflowApplication : DaggerApplication() {
    private val applicationInjector = DaggerAppComponent.builder()
            .application(this)
            .build()

    override fun applicationInjector() = applicationInjector

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}