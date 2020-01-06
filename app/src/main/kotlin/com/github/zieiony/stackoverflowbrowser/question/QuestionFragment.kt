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
import com.github.zieiony.stackoverflowbrowser.api.QuestionRepository.Companion.FIRST_PAGE
import com.github.zieiony.stackoverflowbrowser.api.data.Answer
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import com.github.zieiony.stackoverflowbrowser.search.SearchFragment
import kotlinx.android.synthetic.main.fragment_question.*
import java.io.Serializable
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@ScreenAnnotation(layout = R.layout.fragment_question)
class QuestionFragment : StackOverflowFragment {

    private lateinit var adapter: RowArrayAdapter<Serializable>

    @Inject
    lateinit var viewModelFactory: QuestionViewModelFactory

    lateinit var viewModel: QuestionViewModel

    private var question: Question by FragmentArgumentDelegate()

    private var currentPage = AtomicInteger(FIRST_PAGE)

    private var isLastPage = AtomicBoolean(false)

    constructor(parentNavigator: Navigator) : super(parentNavigator)

    constructor(parentNavigator: Navigator, question: Question) : super(parentNavigator) {
        this.question = question
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stackOverflowApplication!!.component.inject(this)
        viewModel = getViewModel(QuestionViewModel::class.java, viewModelFactory)

        adapter = RowArrayAdapter()
        adapter.putFactory(Answer::class.java, { AnswerRow(it) })
        adapter.putFactory(Question::class.java, { FullQuestionRow(it) })
        adapter.putFactory(ErrorValue::class.java, { ErrorRow(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null)
            loadPage(FIRST_PAGE)

        question_toolbar.title = question.title

        initRecycler()

        question_swipeRefresh.setOnRefreshListener {
            loadPage(FIRST_PAGE)
        }

        viewModel.getState().observe(this, Observer { onStateChanged(it) })
    }

    private fun onStateChanged(state: QuestionState) {
        when (state) {
            is QuestionState.Searching -> question_swipeRefresh.isRefreshing = true
            is QuestionState.Results -> {
                adapter.items = arrayOf(question, *state.items)
                isLastPage.set(state.lastPage)
                question_swipeRefresh.isRefreshing = false
            }
            is QuestionState.Error -> {
                question_swipeRefresh.isRefreshing = false
                adapter.items = arrayOf(ErrorValue(state.error.message.toString()))
            }
        }
    }

    private fun initRecycler() {
        adapter.items = arrayOf(question)

        val layoutManager = LinearLayoutManager(context)
        question_recycler.layoutManager = layoutManager
        question_recycler.adapter = adapter
        question_recycler.addOnScrollListener(object : RecyclerView.Pagination(layoutManager) {
            override fun isLastPage() = this@QuestionFragment.isLastPage.get()

            override fun loadNextPage() {
                arguments!!.getString(SearchFragment.CURRENT_QUERY)?.let {
                    loadPage(currentPage.incrementAndGet())
                }
            }

            override fun isLoading() = question_swipeRefresh.isRefreshing
        })
    }

    private fun loadPage(page: Int) = viewModel.getAnswers(question.question_id!!, page)
}

