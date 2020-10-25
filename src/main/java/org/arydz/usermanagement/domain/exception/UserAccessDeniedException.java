package org.arydz.usermanagement.domain.exception;

public class UserAccessDeniedException extends RuntimeException{

    public UserAccessDeniedException(String userName) {
        super(String.format("Access denied for user %s", userName));
    }
}
