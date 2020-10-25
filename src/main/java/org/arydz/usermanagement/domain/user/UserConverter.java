package org.arydz.usermanagement.domain.user;

import lombok.AllArgsConstructor;
import org.arydz.usermanagement.domain.HashingComponent;
import org.arydz.usermanagement.domain.role.model.RoleDto;
import org.arydz.usermanagement.domain.user.model.UserDto;
import org.arydz.usermanagement.repository.entity.RoleEntity;
import org.arydz.usermanagement.repository.entity.UserEntity;
import org.springframework.stereotype.Component;

/**
 * FYI: Instead of writing our own converters, we could just use an out-of-the-box solution like Orika library.
 * But such an approach (external library) can have some negative performance effects, also maintenance problems might occur.
 */
@Component
@AllArgsConstructor
public class UserConverter {

    HashingComponent hashingComponent;

    public UserEntity mapToEntity(UserDto userDto, RoleEntity roleEntity) {

        String hashedPassword = hashingComponent.guavaMd5(userDto.getPassword());

        return UserEntity.builder()
                .id(userDto.getId())
                .userName(userDto.getUserName())
                .password(hashedPassword)
                .roleEntity(roleEntity)
                .superUser(false)
                .build();
    }

    public UserDto mapToDto(UserEntity userEntity, RoleDto roleDto) {

        return UserDto.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUserName())
                .roleName(roleDto.getRoleName())
                .build();
    }

}
