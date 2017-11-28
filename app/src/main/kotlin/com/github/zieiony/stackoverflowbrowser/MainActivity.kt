package com.github.zieiony.stackoverflowbrowser

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.zieiony.stackoverflowbrowser.api.StackOverflowAPI
import com.github.zieiony.stackoverflowbrowser.api.data.AnswersResponse
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_main.*
import tk.zielony.dataapi.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        test.setOnClickListener {
            StackOverflowAPI.requestAnswers(15674).subscribeWith(object : DisposableObserver<Response<AnswersResponse>>() {
                override fun onNext(response: Response<AnswersResponse>) {
                    Log.e("test", response.data.toString())
                }

                override fun onError(e: Throwable) {
                    Log.e("test", "error", e)
                }

                override fun onComplete() {
                    Log.e("test", "complete")
                }
            })
        }
    }

    override fun onStop() {
        super.onStop()
        StackOverflowAPI.cancelRequests()
    }
}

