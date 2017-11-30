package com.github.zieiony.stackoverflowbrowser.api.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class QuestionsResponse : Serializable {
    var items: Array<Question>? = null
    var has_more: Boolean? = null

    override fun toString(): String =
            "QuestionsResponse(items=${Arrays.toString(items)}, has_more=$has_more)"

}


