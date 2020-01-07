package com.github.zieiony.stackoverflowbrowser.api.web

import com.github.zieiony.stackoverflowbrowser.api.data.AnswersResponse
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse
import io.reactivex.Observable
import javax.inject.Inject

enum class SortingOrder(val value: String) {
    ASCENDING("asc"),
    DESCENDING("desc");

    override fun toString() = value
}

enum class SortingType(val value: String) {
    ACTIVITY("activity"),
    CREATION("creation"),
    VOTES("votes");

    override fun toString() = value
}

class StackOverflowService {
    private var stackOverflowApi: StackOverflowAPI

    @Inject
    constructor(stackOverflowAPI: StackOverflowAPI) {
        this.stackOverflowApi = stackOverflowAPI
    }

    fun getQuestions(query: String, page: Int): Observable<QuestionsResponse> =
            stackOverflowApi.searchQuestions(query, page, PAGE_SIZE, SortingOrder.DESCENDING,
                    SortingType.CREATION, SITE_STACKOVERFLOW, FILTER_DEFAULT)

    fun getAnswers(questionId: Long, page: Int): Observable<AnswersResponse> =
            stackOverflowApi.requestAnswers(questionId, page, PAGE_SIZE, SortingOrder.DESCENDING,
                    SortingType.CREATION, SITE_STACKOVERFLOW, FILTER_DEFAULT)


    companion object {
        const val FIRST_PAGE = 1    // TODO: this is not obvious dependency on the stackoverflow api
        const val PAGE_SIZE = 20
        private const val SITE_STACKOVERFLOW = "stackoverflow"
        private const val FILTER_DEFAULT = "withbody"
    }
}
