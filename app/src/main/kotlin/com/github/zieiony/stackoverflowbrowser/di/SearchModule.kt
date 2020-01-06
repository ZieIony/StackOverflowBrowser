package com.github.zieiony.stackoverflowbrowser.di

import com.github.zieiony.base.util.Logger
import com.github.zieiony.stackoverflowbrowser.api.IQuestionRepository
import com.github.zieiony.stackoverflowbrowser.search.SearchViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    fun provideSearchViewModelFactory(logger: Logger, repository: IQuestionRepository): SearchViewModelFactory {
        return SearchViewModelFactory(logger, repository)
    }

}
