package org.arydz.usermanagement.domain.role.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Value
@Builder
public class RoleDto {

    Long id;

    @NotNull
    @NotBlank
    @Size(max = 128)
    String roleName;

    @NotNull
    Set<PermissionType> permissionTypes;
}
