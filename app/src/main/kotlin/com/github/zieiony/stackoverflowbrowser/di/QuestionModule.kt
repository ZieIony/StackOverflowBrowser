package com.github.zieiony.stackoverflowbrowser.di

import com.github.zieiony.base.util.Logger
import com.github.zieiony.stackoverflowbrowser.api.IQuestionRepository
import com.github.zieiony.stackoverflowbrowser.question.QuestionViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class QuestionModule {

    @Provides
    fun provideSearchViewModelFactory(logger: Logger, repository: IQuestionRepository): QuestionViewModelFactory {
        return QuestionViewModelFactory(logger, repository)
    }

}
