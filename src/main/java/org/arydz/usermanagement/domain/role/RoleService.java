package org.arydz.usermanagement.domain.role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.arydz.usermanagement.domain.exception.RoleNotFoundException;
import org.arydz.usermanagement.domain.role.model.RoleDto;
import org.arydz.usermanagement.repository.entity.RoleEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

    private static final String ROLE_DELETION_MESSAGE = "Role deleted successfully";
    private static final String ROLE_NOT_NULL_MESSAGE = "Role can't be null";
    private static final String ROLE_BY_ID_EXCEPTION_MESSAGE = "Role with id %s not found";
    private static final String ROLE_BY_NAME_EXCEPTION_MESSAGE = "Role with name %s not found";

    RoleRepository roleRepository;
    RoleConverter roleConverter;

    public RoleDto create(RoleDto role) {

        return save(role);
    }

    public RoleDto update(RoleDto role) {

        if (role == null) {
            throw new IllegalArgumentException(ROLE_NOT_NULL_MESSAGE);
        }
        checkIfRoleByIdExist(role.getId());
        return save(role);
    }

    private RoleDto save(RoleDto role) {

        RoleEntity roleEntity = roleConverter.mapToEntity(role);
        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        return roleConverter.mapToDto(savedRoleEntity);
    }

    public String delete(Long roleId) {

        RoleEntity roleEntity = checkIfRoleByIdExist(roleId);
        Long countUsedRoles = roleRepository.countUsedRoles(roleEntity);
        if (countUsedRoles > 0) {
            throw new IllegalArgumentException("Can't removed used role");
        }
        roleRepository.deleteById(roleId);
        return ROLE_DELETION_MESSAGE;
    }

    private RoleEntity checkIfRoleByIdExist(Long roleId) {

        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(roleId);
        return optionalRoleEntity.orElseThrow(() -> new RoleNotFoundException(ROLE_BY_ID_EXCEPTION_MESSAGE, roleId));
    }

    public List<RoleDto> findAll() {

        List<RoleEntity> roleEntityList = roleRepository.findAll();
        return roleEntityList.stream()
                .map(roleConverter::mapToDto)
                .collect(Collectors.toList());
    }

    public RoleEntity findByName(String name) {

        Optional<RoleEntity> optionalRoleEntity = roleRepository.findByRoleName(name);
        return optionalRoleEntity.orElseThrow(() -> new RoleNotFoundException(ROLE_BY_NAME_EXCEPTION_MESSAGE, name));
    }
}
