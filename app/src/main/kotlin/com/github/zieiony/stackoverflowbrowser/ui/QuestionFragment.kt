package com.github.zieiony.stackoverflowbrowser.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import carbon.recycler.RowArrayAdapter
import carbon.recycler.RowFactory
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.api.StackOverflowAPI
import com.github.zieiony.stackoverflowbrowser.api.data.Answer
import com.github.zieiony.stackoverflowbrowser.navigation.BaseFragment
import com.github.zieiony.stackoverflowbrowser.navigation.FragmentAnnotation
import kotlinx.android.synthetic.main.fragment_search.*

@FragmentAnnotation(layout = R.layout.fragment_question)
class QuestionFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RowArrayAdapter<Answer, Answer>(Answer::class.java, RowFactory<Answer> { parent -> AnswerRow(parent) })
        search_recycler.layoutManager = LinearLayoutManager(context)
        search_recycler.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        StackOverflowAPI.cancelRequests()
    }

}

