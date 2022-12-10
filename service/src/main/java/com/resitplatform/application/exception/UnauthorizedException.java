 
package com.resitplatform.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public static UnauthorizedException unauthorized(String message, Object... args) {
        return new UnauthorizedException(String.format(message, args));
    }

}
