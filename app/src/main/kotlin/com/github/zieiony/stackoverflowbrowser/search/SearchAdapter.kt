package com.github.zieiony.stackoverflowbrowser.search

import carbon.recycler.RowArrayAdapter
import com.github.zieiony.stackoverflowbrowser.ErrorRow
import com.github.zieiony.stackoverflowbrowser.ErrorValue
import com.github.zieiony.stackoverflowbrowser.api.data.Question


class SearchAdapter(
        var onQuestionClickedListener: ((Question) -> Unit)
) : RowArrayAdapter<Any>() {
    init {
        putFactory(Question::class.java, { QuestionRow(it) })
        putFactory(EmptyValue::class.java, { EmptyRow(it) })
        putFactory(ErrorValue::class.java, { ErrorRow(it) })
        items = arrayOf(EmptyValue())
        setOnItemClickedListener(Question::class.java, { question ->
            onQuestionClickedListener(question)
        })
    }
}