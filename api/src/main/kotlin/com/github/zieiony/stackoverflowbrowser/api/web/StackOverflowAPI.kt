package com.github.zieiony.stackoverflowbrowser.api.web

import com.github.zieiony.stackoverflowbrowser.api.data.AnswersResponse
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StackOverflowAPI {

    @GET("search")
    fun searchQuestions(
            @Query("intitle") query: String,
            @Query("page") page: Int,
            @Query("pagesize") pagesize: Int,
            @Query("order") order: SortingOrder,
            @Query("sort") sort: SortingType,
            @Query("site") site: String,
            @Query("filter") filter: String
    ): Observable<QuestionsResponse>

    @GET("questions/{questionId}/answers")
    fun requestAnswers(
            @Path("questionId") questionId: Long,
            @Query("page") page: Int,
            @Query("pagesize") pagesize: Int,
            @Query("order") order: SortingOrder,
            @Query("sort") sort: SortingType,
            @Query("site") site: String,
            @Query("filter") filter: String
    ): Observable<AnswersResponse>

    companion object {
        const val DEFAULT_API_URL = "https://api.stackexchange.com/2.2/"
    }
}
