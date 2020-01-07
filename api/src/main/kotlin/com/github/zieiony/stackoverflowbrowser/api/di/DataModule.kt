package com.github.zieiony.stackoverflowbrowser.api.di

import com.github.zieiony.stackoverflowbrowser.api.QuestionRepository
import com.github.zieiony.stackoverflowbrowser.api.QuestionRepositoryImpl
import com.github.zieiony.stackoverflowbrowser.api.web.StackOverflowService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideQuestionRepository(stackOverflowService: StackOverflowService): QuestionRepository {
        return QuestionRepositoryImpl(stackOverflowService)
    }
}