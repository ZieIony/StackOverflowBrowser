package com.github.zieiony.stackoverflowbrowser

import android.app.Application
import com.github.zieiony.stackoverflowbrowser.api.di.APIModule
import com.github.zieiony.stackoverflowbrowser.api.di.DataModule
import com.github.zieiony.stackoverflowbrowser.di.AppComponent
import com.github.zieiony.stackoverflowbrowser.di.AppModule
import com.github.zieiony.stackoverflowbrowser.di.DaggerAppComponent


class StackOverflowApplication : Application() {
    var component: AppComponent = DaggerAppComponent
            .builder()
            .aPIModule(APIModule(this))
            .dataModule(DataModule())
            .appModule(AppModule())
            .build()

}