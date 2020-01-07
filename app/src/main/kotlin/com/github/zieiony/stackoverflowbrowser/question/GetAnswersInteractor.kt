package com.github.zieiony.stackoverflowbrowser.question

import com.github.zieiony.stackoverflowbrowser.api.QuestionRepository
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class GetAnswersInteractor @Inject constructor(
        val questionRepository: QuestionRepository
) {

    fun execute(question: Question, page: Int) =
            questionRepository.getAnswers(question.question_id!!, page)
                    .subscribeOn(Schedulers.io())
}