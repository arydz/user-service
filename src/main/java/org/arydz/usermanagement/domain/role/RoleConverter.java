package org.arydz.usermanagement.domain.role;

import org.arydz.usermanagement.domain.role.model.RoleDto;
import org.arydz.usermanagement.repository.entity.RoleEntity;
import org.springframework.stereotype.Component;

/**
 * FYI: Instead of writing our own converters, we could just use an out-of-the-box solution like Orika library.
 * But such an approach (external library) can have some negative performance effects, also maintenance problems might occur.
 */
@Component
public class RoleConverter {

    public RoleEntity mapToEntity(RoleDto roleDto) {
        return RoleEntity.builder()
                .id(roleDto.getId())
                .roleName(roleDto.getRoleName())
                .permissionTypes(roleDto.getPermissionTypes())
                .build();
    }

    public RoleDto mapToDto(RoleEntity roleEntity) {
        return RoleDto.builder()
                .id(roleEntity.getId())
                .roleName(roleEntity.getRoleName())
                .permissionTypes(roleEntity.getPermissionTypes())
                .build();
    }
}
