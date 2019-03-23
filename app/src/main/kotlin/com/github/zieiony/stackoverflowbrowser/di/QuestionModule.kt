package com.github.zieiony.stackoverflowbrowser.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.github.zieiony.stackoverflowbrowser.api.IQuestionRepository
import com.github.zieiony.stackoverflowbrowser.question.QuestionFragment
import com.github.zieiony.stackoverflowbrowser.question.QuestionViewModel
import dagger.Module
import dagger.Provides

class QuestionViewModelFactory(private val repository: IQuestionRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuestionViewModel(repository) as T
    }
}

@Module
class QuestionModule {

    @Provides
    fun provideSearchViewModelFactory(repository: IQuestionRepository): QuestionViewModelFactory {
        return QuestionViewModelFactory(repository)
    }

    @Provides
    fun provideSearchViewModel(fragment: QuestionFragment, viewModelFactory: QuestionViewModelFactory): QuestionViewModel {
        return ViewModelProviders.of(fragment, viewModelFactory).get(QuestionViewModel::class.java)
    }

}
