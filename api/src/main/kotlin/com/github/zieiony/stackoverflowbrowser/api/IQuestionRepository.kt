package com.github.zieiony.stackoverflowbrowser.api

import com.github.zieiony.stackoverflowbrowser.api.data.AnswersResponse
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse
import io.reactivex.Observable
import tk.zielony.dataapi.Response

interface IQuestionRepository {
    fun getQuestions(query: String, page: Int): Observable<Response<QuestionsResponse>>

    fun getAnswers(questionId: Long): Observable<Response<AnswersResponse>>
}
