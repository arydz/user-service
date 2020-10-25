package org.arydz.usermanagement.domain.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.arydz.usermanagement.domain.exception.UserNameNotUniqueException;
import org.arydz.usermanagement.domain.role.RoleConverter;
import org.arydz.usermanagement.domain.role.RoleService;
import org.arydz.usermanagement.domain.role.model.RoleDto;
import org.arydz.usermanagement.domain.user.model.UserDto;
import org.arydz.usermanagement.domain.exception.UserNotFoundException;
import org.arydz.usermanagement.repository.entity.RoleEntity;
import org.arydz.usermanagement.repository.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    private static final String USER_DELETION_MESSAGE = "User deleted successfully";
    private static final String USER_NOT_NULL_MESSAGE = "User can't be null";
    private static final String USER_BY_ID_EXCEPTION_MESSAGE = "User with id %s not found";

    RoleService roleService;
    UserRepository userRepository;
    UserConverter userConverter;
    RoleConverter roleConverter;

    public UserDto create(UserDto user) {

        validateIfUserNameIsUnique(user);

        RoleEntity roleEntity = getRoleEntity(user);
        return save(user, roleEntity);
    }

    private void validateIfUserNameIsUnique(UserDto user) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserName(user.getUserName());
        boolean exist = optionalUserEntity.isPresent();
        if (exist) {
            throw new UserNameNotUniqueException(user.getUserName());
        }
    }

    private RoleEntity getRoleEntity(UserDto user) {

        String roleName = user.getRoleName();
        return roleService.findByName(roleName);
    }

    public UserDto update(UserDto user) {

        if (user == null) {
            throw new IllegalArgumentException(USER_NOT_NULL_MESSAGE);
        }

        validateIfUserNameWithIdIsUnique(user);

        checkIfUserByIdExist(user.getId());
        RoleEntity roleEntity = getRoleEntity(user);

        return save(user, roleEntity);
    }

    private void validateIfUserNameWithIdIsUnique(UserDto user) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByIdAndUserName(user.getId(), user.getUserName());
        boolean exist = optionalUserEntity.isPresent();
        if (!exist) {
            throw new UserNameNotUniqueException(user.getUserName());
        }
    }

    private UserDto save(UserDto user, RoleEntity roleEntity) {

        UserEntity userEntity = userConverter.mapToEntity(user, roleEntity);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        RoleDto role = roleConverter.mapToDto(userEntity.getRoleEntity());

        return userConverter.mapToDto(savedUserEntity, role);
    }

    public String delete(Long userId) {

        UserEntity userEntity = checkIfUserByIdExist(userId);
        Boolean superUser = userEntity.getSuperUser();
        if (superUser) {
            throw new IllegalArgumentException("Can't delete super user");
        }
        userRepository.deleteById(userId);
        return USER_DELETION_MESSAGE;
    }

    private UserEntity checkIfUserByIdExist(Long userId) {

        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        return optionalUserEntity.orElseThrow(() -> new UserNotFoundException(USER_BY_ID_EXCEPTION_MESSAGE, userId));
    }

    public List<UserDto> findAll() {

        List<UserEntity> userEntityList = userRepository.findAll();
        return userEntityList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserDto convertToDto(UserEntity userEntity) {

        RoleDto roleDto = roleConverter.mapToDto(userEntity.getRoleEntity());
        return userConverter.mapToDto(userEntity, roleDto);
    }

    public UserEntity findByUserName(String userName) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserName(userName);
        return optionalUserEntity.orElseThrow(() -> new UserNotFoundException(userName));
    }

    public Optional<UserEntity> findByUserNameAndHashedPassword(String userName, String hashedPassword) {
        return userRepository.findByUserNameAndPassword(userName, hashedPassword);
    }
}
