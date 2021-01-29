package com.hkmc.behavioralpatternanalysis.common.exception;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class GlobalExternalException extends RuntimeException {

    private final int statusCode;
    private final String body;

    public GlobalExternalException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.body = StringUtils.EMPTY;
    }

    public GlobalExternalException(final int statusCode, final String body) {
        super(checkStatusCode(statusCode));
        this.statusCode = statusCode;
        this.body = body;
    }

    private static String checkStatusCode(final int statusCode) {
        return (statusCode < HttpStatus.OK.value() && HttpStatus.resolve(statusCode) == null) ? HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() : HttpStatus.valueOf(statusCode).getReasonPhrase();
    }


}
