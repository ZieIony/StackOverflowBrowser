package com.github.zieiony.stackoverflowbrowser.api

class RequestConfiguration {
    var pagesize = 20
    private val order = SortingOrder.DESCENDING
    private val sort = SortingType.CREATION
    private val site = SITE_STACKOVERFLOW

    override fun toString(): String = "pagesize=$pagesize&order=${order.value}&sort=${sort.value}&site=$site"

    companion object {
        private const val SITE_STACKOVERFLOW = "stackoverflow"
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