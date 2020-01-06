package com.github.zieiony.stackoverflowbrowser.search

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.recycler.RowArrayAdapter
import carbon.widget.RecyclerView
import com.github.zieiony.base.app.Navigator
import com.github.zieiony.base.app.ScreenAnnotation
import com.github.zieiony.stackoverflowbrowser.ErrorRow
import com.github.zieiony.stackoverflowbrowser.ErrorValue
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.StackOverflowFragment
import com.github.zieiony.stackoverflowbrowser.api.QuestionRepository.Companion.FIRST_PAGE
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import com.github.zieiony.stackoverflowbrowser.question.QuestionFragment
import com.github.zieiony.stackoverflowbrowser.ui.KeyboardUtil
import com.github.zieiony.stackoverflowbrowser.ui.widget.OnSearchListener
import kotlinx.android.synthetic.main.fragment_search.*
import java.io.Serializable
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@ScreenAnnotation(layout = R.layout.fragment_search)
class SearchFragment(parentNavigator: Navigator) : StackOverflowFragment(parentNavigator) {

    private lateinit var adapter: RowArrayAdapter<Serializable>

    @Inject
    lateinit var viewModelFactory: SearchViewModelFactory

    private lateinit var searchViewModel: SearchViewModel

    private var currentPage = AtomicInteger(FIRST_PAGE)

    private var isLastPage = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stackOverflowApplication!!.component.inject(this)
        searchViewModel = getViewModel(SearchViewModel::class.java, viewModelFactory)

        adapter = RowArrayAdapter()
        adapter.putFactory(Question::class.java, { QuestionRow(it) })
        adapter.putFactory(EmptyValue::class.java, { EmptyRow(it) })
        adapter.putFactory(ErrorValue::class.java, { ErrorRow(it) })
        adapter.items = arrayOf(EmptyValue())
        adapter.setOnItemClickedListener(Question::class.java, { question ->
            navigateTo(QuestionFragment(this, question))
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        search_view.query = "Java"  // example
        search_view.setOnSearchListener(object : OnSearchListener {
            override fun onSearch(query: String) = search(query)
        })
        search_swipeRefresh.setOnRefreshListener {
            loadPage(FIRST_PAGE)
        }

        search_openSearch.setOnClickListener { search_view.open(search_openSearch) }

        searchViewModel.getState().observe(this, Observer(this@SearchFragment::onStateChanged))
    }

    private fun initRecycler() {
        val layoutManager = LinearLayoutManager(context)
        search_recycler.layoutManager = layoutManager
        search_recycler.adapter = adapter
        search_recycler.addOnScrollListener(object : RecyclerView.Pagination(layoutManager) {
            override fun isLastPage() = this@SearchFragment.isLastPage.get()

            override fun loadNextPage() {
                arguments!!.getString(CURRENT_QUERY)?.let {
                    loadPage(currentPage.incrementAndGet())
                }
            }

            override fun isLoading() = search_swipeRefresh.isRefreshing
        })
    }

    private fun onStateChanged(state: SearchState) {
        when (state) {
            is SearchState.Empty -> search_swipeRefresh.isRefreshing = false
            is SearchState.Searching -> search_swipeRefresh.isRefreshing = true
            is SearchState.Results -> {
                adapter.items = state.items
                isLastPage.set(state.lastPage)
                search_swipeRefresh.isRefreshing = false
            }
            is SearchState.Error -> {
                search_swipeRefresh.isRefreshing = false
                adapter.items = arrayOf(ErrorValue(state.error.message.toString()))
            }
        }
    }

    private fun search(query: String) {
        KeyboardUtil.hideKeyboard(search_view)
        if (TextUtils.isEmpty(query))
            return
        arguments!!.putString(CURRENT_QUERY, query)
        search_swipeRefresh.isEnabled = true
        loadPage(FIRST_PAGE)
    }

    private fun loadPage(page: Int) {
        searchViewModel.search(arguments!!.getString(CURRENT_QUERY)!!, page)
    }

    companion object {
        const val CURRENT_QUERY = "query"
    }
}

