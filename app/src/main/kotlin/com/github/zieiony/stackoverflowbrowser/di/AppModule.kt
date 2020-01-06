package com.github.zieiony.stackoverflowbrowser.di

import com.github.zieiony.base.util.Logger
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideLogger(): Logger {
        return object : Logger {
            override fun log(exception: Exception) {
            }

            override fun log(text: String) {
            }
        }
    }

}
