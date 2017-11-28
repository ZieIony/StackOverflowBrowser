package com.github.zieiony.stackoverflowbrowser.ui

import android.view.ViewGroup
import carbon.component.DataBindingComponent
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.api.data.Answer

class AnswerRow(parent: ViewGroup?) : DataBindingComponent<Answer>(parent, R.layout.row_answer)