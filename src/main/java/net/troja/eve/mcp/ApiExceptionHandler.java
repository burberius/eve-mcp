package net.troja.eve.mcp;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.troja.eve.esi.ApiException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ApiExceptionHandler {
    public static String handleApiException(final ApiException apiException, String message) {
        log.warn(message + " {}", apiException.getMessage());
        return apiException.getResponseBody().replace("{\"error\":\"", "").replace("\"}", "");
    }
}
