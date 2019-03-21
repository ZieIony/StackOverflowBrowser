package com.github.zieiony.stackoverflowbrowser.di

import com.github.zieiony.stackoverflowbrowser.StackOverflowApplication
import com.github.zieiony.stackoverflowbrowser.api.di.APIModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    APIModule::class,
    ScreenModule::class])
interface AppComponent : AndroidInjector<StackOverflowApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: StackOverflowApplication): Builder

        fun build(): AppComponent
    }

    override fun inject(app: StackOverflowApplication)
}