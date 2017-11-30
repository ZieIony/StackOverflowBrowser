package com.github.zieiony.stackoverflowbrowser.question

import android.os.Bundle
import android.view.View
import carbon.recycler.RowArrayAdapter
import carbon.recycler.RowFactory
import com.github.zieiony.stackoverflowbrowser.ErrorFragment
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.api.StackOverflowAPI
import com.github.zieiony.stackoverflowbrowser.api.data.Answer
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import com.github.zieiony.stackoverflowbrowser.navigation.FragmentAnnotation
import com.github.zieiony.stackoverflowbrowser.navigation.NavigationStep
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_question.*
import java.io.Serializable

@FragmentAnnotation(layout = R.layout.fragment_question)
class QuestionFragment : PagingListFragment() {

    val adapter = RowArrayAdapter<Serializable, Answer>(Answer::class.java, RowFactory { parent -> AnswerRow(parent) })

    init {
        adapter.addFactory(Question::class.java, { parent -> FullQuestionRow(parent) })
    }

    private lateinit var question: Question

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        question = arguments!!.get(QUESTION) as Question

        question_toolbar.title = question.title

        adapter.items = arrayOf(question)
        question_recycler.layoutManager = layoutManager
        question_recycler.adapter = adapter
        question_recycler.addOnScrollListener(onScrollListener)

        question_swipeRefresh.setOnRefreshListener {
            getAnswers(question.question_id!!, FIRST_PAGE)
        }

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        } else {
            getAnswers(question.question_id!!, FIRST_PAGE)
        }
    }

    override fun isRefreshing(): Boolean = question_swipeRefresh.isRefreshing

    override fun loadNextPage() {
        getAnswers(question.question_id!!, currentPage.get() + 1)
    }

    private fun getAnswers(questionId: Long, page: Int) {
        currentPage.set(page)
        question_swipeRefresh.isRefreshing = true
        StackOverflowAPI.requestAnswers(questionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (currentPage.get() > FIRST_PAGE) {
                        adapter.items = arrayOf(question, *adapter.items, *response.data.items!!)
                    } else {
                        adapter.items = arrayOf(question, *response.data.items!!)
                    }
                    isLastPage.set(!response.data.has_more!!)
                    question_swipeRefresh.isRefreshing = false
                }, { throwable ->
                    question_swipeRefresh.isRefreshing = false
                    navigate(ErrorFragment.makeStep(resources.getString(R.string.error_title_requestFailed), throwable.toString()))
                })
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

    override fun onStop() {
        super.onStop()
        StackOverflowAPI.cancelRequests()
    }

    companion object {
        private val QUESTION = "question"
        const val ITEMS = "items"

        fun makeStep(question: Question): NavigationStep {
            val arguments = HashMap<String, Serializable>()
            arguments.put(QUESTION, question)
            return NavigationStep(QuestionFragment::class.java, arguments)
        }
    }
}

