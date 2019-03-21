package com.github.zieiony.stackoverflowbrowser.di

import android.arch.lifecycle.ViewModelProviders
import com.github.zieiony.stackoverflowbrowser.search.SearchFragment
import com.github.zieiony.stackoverflowbrowser.search.SearchViewModel
import dagger.Module
import dagger.Provides

@Module
class SearchModule {
    @Provides
    fun provideViewModel(fragment: SearchFragment): SearchViewModel {
        return ViewModelProviders.of(fragment).get(SearchViewModel::class.java)
    }
}
