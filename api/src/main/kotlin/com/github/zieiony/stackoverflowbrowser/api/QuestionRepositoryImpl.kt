package com.github.zieiony.stackoverflowbrowser.api

import com.github.zieiony.stackoverflowbrowser.api.data.AnswersResponse
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse
import com.github.zieiony.stackoverflowbrowser.api.web.StackOverflowService
import io.reactivex.Observable

class QuestionRepositoryImpl(
        val stackOverflowService: StackOverflowService
) : QuestionRepository {

    override fun getQuestions(query: String, page: Int): Observable<QuestionsResponse> =
            stackOverflowService.getQuestions(query, page)

    override fun getAnswers(questionId: Long, page: Int): Observable<AnswersResponse> =
            stackOverflowService.getAnswers(questionId, page)

}
