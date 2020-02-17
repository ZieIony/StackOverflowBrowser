package com.github.zieiony.stackoverflowbrowser.search

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.widget.RecyclerView
import com.github.zieiony.base.app.FragmentArgumentDelegate
import com.github.zieiony.base.app.Navigator
import com.github.zieiony.base.app.ScreenAnnotation
import com.github.zieiony.stackoverflowbrowser.ErrorValue
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.StackOverflowFragment
import com.github.zieiony.stackoverflowbrowser.question.QuestionFragment
import com.github.zieiony.stackoverflowbrowser.ui.KeyboardUtil
import com.github.zieiony.stackoverflowbrowser.ui.widget.OnSearchListener
import kotlinx.android.synthetic.main.fragment_search.*
import java.io.Serializable
import java.util.concurrent.atomic.AtomicBoolean

@ScreenAnnotation(layoutId = R.layout.fragment_search)
class SearchFragment(parentNavigator: Navigator) : StackOverflowFragment(parentNavigator) {

    private val searchViewModel by lazy { getViewModel(SearchViewModel::class.java) }

    private var adapter = SearchAdapter(
            onQuestionClickedListener = { navigateTo(QuestionFragment(this, it)) }
    )

    private var query: String? by FragmentArgumentDelegate()

    private var isLastPage = AtomicBoolean(false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        search_view.query = "Java"  // example
        search_view.setOnSearchListener(object : OnSearchListener {
            override fun onSearch(query: String) = search(query)
        })
        search_swipeRefresh.setOnRefreshListener {
            loadFirstPage()
        }

        search_openSearch.setOnClickListener { search_view.open(search_openSearch) }

        searchViewModel.getState().observe(viewLifecycleOwner, Observer(this@SearchFragment::onStateChanged))
    }

    private fun initRecycler() {
        val layoutManager = LinearLayoutManager(context)
        search_recycler.layoutManager = layoutManager
        search_recycler.adapter = adapter
        search_recycler.addOnScrollListener(object : RecyclerView.Pagination(layoutManager) {
            override fun isLastPage() = this@SearchFragment.isLastPage.get()

            override fun loadNextPage() {
                query?.let {
                    this@SearchFragment.loadNextPage()
                }
            }

            override fun isLoading() = search_swipeRefresh.isRefreshing
        })
    }

    private fun search(query: String) {
        KeyboardUtil.hideKeyboard(search_view)
        if (TextUtils.isEmpty(query))
            return
        this.query = query
        loadFirstPage()
    }

    private fun loadFirstPage() = searchViewModel.loadFirstPage(query!!)

    private fun loadNextPage() = searchViewModel.loadNextPage()

    private fun onStateChanged(state: SearchState) {
        when (state) {
            is SearchState.Empty -> search_swipeRefresh.isRefreshing = false
            is SearchState.Searching -> {
                search_swipeRefresh.isEnabled = true
                search_swipeRefresh.isRefreshing = true
            }
            is SearchState.Results -> showResults(state.items, state.lastPage)
            is SearchState.Error -> showError(state.error)
        }
    }

    override fun showError(exception: Throwable) {
        search_swipeRefresh.isRefreshing = false
        adapter.items = arrayOf(ErrorValue(exception.message.toString()))
    }

    private fun showResults(items: Array<out Serializable>, lastPage: Boolean) {
        adapter.items = items
        isLastPage.set(lastPage)
        search_swipeRefresh.isRefreshing = false
    }
}

