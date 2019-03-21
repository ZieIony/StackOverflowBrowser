package com.github.zieiony.stackoverflowbrowser.api.di

import com.github.zieiony.stackoverflowbrowser.api.StackOverflowAPI
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class APIModule {
    @Provides
    @Singleton
    fun provideAPI(@Named(API_URL_DEPENDENCY) apiUrl: String): StackOverflowAPI {
        return StackOverflowAPI(apiUrl)
    }

    @Provides
    @Named(API_URL_DEPENDENCY)
    fun provideAPIUrl(): String {
        return StackOverflowAPI.DEFAULT_API_URL
    }

    companion object {
        const val API_URL_DEPENDENCY = "apiUrl"
    }
}