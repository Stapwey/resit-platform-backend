 
package com.resitplatform.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

    public static BadRequestException badRequest(String message, Object... args) {
        return new BadRequestException(String.format(message, args));
    }

}
