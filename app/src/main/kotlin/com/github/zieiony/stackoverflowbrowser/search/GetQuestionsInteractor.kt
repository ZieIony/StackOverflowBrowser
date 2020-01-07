package com.github.zieiony.stackoverflowbrowser.search

import com.github.zieiony.stackoverflowbrowser.api.QuestionRepository
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class GetQuestionsInteractor @Inject constructor(
        val questionRepository: QuestionRepository
) {

    fun execute(query: String, page: Int) =
            questionRepository.getQuestions(query, page)
                    .subscribeOn(Schedulers.io())
}