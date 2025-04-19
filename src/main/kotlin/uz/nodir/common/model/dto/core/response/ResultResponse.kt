package uz.nodir.common.model.dto.core.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import uz.nodir.common.model.dto.core.response.result.ActionResult
import uz.nodir.common.model.enums.ResponseStatus
import uz.nodir.common.model.enums.ResultState
import uz.nodir.common.utils.DateUtils
import java.time.LocalDateTime
import java.util.*


/**
@author: Nodir
@date: 18.04.2025
@group: Meloman

 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
sealed class ResultResponse<out R> : HasResult {

    abstract val code: Int
    abstract val status: ResponseStatus
    abstract val message: String
    abstract val timestamp: LocalDateTime
    abstract val traceId: UUID


    data class Success<out R>(
        @JsonProperty("result")
        val payload: R
    ) : ResultResponse<R>() {
        override val code: Int = 0
        override val status: ResponseStatus = ResponseStatus.SUCCESS
        override val message: String = "Операция выполнена успешно"
        override val traceId: UUID = UUID.randomUUID()
        override fun isSuccess() = true

        override val timestamp: LocalDateTime = DateUtils.nowDateTime()

    }

    data class Failure(
        @JsonProperty("error")
        val error: ActionResult
    ) : ResultResponse<Nothing>() {
        override val code: Int = error.code!!
        override val status: ResponseStatus = ResponseStatus.FAILED
        override val message: String = "Ошибка при обработке данных"
        override val traceId: UUID = UUID.randomUUID()
        override val timestamp: LocalDateTime = DateUtils.nowDateTime()
        override fun isSuccess() = false
    }

    fun toMap(): Map<String, Any?> = when (this) {
        is Success<*> -> mapOf(
            "code"     to code,
            "status"   to status,
            "result"   to payload,
            "message"  to message,
            "traceId"  to traceId,
            "timestamp" to timestamp
        )
        is Failure   -> mapOf(
            "code"     to code,
            "status"   to status,
            "error"    to error,
            "message"  to message,
            "traceId"  to traceId,
            "timestamp" to timestamp
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

    fun getResult(): R? = when (this) {
        is ResultResponse.Success<R> -> payload
        else -> null
    }
}