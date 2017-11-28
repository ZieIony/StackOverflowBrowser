package com.github.zieiony.stackoverflowbrowser.ui

import android.os.Bundle
import android.view.View
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.api.StackOverflowAPI
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse
import com.github.zieiony.stackoverflowbrowser.navigation.BaseFragment
import com.github.zieiony.stackoverflowbrowser.navigation.FragmentAnnotation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_search.*
import tk.zielony.dataapi.Response

@FragmentAnnotation(layout = R.layout.fragment_search)
class SearchFragment : BaseFragment() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_search.setOnClickListener {
            StackOverflowAPI.searchQuestions(search_query.text.toString())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<Response<QuestionsResponse>>() {
                        override fun onNext(response: Response<QuestionsResponse>) {
                            search_results.text = response.data.toString()
                        }

                        override fun onError(e: Throwable) {
                            navigate(ErrorFragment.makeStep(resources.getString(R.string.error_title_requestFailed), e.toString()))
                        }

                        override fun onComplete() {
                        }
                    })
        }
    }

    override fun onStop() {
        super.onStop()
        StackOverflowAPI.cancelRequests()
    }

}