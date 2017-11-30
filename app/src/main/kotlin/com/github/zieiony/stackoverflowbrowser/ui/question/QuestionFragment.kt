package com.github.zieiony.stackoverflowbrowser.ui.question

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import carbon.recycler.RowArrayAdapter
import carbon.recycler.RowFactory
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.api.StackOverflowAPI
import com.github.zieiony.stackoverflowbrowser.api.data.Answer
import com.github.zieiony.stackoverflowbrowser.api.data.AnswersResponse
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import com.github.zieiony.stackoverflowbrowser.navigation.BaseFragment
import com.github.zieiony.stackoverflowbrowser.navigation.FragmentAnnotation
import com.github.zieiony.stackoverflowbrowser.navigation.NavigationStep
import com.github.zieiony.stackoverflowbrowser.ui.ErrorFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_question.*
import tk.zielony.dataapi.Response
import java.io.Serializable

@FragmentAnnotation(layout = R.layout.fragment_question)
class QuestionFragment : BaseFragment() {

    val adapter = RowArrayAdapter<Serializable, Answer>(Answer::class.java, RowFactory { parent -> AnswerRow(parent) })

    init {
        adapter.addFactory(Question::class.java, { parent -> FullQuestionRow(parent) })
    }

    private lateinit var question: Question

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        question = arguments!!.get(QUESTION) as Question
        adapter.items = arrayOf(question)
        question_recycler.layoutManager = LinearLayoutManager(context)
        question_recycler.adapter = adapter

        question_swipeRefresh.setOnRefreshListener {
            searchAnswers(question.question_id!!)
        }

        searchAnswers(question.question_id!!)
    }

    private fun searchAnswers(questionId: Long) {
        question_swipeRefresh.isRefreshing = true
        StackOverflowAPI.requestAnswers(questionId)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Response<AnswersResponse>>() {
                    override fun onNext(response: Response<AnswersResponse>) {
                        activity!!.runOnUiThread {
                            adapter.items = arrayOf<Serializable>(question, *response.data.items!!)
                        }
                    }

                    override fun onError(e: Throwable) {
                        activity!!.runOnUiThread {
                            question_swipeRefresh.isRefreshing = false
                            navigate(ErrorFragment.makeStep(resources.getString(R.string.error_title_requestFailed), e.toString()))
                        }
                    }

                    override fun onComplete() {
                        activity!!.runOnUiThread {
                            question_swipeRefresh.isRefreshing = false
                        }
                    }
                })
    }

    override fun onStop() {
        super.onStop()
        StackOverflowAPI.cancelRequests()
    }

    companion object {
        private val QUESTION = "question"

        fun makeStep(question: Question): NavigationStep {
            val arguments = HashMap<String, Serializable>()
            arguments.put(QUESTION, question)
            return NavigationStep(QuestionFragment::class.java, arguments)
        }
    }
}

