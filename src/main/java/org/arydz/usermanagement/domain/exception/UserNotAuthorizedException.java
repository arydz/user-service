package org.arydz.usermanagement.domain.exception;

public class UserNotAuthorizedException extends RuntimeException {
    private static final String MESSAGE = "Permission denied for user: %s";
    public UserNotAuthorizedException(String userName) {
        super(String.format(MESSAGE, userName));
    }
}
