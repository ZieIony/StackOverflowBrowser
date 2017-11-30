package com.github.zieiony.stackoverflowbrowser.api

import com.github.zieiony.stackoverflowbrowser.api.data.AnswersResponse
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import tk.zielony.dataapi.CacheStrategy
import tk.zielony.dataapi.Configuration
import tk.zielony.dataapi.Response
import tk.zielony.dataapi.WebAPI

object StackOverflowAPI {
    private const val API_URL = "https://api.stackexchange.com/2.2"
    private val configuration = Configuration()

    init {
        configuration.cacheStrategy = CacheStrategy.NONE
    }

    private var webAPI: WebAPI = WebAPI(API_URL, configuration)

    fun searchQuestions(query: String, page: Int, configuration: RequestConfiguration? = RequestConfiguration()): Observable<Response<QuestionsResponse>> {
        return webAPI.get("/search?intitle=$query&$configuration&page=$page", QuestionsResponse::class.java)
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    fun requestAnswers(questionId: Long, configuration: RequestConfiguration? = RequestConfiguration()): Observable<Response<AnswersResponse>> {
        return webAPI.get("/questions/$questionId/answers?$configuration", AnswersResponse::class.java)
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    fun cancelRequests() {
        webAPI.cancelRequests()
    }

}
