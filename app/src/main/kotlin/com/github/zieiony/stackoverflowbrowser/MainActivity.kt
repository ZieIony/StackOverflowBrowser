package com.github.zieiony.stackoverflowbrowser

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.zieiony.stackoverflowbrowser.data.QuestionsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_main.*
import tk.zielony.dataapi.CacheStrategy
import tk.zielony.dataapi.Configuration
import tk.zielony.dataapi.Response
import tk.zielony.dataapi.WebAPI

class MainActivity : AppCompatActivity() {

    private lateinit var webAPI: WebAPI
    private var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val configuration = Configuration()
        configuration.cacheStrategy = CacheStrategy.NONE
        webAPI = WebAPI("https://api.stackexchange.com/2.2", configuration)

        test.setOnClickListener {
            disposables.add(webAPI.get("/questions?pagesize=20&order=desc&sort=activity&site=stackoverflow", QuestionsResponse::class.java)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<Response<QuestionsResponse>>() {
                        override fun onNext(response: Response<QuestionsResponse>) {
                            Log.e("test", response.data.toString())
                        }

                        override fun onError(e: Throwable) {
                            Log.e("test", "error", e)
                        }

                        override fun onComplete() {
                            Log.e("test", "complete")
                        }
                    }))
        }
    }

    override fun onStop() {
        super.onStop()
        webAPI.cancelRequests()
    }
}

