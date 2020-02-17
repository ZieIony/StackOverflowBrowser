package com.github.zieiony.stackoverflowbrowser.question

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.recycler.RowArrayAdapter
import carbon.widget.RecyclerView
import com.github.zieiony.base.app.FragmentArgumentDelegate
import com.github.zieiony.base.app.Navigator
import com.github.zieiony.base.app.ScreenAnnotation
import com.github.zieiony.stackoverflowbrowser.ErrorRow
import com.github.zieiony.stackoverflowbrowser.ErrorValue
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.StackOverflowFragment
import com.github.zieiony.stackoverflowbrowser.api.data.Answer
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import kotlinx.android.synthetic.main.fragment_question.*
import java.io.Serializable
import java.util.concurrent.atomic.AtomicBoolean

@ScreenAnnotation(layoutId = R.layout.fragment_question)
class QuestionFragment : StackOverflowFragment {

    private val viewModel by lazy { getViewModel(QuestionViewModel::class.java) }

    private lateinit var adapter: RowArrayAdapter<Serializable>

    private var question: Question by FragmentArgumentDelegate()

    private var isLastPage = AtomicBoolean(false)

    constructor(parentNavigator: Navigator) : super(parentNavigator)

    constructor(parentNavigator: Navigator, question: Question) : super(parentNavigator) {
        this.question = question
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = RowArrayAdapter()
        adapter.putFactory(Answer::class.java, { AnswerRow(it) })
        adapter.putFactory(Question::class.java, { FullQuestionRow(it) })
        adapter.putFactory(ErrorValue::class.java, { ErrorRow(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null)
            loadFirstPage()

        question_toolbar.title = question.title

        initRecycler()

        question_swipeRefresh.setOnRefreshListener {
            loadFirstPage()
        }

        viewModel.getState().observe(viewLifecycleOwner, Observer { onStateChanged(it) })
    }

    private fun initRecycler() {
        adapter.items = arrayOf(question)

        val layoutManager = LinearLayoutManager(context)
        question_recycler.layoutManager = layoutManager
        question_recycler.adapter = adapter
        question_recycler.addOnScrollListener(object : RecyclerView.Pagination(layoutManager) {
            override fun isLastPage() = this@QuestionFragment.isLastPage.get()

            override fun loadNextPage() = (this@QuestionFragment).loadNextPage()

            override fun isLoading() = question_swipeRefresh.isRefreshing
        })
    }

    private fun loadFirstPage() = viewModel.loadFirstPage(question)

    private fun loadNextPage() = viewModel.getNextPage()

    private fun onStateChanged(state: QuestionState) {
        when (state) {
            is QuestionState.Searching -> question_swipeRefresh.isRefreshing = true
            is QuestionState.Results -> showResults(state.items, state.lastPage)
            is QuestionState.Error -> showError(state.error)
        }
    }

    override fun showError(exception: Throwable) {
        question_swipeRefresh.isRefreshing = false
        adapter.items = arrayOf(ErrorValue(exception.message.toString()))
    }

    private fun showResults(items: Array<out Serializable>, lastPage: Boolean) {
        adapter.items = arrayOf(question, *items)
        isLastPage.set(lastPage)
        question_swipeRefresh.isRefreshing = false
    }
}

