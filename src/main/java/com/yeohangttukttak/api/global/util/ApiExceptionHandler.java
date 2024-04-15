package com.yeohangttukttak.api.global.util;

import com.yeohangttukttak.api.global.common.ApiError;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static com.yeohangttukttak.api.global.util.HttpMessageSerializer.serialize;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiExceptionHandler {

    private final MessageSource messageSource;

    public void handle(RuntimeException e, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        Locale locale = httpRequest.getLocale();
        ApiErrorCode errorCode = ApiErrorCode.INTERNAL_SERVER_ERROR;
        String message = messageSource.getMessage(errorCode.name(), null, locale);

        if (e instanceof ApiException) {
            errorCode = ((ApiException) e).getErrorCode();

            if (ApiErrorCode.INVALIDED_AUTHORIZATION.equals(errorCode)) {

                log.error("""
                            [JWT_AUTH_FILTER] Caught! Credentials has been forged
                                - Uri: {}
                                - Host: {}
                                - User-Agent: {}""",
                        httpRequest.getRequestURI(),
                        httpRequest.getRemoteAddr(),
                        httpResponse.getHeader("User-Agent"));

            }

            message = messageSource.getMessage(errorCode.name(), null, httpRequest.getLocale());

        }

        serialize(httpResponse, errorCode.getStatus().value(), new ApiError(
                errorCode, message, null));
    }

}
