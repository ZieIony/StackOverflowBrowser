package com.github.zieiony.stackoverflowbrowser.question

import carbon.recycler.RowArrayAdapter
import com.github.zieiony.stackoverflowbrowser.ErrorRow
import com.github.zieiony.stackoverflowbrowser.ErrorValue
import com.github.zieiony.stackoverflowbrowser.api.data.Answer
import com.github.zieiony.stackoverflowbrowser.api.data.Question


class QuestionAdapter() : RowArrayAdapter<Any>() {
    init {
        putFactory(Answer::class.java, { AnswerRow(it) })
        putFactory(Question::class.java, { FullQuestionRow(it) })
        putFactory(ErrorValue::class.java, { ErrorRow(it) })
    }
}