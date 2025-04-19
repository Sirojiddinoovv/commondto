package uz.nodir.model.dto.core;


import org.junit.jupiter.api.Test;
import uz.nodir.common.model.dto.core.response.ResultResponse;
import uz.nodir.common.model.dto.core.response.result.ActionResult;
import uz.nodir.common.model.enums.ResponseStatus;
import uz.nodir.common.model.enums.ResultState;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: Nodir
 * @date: 19.04.2025
 * @group: Meloman
 **/
public class ResultResponseTest {
    @Test
    void testSuccessWithPayload() {

        String payload = "hello";

        ResultResponse<String> resp = ResultResponse.ok(payload);
     
        assertTrue(resp.isSuccess(), "isSuccess должен быть true");
        assertEquals(0, resp.getCode());
        assertEquals(ResponseStatus.SUCCESS, resp.getStatus());
        assertEquals("Операция выполнена успешно", resp.getMessage());
        assertNotNull(resp.getTraceId(), "traceId не должен быть null");
        assertNotNull(resp.getTimestamp(), "timestamp не должен быть null");
        assertEquals(payload, resp.getResult());

        // Проверяем toMap()
        Map<String, Object> map = resp.toMap();
        assertEquals(0, map.get("code"));
        assertEquals(ResponseStatus.SUCCESS, map.get("status"));
        assertEquals(payload, map.get("result"));
        assertEquals("Операция выполнена успешно", map.get("message"));
        assertInstanceOf(UUID.class, map.get("traceId"));
        assertInstanceOf(LocalDateTime.class, map.get("timestamp"));
    }

    @Test
    void testFailureWithCodeAndMessage() {
  
        int code = 42;
        String errMsg = "Что‑то пошло не так";

     
        ResultResponse<String> resp = ResultResponse.error(code, errMsg);

     
        assertFalse(resp.isSuccess(), "isSuccess должен быть false");
        assertEquals(code, resp.getCode());
        assertEquals(ResponseStatus.FAILED, resp.getStatus());
        assertEquals("Ошибка при обработке данных", resp.getMessage());
        assertNotNull(resp.getTraceId());
        assertNotNull(resp.getTimestamp());
        assertNull(resp.getResult(), "getData() для Failure должен вернуть null");

     
        ResultResponse.Failure failure = (ResultResponse.Failure) resp;
        ActionResult ar = failure.getError();
        assertEquals(code, ar.getCode());
        assertEquals(errMsg, ar.getMessage());


        Map<String, Object> map = resp.toMap();
        assertEquals(code, map.get("code"));
        assertEquals(ResponseStatus.FAILED, map.get("status"));

        Object errObj = map.get("error");
        assertInstanceOf(ActionResult.class, errObj);
        ActionResult ar2 = (ActionResult) errObj;
        assertEquals(code, ar2.getCode());
        assertEquals(errMsg, ar2.getMessage());

        assertEquals("Ошибка при обработке данных", map.get("message"));
        assertInstanceOf(UUID.class, map.get("traceId"));
        assertInstanceOf(LocalDateTime.class, map.get("timestamp"));
    }

    @Test
    void testFailureWithStateAndMessage() {

        ResultState state = ResultState.BLOCK_OPERATION; 
        String customMsg = "Бизнес‑ошибка";
        
        ResultResponse<String> resp = ResultResponse.error(state, customMsg);
     
        assertFalse(resp.isSuccess());
        assertEquals(state.getCode(), resp.getCode(), "Код должен браться из ResultState");
        assertEquals(ResponseStatus.FAILED, resp.getStatus());

        ResultResponse.Failure failure = (ResultResponse.Failure) resp;
        ActionResult ar = failure.getError();
        assertEquals(state.getCode(), ar.getCode());
        assertEquals(customMsg, ar.getMessage());
    }
}
