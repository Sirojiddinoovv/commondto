package uz.nodir.common.model.enums


/**
@author: Nodir
@date: 18.04.2025
@group: Meloman

 **/
enum class ProcessStatus(
    val code: Int,
) {
    SUCCESS(0),
    FAILED(1000),
    PENDING(-1),
    NEED_CONFIRM(1001),
    TIME_OUT(1003)

}