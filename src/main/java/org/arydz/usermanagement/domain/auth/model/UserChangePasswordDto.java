package org.arydz.usermanagement.domain.auth.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class UserChangePasswordDto {

    @NotNull
    String userName;
    @NotNull
    String oldPassword;
    @NotNull
    String newPassword;
}
