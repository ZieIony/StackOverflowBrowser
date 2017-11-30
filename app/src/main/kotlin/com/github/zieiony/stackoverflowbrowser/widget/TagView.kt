package com.github.zieiony.stackoverflowbrowser.widget

import android.content.Context
import android.util.AttributeSet
import carbon.widget.TextView
import com.github.zieiony.stackoverflowbrowser.R

class TagView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.tagViewStyle
) : TextView(context, attrs, defStyleAttr) {
    constructor(context: Context, text: String) : this(context) {
        this.text = text
    }
}