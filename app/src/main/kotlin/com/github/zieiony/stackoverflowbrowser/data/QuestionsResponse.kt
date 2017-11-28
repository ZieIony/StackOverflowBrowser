package com.github.zieiony.stackoverflowbrowser.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class QuestionsResponse : Serializable {
    var quota_max: String? = null
    var items: Array<Items>? = null
    var has_more: String? = null
    var quota_remaining: String? = null

    override fun toString(): String =
            "QuestionsResponse(quota_max=$quota_max, items=${Arrays.toString(items)}, has_more=$has_more, " +
                    "quota_remaining=$quota_remaining)"

}


