package com.github.zieiony.stackoverflowbrowser.api

class RequestConfiguration {
    var pagesize = 20
    private val order = SortingOrder.DESCENDING
    private val sort = SortingType.CREATION
    private val site = SITE_STACKOVERFLOW
    private val filter = FILTER_DEFAULT

    override fun toString(): String = "pagesize=$pagesize&order=${order.value}&sort=${sort.value}&site=$site&filter=$filter"

    companion object {
        private const val SITE_STACKOVERFLOW = "stackoverflow"
        private const val FILTER_DEFAULT = "withbody"
    }
}

enum class SortingOrder(val value: String) {
    ASCENDING("asc"),
    DESCENDING("desc")
}

enum class SortingType(val value: String) {
    ACTIVITY("activity"),
    CREATION("creation"),
    VOTES("votes")
}