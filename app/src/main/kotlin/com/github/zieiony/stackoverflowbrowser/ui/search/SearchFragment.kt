package com.github.zieiony.stackoverflowbrowser.ui.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import carbon.recycler.RowArrayAdapter
import carbon.recycler.RowFactory
import com.github.zieiony.stackoverflowbrowser.KeyboardUtil
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.api.StackOverflowAPI
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse
import com.github.zieiony.stackoverflowbrowser.navigation.BaseFragment
import com.github.zieiony.stackoverflowbrowser.navigation.FragmentAnnotation
import com.github.zieiony.stackoverflowbrowser.ui.ErrorFragment
import com.github.zieiony.stackoverflowbrowser.ui.question.QuestionFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_search.*
import tk.zielony.dataapi.Response

@FragmentAnnotation(layout = R.layout.fragment_search)
class SearchFragment : BaseFragment() {

    val adapter = RowArrayAdapter<Question, Question>(Question::class.java, RowFactory<Question> { parent -> QuestionRow(parent) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_recycler.layoutManager = LinearLayoutManager(context)
        search_recycler.adapter = adapter
        adapter.setOnItemClickedListener { view, question, position -> navigate(QuestionFragment.makeStep(question)) }

        if(savedInstanceState!=null)
            adapter.items = savedInstanceState.getSerializable(ITEMS) as Array<out Question>?

        search_query.setOnEditorActionListener { textView, i, keyEvent ->
            KeyboardUtil.hideKeyboard(activity)
            if (textView.text.isEmpty())
                return@setOnEditorActionListener false
            StackOverflowAPI.cancelRequests()
            searchQuestions(search_query.text.toString())
            true
        }
        search_swipeRefresh.setOnRefreshListener {
            StackOverflowAPI.cancelRequests()
            searchQuestions(search_query.text.toString())
        }
    }

    private fun searchQuestions(query: String) {
        search_swipeRefresh.isRefreshing = true
        StackOverflowAPI.searchQuestions(query)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Response<QuestionsResponse>>() {
                    override fun onNext(response: Response<QuestionsResponse>) {
                        activity!!.runOnUiThread {
                            adapter.items = response.data.items
                        }
                    }

                    override fun onError(e: Throwable) {
                        activity!!.runOnUiThread {
                            search_swipeRefresh.isRefreshing = false
                            navigate(ErrorFragment.makeStep(resources.getString(R.string.error_title_requestFailed), e.toString()))
                        }
                    }

                    override fun onComplete() {
                        activity!!.runOnUiThread {
                            search_swipeRefresh.isRefreshing = false
                        }
                    }
                })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(ITEMS, adapter.items)
    }

    override fun onStop() {
        super.onStop()
        StackOverflowAPI.cancelRequests()
    }

    companion object {
        const val CURRENT_QUERY = "query"
        const val ITEMS = "items"
    }
}

