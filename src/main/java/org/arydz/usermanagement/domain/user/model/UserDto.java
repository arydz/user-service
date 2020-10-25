package org.arydz.usermanagement.domain.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder
public class UserDto {

    Long id;

    @NotNull
    @NotBlank
    @Size(max = 128)
    String userName;

    @NotNull
    @NotBlank
    @Size(max = 64)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String password;

    /**
     * FYI: if we would also have UI applications, I would go with roleId
     */
    @NotNull
    @NotBlank
    String roleName;
}
