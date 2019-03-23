package com.github.zieiony.stackoverflowbrowser.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.github.zieiony.stackoverflowbrowser.api.IQuestionRepository
import com.github.zieiony.stackoverflowbrowser.search.SearchFragment
import com.github.zieiony.stackoverflowbrowser.search.SearchViewModel
import dagger.Module
import dagger.Provides

class SearchViewModelFactory(private val repository: IQuestionRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(repository) as T
    }
}

@Module
class SearchModule {

    @Provides
    fun provideSearchViewModelFactory(repository: IQuestionRepository): SearchViewModelFactory {
        return SearchViewModelFactory(repository)
    }

    @Provides
    fun provideSearchViewModel(fragment: SearchFragment, viewModelFactory: SearchViewModelFactory): SearchViewModel {
        return ViewModelProviders.of(fragment, viewModelFactory).get(SearchViewModel::class.java)
    }

}
