package uz.nodir.common.utils

import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import uz.nodir.common.exception.InvalidDataException
import uz.nodir.common.model.dto.core.request.params.DateRange
import uz.nodir.common.model.dto.core.response.ProcessData
import uz.nodir.common.model.dto.core.response.result.DateRangeResult
import uz.nodir.common.model.enums.ResultState
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
@author: Nodir
@date: 19.04.2025
@group: Meloman

 **/


object DateUtils {

    private val log = LoggerFactory.getLogger(this::class.java)
    private const val ZONE_ID = "Asia/Tashkent"

    @JvmStatic
    fun nowDateTime(): LocalDateTime = LocalDateTime.now(ZoneId.of(ZONE_ID))

    @JvmStatic
    fun nowDate(): LocalDate = LocalDate.now(ZoneId.of(ZONE_ID))

    /**
     * Regex matching dd.MM.yyyy (with "/", "-" or "." separators) and leap years
     */
    @JvmStatic
    fun ddMMyyyyRegex(): Regex = Regex(
        """
        ^(?:(?:31([/\-.])(?:0?[13578]|1[02]))\1|
        (?:(?:29|30)([/\-.])(?:0?[13-9]|1[0-2])\2))
        (?:(?:1[6-9]|[2-9]\d)?\d{2})$|
        ^(?:29([/\-.])0?2\3(?:(?:(?:1[6-9]|[2-9]\d)?
        (?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|
        ^(?:0?[1-9]|1\d|2[0-8])([/\-.])(?:0?[1-9]|1[0-2])\4
        (?:(?:1[6-9]|[2-9]\d)?\d{2})
        """.trimIndent().replace("\n", "")
    )

    @JvmStatic
    fun format(localDate: LocalDate, pattern: String): ProcessData<String> {
        log.info("Parsing date: $localDate by $pattern")
        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            val formatted = formatter.format(localDate)
            log.info("Formatted date: '$formatted' with pattern: '$pattern'")
            ProcessData.ok(formatted)
        } catch (e: Exception) {
            val message = "Error formatting date $localDate by pattern '$pattern': with message: ${e.message}"
            log.error(message, e)
            ProcessData.error(ResultState.INCORRECT_DATA.code, message)
        }
    }

    @JvmStatic
    fun validateDate(dateRange: DateRange, pattern: String): ProcessData<DateRangeResult> {
        log.info("Parsing date range: $dateRange by $pattern")

        if (dateRange.dateFrom.isNullOrBlank() || dateRange.dateTo.isNullOrBlank()) {
            val msg = "Start or end date is blank: from='${dateRange.dateFrom}', to='${dateRange.dateTo}'"
            log.error(msg)
            return ProcessData.error(ResultState.INCORRECT_DATA.code, msg)
        }

        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            val start = LocalDate.parse(dateRange.dateFrom!!, formatter)
            val end = LocalDate.parse(dateRange.dateTo!!, formatter)
            val result = DateRangeResult(start, end)
            log.info("Validated date range: $result")
            ProcessData.ok(result)
        } catch (e: DateTimeException) {
            val message =
                "Error parsing date range '${dateRange.dateFrom}' with '${dateRange.dateTo}' by pattern: '$pattern' with message: ${e.message}"
            log.error(message, e)
            ProcessData.error(ResultState.INCORRECT_DATA.code, message)
        }
    }

    @JvmStatic
    fun parseToLocalDate(dateStr: String, pattern: String): ProcessData<LocalDate> {
        log.info("Parsing date from string: $dateStr by $pattern")

        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            val parsed = LocalDate.parse(dateStr, formatter)
            log.info("Parsed LocalDate: $parsed from '$dateStr' using pattern '$pattern'")
            ProcessData.ok(parsed)
        } catch (e: DateTimeException) {
            val message = "Error parsing LocalDate from '$dateStr' by pattern: '$pattern' with message: ${e.message}"
            log.error(message, e)
            ProcessData.error(ResultState.INCORRECT_DATA.code, message)
        }
    }

    @JvmStatic
    fun parseToDate(dateStr: String, pattern: String): ProcessData<Date> {
        log.info("Parsing date from string: $dateStr by $pattern")
        return try {
            val sdf = SimpleDateFormat(pattern)
            val parsed = sdf.parse(dateStr)
            log.info("Parsed Date: $parsed from '$dateStr' using pattern '$pattern'")
            ProcessData.ok(parsed)
        } catch (e: ParseException) {
            val message = "Error parsing Date from '$dateStr' with pattern: '$pattern' with message: ${e.message}"
            log.error(message, e)
            ProcessData.error(ResultState.INCORRECT_DATA.code, message)
        }
    }

    @JvmStatic
    fun toLocalDateTime(date: Date): LocalDateTime {
        val result = date.toInstant()
            .atZone(ZoneId.of(ZONE_ID))
            .toLocalDateTime()
        log.info("Converted Date '$date' to LocalDateTime '$result'")
        return result
    }

}
