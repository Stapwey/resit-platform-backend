
package com.resitplatform.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public static ForbiddenException forbidden(String message, Object... args) {
        return new ForbiddenException(String.format(message, args));
    }

}
