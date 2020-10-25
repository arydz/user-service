package org.arydz.usermanagement.domain.user.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.arydz.usermanagement.domain.DeleteResponse;
import org.arydz.usermanagement.domain.auth.AuthService;
import org.arydz.usermanagement.domain.exception.UserAccessDeniedException;
import org.arydz.usermanagement.domain.exception.UserNotAuthorizedException;
import org.arydz.usermanagement.domain.role.model.PermissionType;
import org.arydz.usermanagement.domain.user.UserService;
import org.arydz.usermanagement.domain.user.model.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    AuthService authService;
    UserService userService;

    @PostMapping
    public UserDto createUser(@RequestHeader("user-name") String userName, @RequestHeader("token") String token, @Valid @RequestBody UserDto user) {
        isAuthorized(userName, token);
        hasPermissionGranted(userName, PermissionType.CREATE_USER);
        return userService.create(user);
    }

    @PutMapping
    public UserDto updateUser(@RequestHeader("user-name") String userName, @RequestHeader("token") String token, @Valid @RequestBody UserDto user) {
        isAuthorized(userName, token);
        hasPermissionGranted(userName, PermissionType.UPDATE_USER);
        return  userService.update(user);
    }

    @DeleteMapping("/{userId}")
    public DeleteResponse deleteUser(@RequestHeader("user-name") String userName, @RequestHeader("token") String token, @PathVariable Long userId) {
        isAuthorized(userName, token);
        hasPermissionGranted(userName, PermissionType.DELETE_USER);
        String message = userService.delete(userId);
        return DeleteResponse.builder().message(message).build();
    }

    @GetMapping
    public List<UserDto> getUserList(@RequestHeader("user-name") String userName, @RequestHeader("token") String token) {
        isAuthorized(userName, token);
        hasPermissionGranted(userName, PermissionType.LIST_USERS);
        return  userService.findAll();
    }

    private void isAuthorized(String userName, String token) {
        boolean userAuthorized = authService.isUserAuthorized(userName, token);
        if (!userAuthorized) {
            throw new UserNotAuthorizedException(userName);
        }
    }

    private void hasPermissionGranted(String userName, PermissionType permissionType) {
        boolean hasPermissionGranted = authService.hasPermissionGranted(userName, permissionType);
        if (!hasPermissionGranted) {
            throw new UserAccessDeniedException(userName);
        }
    }
}
