package uz.nodir.common.model.enums


/**
@author: Nodir
@date: 18.04.2025
@group: Meloman

 **/

enum class ResultState(
    val message: String,
    val code: Int,
) {
    PHONE_NOT_FOUND("Phone number of card not taken or not turned on", 1022),
    SMS_NOT_SENT("Problem with sms center", 1023),
    DATA_NOT_FOUND("Data not found!", 1024),
    ENTITY_NOT_FOUND("Entity not found!", 1026),
    INTERNAL_SERVER_ERROR("In Back something went wrong", 1027),
    SQL_EXCEPTION("Database process error", 1028),
    WRONG_PARAMS("Field or value filled incorrectly!", 1029),
    THIRD_PARTY_ERROR("Third Party connection error...", 1030),
    INVALID_REQUEST("Error caused by incorrect parameters", 1031),
    TIME_OUT_ERROR("Connection time out", 1032),
    NOT_PROCESSED("The operation was not processed to completion.", 1033),
    BLOCK_OPERATION("This operation is blocked", 1034),
    NOT_IMPLEMENTED("Method or interface not implemented", 1035),
    INSUFFICIENT_FUNDS("Insufficient funds", 1037),
    LIMIT_REACHED("Attempts limit exhausted", 1038)
}