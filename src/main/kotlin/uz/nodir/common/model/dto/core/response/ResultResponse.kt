package uz.nodir.common.model.dto.core.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import uz.nodir.common.model.enums.ResponseStatus
import uz.nodir.common.model.enums.ResultState


/**
@author: Nodir
@date: 18.04.2025
@group: Meloman

 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
sealed class ResultResponse<out R> {

    abstract val code: Int
    abstract val status: ResponseStatus

    val isSuccess: Boolean
        get() = this is Success<R>

    data class Success<out R>(
        @JsonProperty("result")
        val result: R
    ) : ResultResponse<R>() {
        override val code: Int = 0
        override val status: ResponseStatus = ResponseStatus.SUCCESS
    }

    data class Failure(
        @JsonProperty("error")
        val error: ActionResult
    ) : ResultResponse<Nothing>() {
        override val code: Int = error.code!!
        override val status: ResponseStatus = ResponseStatus.FAILED
    }

    fun ResultResponse<*>.toMap(): Map<String, Any?> =
        when (this) {
            is ResultResponse.Success -> mapOf(
                "code" to code,
                "status" to status,
                "result" to result
            )
            is ResultResponse.Failure -> mapOf(
                "code" to code,
                "status" to status,
                "error" to error
            )
        }

    companion object {

        @JvmStatic
        fun <R> ok(result: R): ResultResponse<R> =
            Success(result)

        @JvmStatic
        fun <R> error(code: Int, message: String): ResultResponse<R> =
            Failure(ActionResult(code, message))

        @JvmStatic
        fun <R> error(state: ResultState, message: String): ResultResponse<R> =
            Failure(ActionResult(state.code, message))
    }
}