package com.github.zieiony.stackoverflowbrowser.di

import com.github.zieiony.stackoverflowbrowser.SplashActivity
import com.github.zieiony.stackoverflowbrowser.navigation.NavigationActivity
import com.github.zieiony.stackoverflowbrowser.question.QuestionFragment
import com.github.zieiony.stackoverflowbrowser.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreenModule {

    @ContributesAndroidInjector()
    abstract fun bindMainActivity(): NavigationActivity

    @ContributesAndroidInjector()
    abstract fun bindDetailActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [SearchModule::class])
    abstract fun bindSearchFragment(): SearchFragment

    @ContributesAndroidInjector()
    abstract fun bindQuestionFragment(): QuestionFragment

}
