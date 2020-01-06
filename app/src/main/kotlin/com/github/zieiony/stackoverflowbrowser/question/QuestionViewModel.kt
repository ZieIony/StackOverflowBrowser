package com.github.zieiony.stackoverflowbrowser.question

import com.github.zieiony.base.arch.BaseState
import com.github.zieiony.base.arch.BaseViewModel
import com.github.zieiony.base.util.Logger
import com.github.zieiony.stackoverflowbrowser.api.IQuestionRepository
import com.github.zieiony.stackoverflowbrowser.api.QuestionRepository.Companion.FIRST_PAGE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable

sealed class QuestionState : BaseState {
    object Searching : QuestionState()
    class Results(var items: Array<out Serializable>, var lastPage: Boolean) : QuestionState()
    class Error(var error: Throwable) : QuestionState()
}

class QuestionViewModel(
        logger: Logger,
        private var repository: IQuestionRepository
) : BaseViewModel<QuestionState>(logger) {

    private var items: Array<out Serializable>

    init {
        this.items = emptyArray()
        state.value = QuestionState.Searching
    }

    fun getAnswers(questionId: Long, page: Int) {
        state.value = QuestionState.Searching
        repository.getAnswers(questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    items = when {
                        page > FIRST_PAGE -> arrayOf(*items, *response.items!!)
                        else -> response.items!!
                    }
                    state.value = QuestionState.Results(items, !response.has_more!!)
                }, { throwable ->
                    state.value = QuestionState.Error(throwable)
                })
                .disposeOnCleared()
    }
}
