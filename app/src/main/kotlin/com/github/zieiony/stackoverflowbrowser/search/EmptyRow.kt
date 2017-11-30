package com.github.zieiony.stackoverflowbrowser.search

import android.view.ViewGroup
import carbon.component.DataBindingComponent
import com.github.zieiony.stackoverflowbrowser.R
import java.io.Serializable

class EmptyValue : Serializable
class EmptyRow(parent: ViewGroup?)  : DataBindingComponent<EmptyValue>(parent, R.layout.row_empty)