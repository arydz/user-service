package org.arydz.usermanagement.domain.exception;

public class UserNameNotUniqueException extends RuntimeException {

    private static final String MESSAGE = "User Name not unique %s";

    public UserNameNotUniqueException(String args) {
        super(String.format(MESSAGE, args));
    }
}
