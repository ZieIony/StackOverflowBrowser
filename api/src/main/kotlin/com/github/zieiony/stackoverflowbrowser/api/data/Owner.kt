package com.github.zieiony.stackoverflowbrowser.api.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class Owner : Serializable {
    var display_name: String? = null
    var accept_rate: String? = null
    var user_type: String? = null
    var profile_image: String? = null
    var link: String? = null
    var reputation: String? = null
    var user_id: String? = null

    override fun toString(): String =
            "Owner(display_name=$display_name, accept_rate=$accept_rate, user_type=$user_type, " +
                    "profile_image=$profile_image, link=$link, reputation=$reputation, " +
                    "user_id=$user_id)"
}
