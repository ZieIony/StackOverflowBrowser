package com.github.zieiony.stackoverflowbrowser.search

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.text.TextUtils
import android.view.View
import carbon.recycler.RowArrayAdapter
import carbon.recycler.RowFactory
import com.github.zieiony.stackoverflowbrowser.ErrorFragment
import com.github.zieiony.stackoverflowbrowser.PagingListFragment
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import com.github.zieiony.stackoverflowbrowser.navigation.FragmentAnnotation
import com.github.zieiony.stackoverflowbrowser.question.QuestionFragment
import com.github.zieiony.stackoverflowbrowser.ui.KeyboardUtil
import kotlinx.android.synthetic.main.fragment_search.*
import pl.zielony.statemachine.OnStateChangedListener
import pl.zielony.statemachine.StateMachine
import java.io.Serializable
import javax.inject.Inject

@FragmentAnnotation(layout = R.layout.fragment_search)
class SearchFragment : PagingListFragment() {

    private val adapter = RowArrayAdapter<Serializable, Question>(Question::class.java, RowFactory<Question> { parent -> QuestionRow(parent) }).also {
        it.addFactory(EmptyValue::class.java, { parent -> EmptyRow(parent) })
    }

    @Inject
    lateinit var searchViewModel:SearchViewModel

    private val stateMachine = StateMachine<SearchFragmentState>(SearchFragmentState.CLOSED)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupStateMachine()

        search_recycler.layoutManager = layoutManager
        search_recycler.adapter = adapter
        search_recycler.addOnScrollListener(onScrollListener)
        adapter.setOnItemClickedListener(Question::class.java, { _, question, _ -> navigate(QuestionFragment.makeStep(question as Question)) })
        adapter.items = arrayOf(EmptyValue())

        search_query.setOnEditorActionListener { textView, i, keyEvent ->
            KeyboardUtil.hideKeyboard(activity)
            val query = textView.text.toString()
            if (TextUtils.isEmpty(query))
                return@setOnEditorActionListener false
            arguments!!.putString(CURRENT_QUERY, query)
            search_swipeRefresh.isEnabled=true
            searchQuestions(query, FIRST_PAGE)
            true
        }
        search_swipeRefresh.setOnRefreshListener {
            searchQuestions(arguments!!.getString(CURRENT_QUERY)!!, FIRST_PAGE)
        }

        search_openSearch.setOnClickListener { stateMachine.setState(SearchFragmentState.OPEN) }
        search_close.setOnClickListener { stateMachine.setState(SearchFragmentState.CLOSED) }

        search_search.setOnClickListener {
            KeyboardUtil.hideKeyboard(activity)
            if (search_query.text.isEmpty())
                return@setOnClickListener
            searchQuestions(search_query.text.toString(), FIRST_PAGE)
        }

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState)

        when {
            searchViewModel.getState().value is SearchState.Empty -> {
                search_swipeRefresh.isRefreshing = false
            }
            searchViewModel.getState().value is SearchState.Searching -> {
                search_swipeRefresh.isRefreshing = true
            }
            searchViewModel.getState().value is SearchState.Results -> {
                val results = searchViewModel.getState().value as SearchState.Results
                adapter.items = results.items
                isLastPage.set(results.lastPage)
                search_swipeRefresh.isRefreshing = false
            }
            searchViewModel.getState().value is SearchState.Error -> {
                val error = searchViewModel.getState().value as SearchState.Error
                search_swipeRefresh.isRefreshing = false
                navigate(ErrorFragment.makeStep(resources.getString(R.string.error_title_requestFailed), error.toString()))
            }
        }
    }

    private fun setupStateMachine() {
        stateMachine.addEdge(SearchFragmentState.CLOSED, SearchFragmentState.OPEN, OnStateChangedListener {
            search_bar.visibility = View.VISIBLE
            if (isResumed) {
                openSearchView()
            }
            KeyboardUtil.showKeyboard(search_query)
        })

        stateMachine.addEdge(SearchFragmentState.OPEN, SearchFragmentState.CLOSED, OnStateChangedListener {
            if (isResumed) {
                closeSearchView()
            } else {
                search_bar.visibility = View.INVISIBLE
            }
            KeyboardUtil.hideKeyboard(activity)
        })
    }

    private fun openSearchView() {
        // TODO not business logic - move to a widget
        val setLocation = IntArray(2)
        search_bar.getLocationOnScreen(setLocation)
        val sbLocation = IntArray(2)
        search_openSearch.getLocationOnScreen(sbLocation)
        val animator = search_bar.createCircularReveal(sbLocation[0] - setLocation[0] + search_bar.width / 2,
                search_bar.height / 2, 0f, search_bar.width.toFloat())
        animator.interpolator = FastOutSlowInInterpolator()
        animator.start()
    }

    private fun closeSearchView() {
        // TODO not business logic - move to a widget
        val setLocation = IntArray(2)
        search_bar.getLocationOnScreen(setLocation)
        val sbLocation = IntArray(2)
        search_openSearch.getLocationOnScreen(sbLocation)
        val animator = search_bar.createCircularReveal(sbLocation[0] - setLocation[0] + search_bar.width / 2,
                search_bar.height / 2,
                search_bar.width.toFloat(), 0f)
        animator.interpolator = FastOutSlowInInterpolator()
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                search_bar.visibility = View.INVISIBLE
            }
        })
        animator.start()
    }

    override fun isRefreshing(): Boolean = search_swipeRefresh.isRefreshing

    override fun loadNextPage() {
        searchQuestions(arguments!!.getString(CURRENT_QUERY)!!, currentPage.get() + 1)
    }

    private fun searchQuestions(query: String, page: Int) {
        currentPage.set(page)
        searchViewModel.search(api, query, page)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (adapter.items.isNotEmpty())
            outState.putSerializable(ITEMS, adapter.items)
        stateMachine.save(outState)
    }

    private fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(ITEMS))
            adapter.items = savedInstanceState.getSerializable(ITEMS) as Array<out Serializable>?
        stateMachine.restore(savedInstanceState)
    }

    companion object {
        const val CURRENT_QUERY = "query"
        const val ITEMS = "items"
    }
}

