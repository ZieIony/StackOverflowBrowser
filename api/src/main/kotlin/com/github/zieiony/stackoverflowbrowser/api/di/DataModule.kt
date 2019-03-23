package com.github.zieiony.stackoverflowbrowser.api.di

import com.github.zieiony.stackoverflowbrowser.api.IQuestionRepository
import com.github.zieiony.stackoverflowbrowser.api.QuestionRepository
import com.github.zieiony.stackoverflowbrowser.api.StackOverflowAPI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideQuestionRepository(stackOverflowAPI: StackOverflowAPI): IQuestionRepository {
        return QuestionRepository(stackOverflowAPI)
    }
}