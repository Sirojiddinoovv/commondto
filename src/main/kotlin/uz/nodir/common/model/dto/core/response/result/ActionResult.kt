package uz.nodir.common.model.dto.core.response.result

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable


/**
@author: Nodir
@date: 18.04.2025
@group: Meloman

 **/

@JsonIgnoreProperties(ignoreUnknown = true)
@Serializable
data class ActionResult(

    @JsonProperty("code")
    val code: Int? = null,

    @JsonProperty("message")
    val message: String? = null,
) {

    fun getTruncatedMessage(): String? {
        return message?.take(255)
    }

}