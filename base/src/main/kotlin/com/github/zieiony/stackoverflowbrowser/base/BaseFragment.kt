package com.github.zieiony.stackoverflowbrowser.base

import com.github.zieiony.stackoverflowbrowser.api.IQuestionRepository
import com.github.zieiony.stackoverflowbrowser.navigation.NavigationFragment
import javax.inject.Inject

abstract class BaseFragment : NavigationFragment() {

    @Inject
    lateinit var repository: IQuestionRepository

}