package org.arydz.usermanagement.domain.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
