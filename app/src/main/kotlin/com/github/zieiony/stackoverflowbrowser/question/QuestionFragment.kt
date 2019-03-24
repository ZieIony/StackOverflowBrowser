package com.github.zieiony.stackoverflowbrowser.question

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import carbon.recycler.RowArrayAdapter
import carbon.recycler.RowFactory
import com.github.zieiony.stackoverflowbrowser.ErrorRow
import com.github.zieiony.stackoverflowbrowser.ErrorValue
import com.github.zieiony.stackoverflowbrowser.base.PagingListFragment
import com.github.zieiony.stackoverflowbrowser.api.data.Answer
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import com.github.zieiony.stackoverflowbrowser.base.RefreshingDelegate
import com.github.zieiony.stackoverflowbrowser.navigation.FragmentAnnotation
import com.github.zieiony.stackoverflowbrowser.navigation.NavigationStep
import kotlinx.android.synthetic.main.fragment_question.*
import com.github.zieiony.stackoverflowbrowser.R
import java.io.Serializable
import javax.inject.Inject

@FragmentAnnotation(layout = R.layout.fragment_question)
class QuestionFragment : PagingListFragment() {

    private val adapter = RowArrayAdapter<Serializable, Answer>(Answer::class.java, RowFactory { parent -> AnswerRow(parent) })

    @Inject
    lateinit var questionViewModel: QuestionViewModel

    private lateinit var question: Question

    override var refreshing: Boolean by RefreshingDelegate { question_swipeRefresh }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        } else {
            question = arguments!!.get(QUESTION) as Question
            loadPage(FIRST_PAGE)
        }

        question_toolbar.title = question.title

        initAdapter()

        question_swipeRefresh.setOnRefreshListener {
            loadPage(FIRST_PAGE)
        }

        questionViewModel.getState().observe(this, Observer {
            when (it) {
                is QuestionState.Searching -> question_swipeRefresh.isRefreshing = true
                is QuestionState.Results -> {
                    adapter.items = arrayOf(question, *it.items)
                    isLastPage.set(it.lastPage)
                    question_swipeRefresh.isRefreshing = false
                }
                is QuestionState.Error -> {
                    question_swipeRefresh.isRefreshing = false
                    adapter.items = arrayOf(ErrorValue(it.error.message.toString()))
                }
            }
        })
    }

    private fun initAdapter() {
        adapter.addFactory(Question::class.java, { parent -> FullQuestionRow(parent) })
        adapter.addFactory(ErrorValue::class.java, { parent -> ErrorRow(parent) })
        adapter.items = arrayOf(question)
        question_recycler.layoutManager = layoutManager
        question_recycler.adapter = adapter
        question_recycler.addOnScrollListener(onScrollListener)
    }

    override fun loadPage(page:Int) {
        questionViewModel.getAnswers(question.question_id!!, page)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (adapter.items.isNotEmpty())
            outState.putSerializable(ITEMS, adapter.items)
        outState.putSerializable(QUESTION, question)
    }

    private fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(ITEMS))
            adapter.items = savedInstanceState.getSerializable(ITEMS) as Array<out Serializable>?
        question = savedInstanceState.getSerializable(QUESTION) as Question
    }

    companion object {
        private const val QUESTION = "question"
        const val ITEMS = "items"

        fun makeStep(question: Question): NavigationStep {
            val arguments = HashMap<String, Serializable>()
            arguments[QUESTION] = question
            return NavigationStep(QuestionFragment::class.java, arguments)
        }
    }
}

