package com.github.zieiony.stackoverflowbrowser.question

import android.arch.lifecycle.MutableLiveData
import com.github.zieiony.stackoverflowbrowser.BaseViewModel
import com.github.zieiony.stackoverflowbrowser.PagingListFragment
import com.github.zieiony.stackoverflowbrowser.api.IQuestionRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.Serializable

sealed class QuestionState {
    class Searching : QuestionState()
    class Results(var items: Array<out Serializable>,var lastPage: Boolean) : QuestionState()
    class Error(var error: Throwable) : QuestionState()
}

class QuestionViewModel : BaseViewModel {

    private var repository: IQuestionRepository
    private var items: Array<out Serializable>
    private var liveData: MutableLiveData<QuestionState>

    constructor(repository: IQuestionRepository) : super() {
        this.repository = repository
        this.items = emptyArray()
        this.liveData = MutableLiveData<QuestionState>().also { it.value = QuestionState.Searching() }
    }

    fun getState(): MutableLiveData<QuestionState> {
        return liveData
    }

     fun getAnswers(questionId: Long, page: Int) {
        liveData.value = QuestionState.Searching()
        addDisposable(repository.getAnswers(questionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    items = when {
                        page > PagingListFragment.FIRST_PAGE -> arrayOf(*items, *response.data.items!!)
                        else -> response.data.items!!
                    }
                    liveData.value = QuestionState.Results(items, !response.data.has_more!!)
                }, { throwable ->
                    liveData.value = QuestionState.Error(throwable)
                }))
    }
}
