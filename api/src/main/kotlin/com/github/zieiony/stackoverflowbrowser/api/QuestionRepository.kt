package com.github.zieiony.stackoverflowbrowser.api

import com.github.zieiony.stackoverflowbrowser.api.data.AnswersResponse
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse
import io.reactivex.Observable
import tk.zielony.dataapi.Response

class QuestionRepository : IQuestionRepository {
    private var stackOverflowApi: StackOverflowAPI

    constructor(stackOverflowAPI: StackOverflowAPI) {
        this.stackOverflowApi = stackOverflowAPI
    }

    override fun getQuestions(query: String, page: Int): Observable<Response<QuestionsResponse>> =
            stackOverflowApi.searchQuestions(query, page)

    override fun getAnswers(questionId: Long): Observable<Response<AnswersResponse>> =
            stackOverflowApi.requestAnswers(questionId)

}
