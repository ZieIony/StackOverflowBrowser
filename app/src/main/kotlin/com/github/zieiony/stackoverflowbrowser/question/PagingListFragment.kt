package com.github.zieiony.stackoverflowbrowser.question

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.github.zieiony.stackoverflowbrowser.api.RequestConfiguration
import com.github.zieiony.stackoverflowbrowser.navigation.BaseFragment
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

abstract class PagingListFragment : BaseFragment() {

    val layoutManager = LinearLayoutManager(context)
    var isLastPage = AtomicBoolean(false)
    var currentPage = AtomicInteger(FIRST_PAGE)

    val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (!isRefreshing() && !isLastPage.get()) {
                if (layoutManager.childCount + firstVisibleItemPosition >= layoutManager.itemCount
                        && firstVisibleItemPosition >= 0
                        && layoutManager.itemCount >= RequestConfiguration.PAGE_SIZE) {
                    loadNextPage()
                }
            }
        }
    }

    abstract fun loadNextPage()

    abstract fun isRefreshing(): Boolean

    companion object {
        const val FIRST_PAGE = 1

    }
}