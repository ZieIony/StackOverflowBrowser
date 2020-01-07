package com.github.zieiony.stackoverflowbrowser.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.zieiony.base.util.Logger
import javax.inject.Inject

class QuestionViewModelFactory @Inject constructor(
        private val logger: Logger,
        private val getAnswersInteractor: GetAnswersInteractor
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuestionViewModel(logger, getAnswersInteractor) as T
    }
}
