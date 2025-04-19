package uz.nodir.common.model.dto.core.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.nodir.common.model.enums.ProcessStatus


/**
@author: Nodir
@date: 18.04.2025
@group: Meloman

 **/

@Serializable
sealed class ProcessData<out D> : HasResult, TimeOutResult {

    abstract val code: Int
    abstract val status: ProcessStatus

    @Serializable
    data class Success<out D>(
        val data: D
    ) : ProcessData<D>() {
        override val code: Int = ProcessStatus.SUCCESS.code
        override val status: ProcessStatus = ProcessStatus.SUCCESS
    }

    @Serializable
    data class Failure(
        val error: ActionResult
    ) : ProcessData<Nothing>() {
        override val code: Int = error.code!!
        override val status: ProcessStatus = ProcessStatus.FAILED
    }

    @Serializable
    @SerialName("timeout")
    object TimeOut : ProcessData<Nothing>() {
        override val code: Int = ProcessStatus.TIME_OUT.code
        override val status: ProcessStatus = ProcessStatus.TIME_OUT
    }

    override fun isSuccess(): Boolean = this is Success<*>
    override fun isTimeOut(): Boolean = this === TimeOut

    companion object {

        @JvmStatic
        fun <D> ok(data: D): ProcessData<D> =
            Success(data)

        @JvmStatic
        fun <D> error(code: Int, message: String): ProcessData<D> =
            Failure(ActionResult(code, message))

        @JvmStatic
        fun <D> timeout(message: String): ProcessData<D> =
            Failure(ActionResult(ProcessStatus.TIME_OUT.code, message))
    }
}