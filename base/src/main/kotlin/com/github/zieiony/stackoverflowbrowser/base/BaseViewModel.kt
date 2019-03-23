package com.github.zieiony.stackoverflowbrowser

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {
    private val disposables = CompositeDisposable()

    fun addDisposable(disposable: Disposable) = disposables.add(disposable)

    override fun onCleared() {
        disposables.clear()
    }
}
