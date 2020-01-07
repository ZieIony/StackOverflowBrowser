package com.github.zieiony.stackoverflowbrowser.api.di

import android.app.Application
import com.github.zieiony.stackoverflowbrowser.api.web.StackOverflowAPI
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
class APIModule (val application: Application) {

    @Provides
    @Singleton
    fun provideOkhttp() = OkHttpClient.Builder()
            .addInterceptor(ChuckInterceptor(application))
            .build()

    @Provides
    @Singleton
    fun provideAPI(@Named(API_URL) apiUrl: String, client: OkHttpClient): StackOverflowAPI {
        val retrofit = Retrofit.Builder()
                .baseUrl(apiUrl)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(StackOverflowAPI::class.java)
    }

    @Provides
    @Named(API_URL)
    fun provideAPIUrl() = StackOverflowAPI.DEFAULT_API_URL

    companion object {
        const val API_URL = "apiUrl"
    }
}