package org.arydz.usermanagement.domain.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
