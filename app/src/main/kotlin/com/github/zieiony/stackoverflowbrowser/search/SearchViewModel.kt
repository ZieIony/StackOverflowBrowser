package com.github.zieiony.stackoverflowbrowser.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.zieiony.stackoverflowbrowser.PagingListFragment
import com.github.zieiony.stackoverflowbrowser.api.StackOverflowAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.Serializable

sealed class SearchState {
    class Empty : SearchState()
    class Searching : SearchState()
    class Results(var items: Array<out Serializable>,var lastPage: Boolean) : SearchState()
    class Error(var error: Throwable) : SearchState()
}

class SearchViewModel : ViewModel() {
    var items: Array<out Serializable> = arrayOf(EmptyValue())
    var liveData = MutableLiveData<SearchState>().also { it.value = SearchState.Empty() }

    fun getState(): MutableLiveData<SearchState> {
        return liveData
    }

    fun search(api: StackOverflowAPI, query: String, page: Int) {
        liveData.value = SearchState.Searching()
        api.cancelRequests()
        val disposable = api.searchQuestions(query, page)
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
                })
    }
}
