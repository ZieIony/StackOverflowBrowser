package com.github.zieiony.stackoverflowbrowser.search

import androidx.lifecycle.SavedStateHandle
import com.github.zieiony.base.arch.BaseState
import com.github.zieiony.base.arch.BaseViewModel
import com.github.zieiony.stackoverflowbrowser.api.web.StackOverflowService.Companion.FIRST_PAGE
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.Serializable

sealed class SearchState : BaseState {
    class Empty : SearchState()
    class Searching : SearchState()
    class Results(var items: Array<out Serializable>, var lastPage: Boolean) : SearchState()
    class Error(var error: Throwable) : SearchState()
}

class SearchViewModel(
        handle: SavedStateHandle,
        val getQuestionsInteractor: GetQuestionsInteractor
) : BaseViewModel<SearchState>(handle) {

    private var items: Array<out Serializable>

    private lateinit var query: String

    private var currentPage = FIRST_PAGE

    init {
        this.items = arrayOf(EmptyValue())
        this.state.value = SearchState.Empty()
    }

    fun loadFirstPage(query: String) {
        this.query = query
        currentPage = FIRST_PAGE
        loadNextPage()
    }

    fun loadNextPage() {
        state.value = SearchState.Searching()
        getQuestionsInteractor.execute(query, currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    items = when {
                        currentPage > FIRST_PAGE -> arrayOf(*items, *response.items!!)
                        response.items!!.isNotEmpty() -> response.items!!
                        else -> arrayOf(EmptyValue())
                    }
                    currentPage++
                    state.value = SearchState.Results(items, !response.has_more!!)
                }, { throwable ->
                    state.value = SearchState.Error(throwable)
                })
                .disposeOnCleared()
    }
}
