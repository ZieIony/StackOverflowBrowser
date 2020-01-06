package com.github.zieiony.stackoverflowbrowser.ui.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import carbon.widget.EditText
import carbon.widget.FrameLayout
import com.github.zieiony.stackoverflowbrowser.ui.KeyboardUtil
import com.github.zieiony.stackoverflowbrowser.ui.R

interface OnSearchListener {
    fun onSearch(query: String)
}

class SearchView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val search_query: EditText
    private val search_close: View
    private val search_search: View

    init {
        View.inflate(context, R.layout.view_search, this)
        search_query = findViewById(R.id.search_query)
        search_close = findViewById(R.id.search_close)
        search_search = findViewById(R.id.search_search)
        search_close.setOnClickListener { close(search_close) }
    }

    private var revealAnimator: Animator? = null

    var query: String
        get() = search_query.text.toString()
        set(value) = search_query.setText(value)

    fun setOnSearchListener(listener: OnSearchListener) {
        search_query.setOnEditorActionListener { v, actionId, event ->
            listener.onSearch(query)
            true
        }
        search_search.setOnClickListener {
            listener.onSearch(query)
        }
    }

    fun open(source: View) {
        visibility = View.VISIBLE

        val location = IntArray(2)
        getLocationOnScreen(location)
        val sbLocation = IntArray(2)
        source.getLocationOnScreen(sbLocation)
        revealAnimator?.cancel()
        revealAnimator = createCircularReveal(sbLocation[0] - location[0] + source.width / 2,
                sbLocation[1] - location[1] + source.height / 2, 0f, width.toFloat()).apply {
            interpolator = FastOutSlowInInterpolator()
            start()
        }

        search_query.requestFocus()
        search_query.setSelection(search_query.length())
        KeyboardUtil.showKeyboard(search_query) // TODO: doesn't work when animating for the first time
    }

    fun close(source: View) {
        val location = IntArray(2)
        getLocationOnScreen(location)
        val sbLocation = IntArray(2)
        source.getLocationOnScreen(sbLocation)
        revealAnimator?.cancel()
        revealAnimator = createCircularReveal(sbLocation[0] - location[0] + source.width / 2,
                sbLocation[1] - location[1] + source.height / 2,
                width.toFloat(), 0f).apply {
            interpolator = FastOutSlowInInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    KeyboardUtil.hideKeyboard(search_query)
                }

                override fun onAnimationEnd(animation: Animator) {
                    visibility = View.INVISIBLE
                }
            })
            start() // TODO: doesn't work well, strange fading animation
        }
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState!!)
        ss.state = visibility
        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        visibility = ss.state
    }

    internal class SavedState : View.BaseSavedState {
        var state: Int = View.INVISIBLE

        constructor(superState: Parcelable) : super(superState) {}

        private constructor(`in`: Parcel) : super(`in`) {
            state = `in`.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(state)
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}
