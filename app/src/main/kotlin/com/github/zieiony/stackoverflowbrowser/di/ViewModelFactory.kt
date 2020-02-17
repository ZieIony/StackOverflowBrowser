package com.github.zieiony.stackoverflowbrowser.di

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.github.zieiony.stackoverflowbrowser.question.GetAnswersInteractor
import com.github.zieiony.stackoverflowbrowser.question.QuestionViewModel
import com.github.zieiony.stackoverflowbrowser.search.GetQuestionsInteractor
import com.github.zieiony.stackoverflowbrowser.search.SearchViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @AssistedInject constructor(
        @Assisted owner: SavedStateRegistryOwner,

        val getAnswersInteractor: GetAnswersInteractor,
        val getQuestionsInteractor: GetQuestionsInteractor
) : AbstractSavedStateViewModelFactory(owner, Bundle()) {

    @AssistedInject.Factory
    interface Factory {
        fun create(owner: SavedStateRegistryOwner): ViewModelFactory
    }

    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        return when (modelClass) {
            QuestionViewModel::class.java -> QuestionViewModel(handle, getAnswersInteractor) as T
            SearchViewModel::class.java -> SearchViewModel(handle, getQuestionsInteractor) as T
            else -> throw ClassNotFoundException()
        }
    }
}
