package com.github.zieiony.stackoverflowbrowser.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.zieiony.base.util.Logger
import com.github.zieiony.stackoverflowbrowser.api.IQuestionRepository

class SearchViewModelFactory(
        private val logger: Logger,
        private val repository: IQuestionRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(logger, repository) as T
    }
}
