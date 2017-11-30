package com.github.zieiony.stackoverflowbrowser.api.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class Question : Serializable {
    var creation_date: Long? = null
    var tags: Array<String>? = null
    var title: String? = null
    var link: String? = null
    var last_edit_date: Long? = null
    var score: Int? = null
    var answer_count: Int? = null
    var owner: Owner? = null
    var last_activity_date: Long? = null
    var question_id: Long? = null
    var view_count: Int? = null
    var body: String? = null

    @JsonProperty("is_answered")
    var is_answered: Boolean? = null

    override fun toString(): String =
            "Question(creation_date=$creation_date, tags=${Arrays.toString(tags)}, title=$title, " +
                    "link=$link, last_edit_date=$last_edit_date, score=$score, answer_count=$answer_count, " +
                    "owner=$owner, last_activity_date=$last_activity_date, question_id=$question_id, " +
                    "view_count=$view_count, is_answered=$is_answered, body=$body)"
}

