package com.github.zieiony.stackoverflowbrowser.search

import android.arch.lifecycle.MutableLiveData
import com.github.zieiony.stackoverflowbrowser.BaseViewModel
import com.github.zieiony.stackoverflowbrowser.PagingListFragment
import com.github.zieiony.stackoverflowbrowser.api.IQuestionRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.Serializable

sealed class SearchState {
    class Empty : SearchState()
    class Searching : SearchState()
    class Results(var items: Array<out Serializable>,var lastPage: Boolean) : SearchState()
    class Error(var error: Throwable) : SearchState()
}

class SearchViewModel : BaseViewModel {

    private var repository: IQuestionRepository
    private var items: Array<out Serializable>
    private var liveData: MutableLiveData<SearchState>

    constructor(repository: IQuestionRepository) : super() {
        this.repository = repository
        this.items = arrayOf(EmptyValue())
        this.liveData = MutableLiveData<SearchState>().also { it.value = SearchState.Empty() }
    }

    fun getState(): MutableLiveData<SearchState> {
        return liveData
    }

    fun search(query: String, page: Int) {
        liveData.value = SearchState.Searching()
        addDisposable(repository.getQuestions(query, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    items = when {
                        page > PagingListFragment.FIRST_PAGE -> arrayOf(*items, *response.data.items!!)
                        response.data.items!!.isNotEmpty() -> response.data.items!!
                        else -> arrayOf(EmptyValue())
                    }
                    liveData.value = SearchState.Results(items, !response.data.has_more!!)
                }, { throwable ->
                    liveData.value = SearchState.Error(throwable)
                }))
    }
}
