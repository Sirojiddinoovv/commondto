package uz.nodir.common.model.dto.core.response.result

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime


/**
@author: Nodir
@date: 19.04.2025
@group: Meloman

 **/

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
open class DateTimRangeResult(
    open val dateFrom: LocalDateTime? = null,

    open val dateTo: LocalDateTime? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DateTimRangeResult

        if (dateFrom != other.dateFrom) return false
        if (dateTo != other.dateTo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dateFrom?.hashCode() ?: 0
        result = 31 * result + (dateTo?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "DateRangeResult(dateFrom=$dateFrom, dateTo=$dateTo)"
    }
}