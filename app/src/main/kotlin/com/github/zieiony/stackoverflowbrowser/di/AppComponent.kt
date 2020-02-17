package com.github.zieiony.stackoverflowbrowser.di

import com.github.zieiony.stackoverflowbrowser.StackOverflowFragment
import com.github.zieiony.stackoverflowbrowser.api.di.APIModule
import com.github.zieiony.stackoverflowbrowser.api.di.DataModule
import com.github.zieiony.stackoverflowbrowser.question.QuestionFragment
import com.github.zieiony.stackoverflowbrowser.search.SearchFragment
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    APIModule::class,
    DataModule::class,
    AppModule::class
])
interface AppComponent {
    val viewModelFactory:ViewModelFactory.Factory

    fun inject(stackOverflowFragment: StackOverflowFragment)

    fun inject(searchFragment: SearchFragment)
    fun inject(questionFragment: QuestionFragment)
}