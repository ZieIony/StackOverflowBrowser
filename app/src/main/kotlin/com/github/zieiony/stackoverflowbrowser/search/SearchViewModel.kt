package com.github.zieiony.stackoverflowbrowser.search

import com.github.zieiony.base.arch.BaseState
import com.github.zieiony.base.arch.BaseViewModel
import com.github.zieiony.base.util.Logger
import com.github.zieiony.stackoverflowbrowser.api.IQuestionRepository
import com.github.zieiony.stackoverflowbrowser.api.QuestionRepository.Companion.FIRST_PAGE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable

sealed class SearchState:BaseState {
    class Empty : SearchState()
    class Searching : SearchState()
    class Results(var items: Array<out Serializable>,var lastPage: Boolean) : SearchState()
    class Error(var error: Throwable) : SearchState()
}

class SearchViewModel : BaseViewModel<SearchState> {

    private var repository: IQuestionRepository
    private var items: Array<out Serializable>

    constructor(logger: Logger, repository: IQuestionRepository) : super(logger) {
        this.repository = repository
        this.items = arrayOf(EmptyValue())
        this.state.value = SearchState.Empty()
    }

    fun search(query: String, page: Int) {
        state.value = SearchState.Searching()
        repository.getQuestions(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    items = when {
                        page > FIRST_PAGE -> arrayOf(*items, *response.items!!)
                        response.items!!.isNotEmpty() -> response.items!!
                        else -> arrayOf(EmptyValue())
                    }
                    state.value = SearchState.Results(items, !response.has_more!!)
                }, { throwable ->
                    state.value = SearchState.Error(throwable)
                })
                .disposeOnCleared()
    }
}
