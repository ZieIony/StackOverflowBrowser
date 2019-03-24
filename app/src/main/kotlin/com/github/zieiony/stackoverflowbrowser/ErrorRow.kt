package com.github.zieiony.stackoverflowbrowser

import android.view.ViewGroup
import carbon.component.DataBindingComponent
import java.io.Serializable

class ErrorValue(var message:String) : Serializable
class ErrorRow(parent: ViewGroup?)  : DataBindingComponent<ErrorValue>(parent, R.layout.row_error)
