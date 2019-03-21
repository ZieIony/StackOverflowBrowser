package com.github.zieiony.stackoverflowbrowser.api

import com.github.zieiony.stackoverflowbrowser.api.data.AnswersResponse
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse
import com.github.zieiony.stackoverflowbrowser.api.di.APIModule
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import tk.zielony.dataapi.CacheStrategy
import tk.zielony.dataapi.Configuration
import tk.zielony.dataapi.Response
import tk.zielony.dataapi.WebAPI
import javax.inject.Inject
import javax.inject.Named

class StackOverflowAPI {

    private val configuration: Configuration
    private var webAPI: WebAPI

    @Inject constructor(@Named(APIModule.API_URL_DEPENDENCY) apiUrl: String) {
        this.configuration = Configuration()
        configuration.cacheStrategy = CacheStrategy.NONE
        this.webAPI = WebAPI(apiUrl, configuration)
    }

    fun searchQuestions(query: String, page: Int, configuration: RequestConfiguration? = RequestConfiguration()): Observable<Response<QuestionsResponse>> {
        return webAPI.get("/search?intitle=$query&$configuration&page=$page", QuestionsResponse::class.java)
                .subscribeOn(Schedulers.io())
    }

    fun requestAnswers(questionId: Long, configuration: RequestConfiguration? = RequestConfiguration()): Observable<Response<AnswersResponse>> {
        return webAPI.get("/questions/$questionId/answers?$configuration", AnswersResponse::class.java)
                .subscribeOn(Schedulers.io())
    }

    fun cancelRequests() {
        webAPI.cancelRequests()
    }

    companion object {
        const val DEFAULT_API_URL = "https://api.stackexchange.com/2.2"

    }
}
