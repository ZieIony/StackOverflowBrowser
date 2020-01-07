package com.github.zieiony.stackoverflowbrowser.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.zieiony.base.util.Logger
import javax.inject.Inject

class SearchViewModelFactory @Inject constructor(
        private val logger: Logger,
        private val getQuestionsInteractor: GetQuestionsInteractor
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(logger, getQuestionsInteractor) as T
    }
}
