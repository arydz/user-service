package org.arydz.usermanagement.domain.auth;

import org.arydz.usermanagement.domain.HashingComponent;
import org.arydz.usermanagement.domain.auth.model.UserChangePasswordDto;
import org.arydz.usermanagement.domain.exception.UserNotAuthorizedException;
import org.arydz.usermanagement.domain.exception.UserNotFoundException;
import org.arydz.usermanagement.domain.role.model.PermissionType;
import org.arydz.usermanagement.domain.user.UserConverter;
import org.arydz.usermanagement.domain.user.UserService;
import org.arydz.usermanagement.domain.user.model.UserDto;
import org.arydz.usermanagement.repository.entity.RoleEntity;
import org.arydz.usermanagement.repository.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private static final String INVALID_CREDENTIALS_MESSAGE = "Provided credentials are invalid";
    private static final String RESET_PASSWORD_URL = "%s/api/auth/resetPassword/%s";

    String baseUrl;

    UserService userService;
    UserConverter userConverter;
    HashingComponent hashingComponent;

    public AuthService( @Value("${userManagement.baseUrl}") String baseUrl,
                        UserService userService,
                        UserConverter userConverter,
                        HashingComponent hashingComponent) {
        this.baseUrl = baseUrl;
        this.userService = userService;
        this.userConverter = userConverter;
        this.hashingComponent = hashingComponent;
    }

    public AuthResponse login(String userName, String password) {

        AuthResponse.AuthResponseBuilder loginResponseBuilder = AuthResponse.builder();

        String hashedPassword = hashingComponent.guavaMd5(password);
        Optional<UserEntity> optionalUserEntity = userService.findByUserNameAndHashedPassword(userName, hashedPassword);
        optionalUserEntity
                .ifPresentOrElse( entity -> loginResponseBuilder.message(generateToken(entity.getUserName())),
                () -> loginResponseBuilder.message(INVALID_CREDENTIALS_MESSAGE));

        return loginResponseBuilder.build();
    }

    public AuthResponse forgotPassword(String userName) {
        String generateToken = generateToken(userName);
        return AuthResponse.builder()
                .message(String.format(RESET_PASSWORD_URL, baseUrl, generateToken))
                .build();
    }

    public AuthResponse resetPassword(String token, UserChangePasswordDto userChangePassword) {

        AuthResponse.AuthResponseBuilder authResponseBuilder = AuthResponse.builder();
        String userName = userChangePassword.getUserName();
        if (isUserAuthorized(userName, token)) {

            String hashedOldPassword = hashingComponent.guavaMd5(userChangePassword.getOldPassword());
            Optional<UserEntity> optionalUserEntity = userService.findByUserNameAndHashedPassword(userName, hashedOldPassword);
            UserEntity userEntityFromDb = optionalUserEntity.orElseThrow(() -> new UserNotFoundException("Invalid credentials"));

            RoleEntity roleEntity = userEntityFromDb.getRoleEntity();

            UserDto userEntityToUpdate = UserDto.builder()
                    .id(userEntityFromDb.getId())
                    .userName(userName)
                    .roleName(roleEntity.getRoleName())
                    .password(userChangePassword.getNewPassword())
                    .build();

            userService.update(userEntityToUpdate);
            return authResponseBuilder.message("Password changed successfully").build();
        }

        throw new UserNotAuthorizedException(userName);
    }

    public boolean isUserAuthorized(String userName, String tokenFromRequest) {
        String tokenFromUserName = generateToken(userName);
        return tokenFromUserName.equals(tokenFromRequest);
    }

    public boolean hasPermissionGranted(String userName, PermissionType permissionType) {
        UserEntity userEntity = userService.findByUserName(userName);
        RoleEntity roleEntity = userEntity.getRoleEntity();
        return roleEntity.getPermissionTypes().contains(permissionType);
    }

    private String generateToken(String userName) {
        return hashingComponent.guavaMd5(userName);
    }
}
