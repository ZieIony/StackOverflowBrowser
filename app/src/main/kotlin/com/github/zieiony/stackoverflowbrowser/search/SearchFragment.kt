package com.github.zieiony.stackoverflowbrowser.search

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.text.TextUtils
import android.view.View
import carbon.recycler.RowArrayAdapter
import carbon.recycler.RowFactory
import com.github.zieiony.stackoverflowbrowser.ErrorRow
import com.github.zieiony.stackoverflowbrowser.ErrorValue
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import com.github.zieiony.stackoverflowbrowser.base.PagingListFragment
import com.github.zieiony.stackoverflowbrowser.base.RefreshingDelegate
import com.github.zieiony.stackoverflowbrowser.navigation.FragmentAnnotation
import com.github.zieiony.stackoverflowbrowser.question.QuestionFragment
import com.github.zieiony.stackoverflowbrowser.ui.KeyboardUtil
import kotlinx.android.synthetic.main.fragment_search.*
import java.io.Serializable
import javax.inject.Inject
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.ui.widget.OnSearchListener

@FragmentAnnotation(layout = R.layout.fragment_search)
class SearchFragment : PagingListFragment() {

    private val adapter = RowArrayAdapter<Serializable, Question>(Question::class.java, RowFactory<Question> { parent -> QuestionRow(parent) }).also {
        it.addFactory(EmptyValue::class.java, { parent -> EmptyRow(parent) })
        it.addFactory(ErrorValue::class.java, { parent -> ErrorRow(parent) })
    }

    @Inject
    lateinit var searchViewModel: SearchViewModel

    override var refreshing: Boolean by RefreshingDelegate {search_swipeRefresh}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_recycler.layoutManager = layoutManager
        search_recycler.adapter = adapter
        search_recycler.addOnScrollListener(onScrollListener)
        adapter.setOnItemClickedListener(Question::class.java, { _, question, _ -> navigate(QuestionFragment.makeStep(question as Question)) })
        adapter.items = arrayOf(EmptyValue())

        search_view.query = "Java"
        search_view.setOnSearchListener(object : OnSearchListener {
            override fun onSearch(query: String) = search(query)
        })
        search_swipeRefresh.setOnRefreshListener {
            loadPage(FIRST_PAGE)
        }

        search_openSearch.setOnClickListener { search_view.open(search_openSearch) }

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState)

        searchViewModel.getState().observe(this, Observer {
            when (it) {
                is SearchState.Empty -> refreshing = false
                is SearchState.Searching -> refreshing = true
                is SearchState.Results -> {
                    adapter.items = it.items
                    isLastPage.set(it.lastPage)
                    refreshing = false
                }
                is SearchState.Error -> {
                    refreshing = false
                    adapter.items = arrayOf(ErrorValue(it.error.message.toString()))
                }
            }
        })
    }

    private fun search(query: String) {
        KeyboardUtil.hideKeyboard(search_view)
        if (TextUtils.isEmpty(query))
            return
        arguments!!.putString(CURRENT_QUERY, query)
        search_swipeRefresh.isEnabled = true
        loadPage(FIRST_PAGE)
    }

    override fun loadPage(page:Int) {
        searchViewModel.search(arguments!!.getString(CURRENT_QUERY)!!, page)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (adapter.items.isNotEmpty())
            outState.putSerializable(ITEMS, adapter.items)
    }

    private fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(ITEMS))
            adapter.items = savedInstanceState.getSerializable(ITEMS) as Array<out Serializable>?
    }

    companion object {
        const val CURRENT_QUERY = "query"
        const val ITEMS = "items"
    }
}

