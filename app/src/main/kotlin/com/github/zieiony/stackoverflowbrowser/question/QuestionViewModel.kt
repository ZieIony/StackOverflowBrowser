package com.github.zieiony.stackoverflowbrowser.question

import com.github.zieiony.base.arch.BaseState
import com.github.zieiony.base.arch.BaseViewModel
import com.github.zieiony.base.util.Logger
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import com.github.zieiony.stackoverflowbrowser.api.web.StackOverflowService.Companion.FIRST_PAGE
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.Serializable

sealed class QuestionState : BaseState {
    object Searching : QuestionState()
    class Results(var items: Array<out Serializable>, var lastPage: Boolean) : QuestionState()
    class Error(var error: Throwable) : QuestionState()
}

class QuestionViewModel(
        logger: Logger,
        private var getAnswersInteractor: GetAnswersInteractor
) : BaseViewModel<QuestionState>(logger) {

    private lateinit var question: Question

    private var currentPage = FIRST_PAGE

    private var items: Array<out Serializable>

    init {
        this.items = emptyArray()
        state.value = QuestionState.Searching
    }

    fun loadFirstPage(question: Question) {
        this.question = question
        currentPage = FIRST_PAGE
        getNextPage()
    }

    fun getNextPage() {
        state.value = QuestionState.Searching
        getAnswersInteractor.execute(question, currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    items = when {
                        currentPage > FIRST_PAGE -> arrayOf(*items, *response.items!!)
                        else -> response.items!!
                    }
                    currentPage++
                    state.value = QuestionState.Results(items, !response.has_more!!)
                }, { throwable ->
                    state.value = QuestionState.Error(throwable)
                })
                .disposeOnCleared()
    }
}
