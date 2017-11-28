package com.github.zieiony.stackoverflowbrowser.api.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true) // I don't really need anything more than this
class AnswersResponse : Serializable {
    var items: Array<Answer>? = null
    var has_more: Boolean? = null

    override fun toString(): String =
            "AnswersResponse(items=${Arrays.toString(items)}, has_more=$has_more)"
}

