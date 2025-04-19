package uz.nodir.common.model.dto.core.request.params

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming


/**
@author: Nodir
@date: 19.04.2025
@group: Meloman

 **/

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
open class DateRange(
    open val dateFrom: String? = null,

    open val dateTo: String? = null,
) {
    override fun toString(): String {
        return "DateRange(dateFrom='$dateFrom', dateTo='$dateTo')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DateRange

        if (dateFrom != other.dateFrom) return false
        if (dateTo != other.dateTo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dateFrom.hashCode()
        result = 31 * result + dateTo.hashCode()
        return result
    }

}