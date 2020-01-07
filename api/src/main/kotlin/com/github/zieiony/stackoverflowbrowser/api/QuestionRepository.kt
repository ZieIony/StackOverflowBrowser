package com.github.zieiony.stackoverflowbrowser.api

import com.github.zieiony.stackoverflowbrowser.api.data.AnswersResponse
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse
import io.reactivex.Observable

interface QuestionRepository {
    fun getQuestions(query: String, page: Int): Observable<QuestionsResponse>

    fun getAnswers(questionId: Long, page: Int): Observable<AnswersResponse>
}
