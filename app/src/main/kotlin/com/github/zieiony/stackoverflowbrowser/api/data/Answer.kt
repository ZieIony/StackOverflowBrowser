package com.github.zieiony.stackoverflowbrowser.api.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class Answer : Serializable {
    var creation_date: Long? = null
    var is_accepted: Boolean? = null
    var answer_id: Long? = null
    var score: Int? = null
    var owner: Owner? = null
    var last_activity_date: Long? = null
    var question_id: Long? = null

    override fun toString(): String =
            "Answer(creation_date=$creation_date, is_accepted=$is_accepted, answer_id=$answer_id, " +
                    "score=$score, owner=$owner, last_activity_date=$last_activity_date, " +
                    "question_id=$question_id)"
}