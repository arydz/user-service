package org.arydz.usermanagement.domain.role.model;

import lombok.ToString;

@ToString
public enum PermissionType {

    CREATE_USER,
    UPDATE_USER,
    DELETE_USER,
    LIST_USERS
}
