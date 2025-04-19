package uz.nodir.common.utils;


import kotlin.text.Regex;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import uz.nodir.common.model.dto.core.request.params.DateRange;
import uz.nodir.common.model.dto.core.response.ProcessData;
import uz.nodir.common.model.dto.core.response.result.DateRangeResult;
import uz.nodir.common.model.enums.ResultState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: Nodir
 * @date: 19.04.2025
 * @group: Meloman
 **/


public class DateUtilsTest {

    @Test
    void testNowDate() {
        LocalDate expected = LocalDate.now(ZoneId.of("Asia/Tashkent"));
        LocalDate actual = DateUtils.nowDate();
        assertEquals(expected, actual);
    }

    @Test
    void testNowDateTime() {
        LocalDateTime expected = LocalDateTime.now(ZoneId.of("Asia/Tashkent"));
        LocalDateTime actual = DateUtils.nowDateTime();

        assertTrue(!actual.isBefore(expected.minusSeconds(1)) && !actual.isAfter(expected.plusSeconds(1)),
                "DateTime should be within one second of current time");
    }

    @Test
    void testDdMMyyyyRegexValid() {
        Regex regex = DateUtils.ddMMyyyyRegex();
        assertTrue(regex.matches("29.02.2024")); // Leap year
        assertTrue(regex.matches("31-01-2025"));
    }

    @Test
    void testDdMMyyyyRegexInvalid() {
        Regex regex = DateUtils.ddMMyyyyRegex();
        assertFalse(regex.matches("31.04.2025")); // April has 30 days
        assertFalse(regex.matches("29.02.2023")); // 2023 not leap year
    }

    @Test
    void testFormatSuccess() {
        LocalDate date = LocalDate.of(2025, 4, 19);
        ProcessData<String> result = DateUtils.format(date, "yyyy-MM-dd");
        assertNotNull(result);
        assertEquals("2025-04-19", result.getData());
    }

    @Test
    void testFormatError() {
        LocalDate date = LocalDate.of(2025, 4, 19);
        ProcessData<String> result = DateUtils.format(date, "invalid-pattern");
        assertNull(result.getData());
        assertEquals(ResultState.INCORRECT_DATA.getCode(), result.getCode());
    }

    @Test
    void testValidateDateSuccess() {
        DateRange range = new DateRange("2025-01-01", "2025-12-31");
        ProcessData<DateRangeResult> result = DateUtils.validateDate(range, "yyyy-MM-dd");
        assertNotNull(result.getData());
        DateRangeResult dr = result.getData();
        assertEquals(LocalDate.of(2025, 1, 1), dr.getDateFrom());
        assertEquals(LocalDate.of(2025, 12, 31), dr.getDateTo());
    }

    @Test
    void testValidateDateErrorOnBlank() {
        DateRange range = new DateRange("", null);
        ProcessData<DateRangeResult> result = DateUtils.validateDate(range, "yyyy-MM-dd");
        assertNull(result.getData());
        assertEquals(ResultState.INCORRECT_DATA.getCode(), result.getCode());
    }

    @Test
    void testParseToLocalDateSuccess() {
        ProcessData<LocalDate> result = DateUtils.parseToLocalDate("19.04.2025", "dd.MM.yyyy");
        assertEquals(LocalDate.of(2025, 4, 19), result.getData());
    }

    @Test
    void testParseToLocalDateError() {
        ProcessData<LocalDate> result = DateUtils.parseToLocalDate("invalid", "dd.MM.yyyy");
        assertNull(result.getData());
        assertEquals(ResultState.INCORRECT_DATA.getCode(), result.getCode());
    }

    @Test
    void testParseToDateSuccess() {
        ProcessData<Date> result = DateUtils.parseToDate("2025-04-19", "yyyy-MM-dd");
        assertNotNull(result.getData());
        Calendar cal = Calendar.getInstance();
        cal.setTime(result.getData());
        assertEquals(2025, cal.get(Calendar.YEAR));
        assertEquals(Calendar.APRIL, cal.get(Calendar.MONTH));
        assertEquals(19, cal.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    void testParseToDateError() {
        ProcessData<Date> result = DateUtils.parseToDate("not-a-date", "yyyy-MM-dd");
        assertNull(result.getData());
        assertEquals(ResultState.INCORRECT_DATA.getCode(), result.getCode());
    }

    @Test
    void testToLocalDateTime() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2025, Calendar.APRIL, 19, 10, 30, 0);
        Date date = cal.getTime();
        LocalDateTime ldt = DateUtils.toLocalDateTime(date);
        assertEquals(LocalDateTime.of(2025, 4, 19, 10, 30, 0), ldt);
    }
}
