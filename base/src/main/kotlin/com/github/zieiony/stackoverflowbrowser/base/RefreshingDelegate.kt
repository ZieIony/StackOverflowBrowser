package com.github.zieiony.stackoverflowbrowser.base

import android.support.v4.widget.SwipeRefreshLayout
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class RefreshingDelegate(var swipeRefreshLayout: ()->SwipeRefreshLayout) : ReadWriteProperty<Any?, Boolean> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
        return swipeRefreshLayout().isRefreshing
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
        swipeRefreshLayout().isRefreshing = value
    }
}
