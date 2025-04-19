package uz.nodir.model.dto.core;


import org.junit.jupiter.api.Test;
import uz.nodir.common.model.dto.core.response.ProcessData;
import uz.nodir.common.model.dto.core.response.result.ActionResult;
import uz.nodir.common.model.enums.ProcessStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: Nodir
 * @date: 19.04.2025
 * @group: Meloman
 **/
public class ProcessDataTest {
    @Test
    void testOk() {

        String payload = "payload";

        ProcessData<String> pd = ProcessData.ok(payload);

        assertTrue(pd.isSuccess(), "ok() должен быть успешным");
        assertFalse(pd.isTimeOut(), "ok() не должен быть таймаутом");
        assertEquals(ProcessStatus.SUCCESS.getCode(), pd.getCode());
        assertEquals(ProcessStatus.SUCCESS, pd.getStatus());
        assertEquals(payload, pd.getData(), "getData() должен вернуть payload");
    }

    @Test
    void testError() {

        int code = 500;
        String msg = "Internal error";

        ProcessData<String> pd = ProcessData.error(code, msg);

        assertFalse(pd.isSuccess(), "error() не должен быть успешным");
        assertFalse(pd.isTimeOut(), "error() не должен быть таймаутом");
        assertEquals(code, pd.getCode());
        assertEquals(ProcessStatus.FAILED, pd.getStatus());
        assertNull(pd.getData(), "getData() для ошибки должен вернуть null");

        assertInstanceOf(ProcessData.Failure.class, pd, "Должен быть экземпляр Failure");
        ActionResult ar = ((ProcessData.Failure) pd).getError();
        assertEquals(code, ar.getCode());
        assertEquals(msg, ar.getMessage());
    }

    @Test
    void testTimeoutCompanion() {
        
        String timeoutMsg = "Operation timed out";

        ProcessData<String> pd = ProcessData.timeout(timeoutMsg);

        assertFalse(pd.isSuccess(), "timeout() не должен быть успешным");
        assertFalse(pd.isTimeOut(), "timeout() из companion возвращает Failure, не объект TimeOut");
        assertEquals(ProcessStatus.TIME_OUT.getCode(), pd.getCode());
        assertEquals(ProcessStatus.FAILED, pd.getStatus());

        ActionResult ar = ((ProcessData.Failure) pd).getError();
        assertEquals(ProcessStatus.TIME_OUT.getCode(), ar.getCode());
        assertEquals(timeoutMsg, ar.getMessage());
    }

    @Test
    void testTimeOutObject() {

        ProcessData<?> pd = ProcessData.TimeOut.INSTANCE;

        assertFalse(pd.isSuccess(), "TimeOut объект не является успешным");
        assertTrue(pd.isTimeOut(), "TimeOut объект должен быть таймаутом");
        assertEquals(ProcessStatus.TIME_OUT.getCode(), pd.getCode());
        assertEquals(ProcessStatus.TIME_OUT, pd.getStatus());
        assertNull(pd.getData(), "getData() для TimeOut должен вернуть null");
    }
}
