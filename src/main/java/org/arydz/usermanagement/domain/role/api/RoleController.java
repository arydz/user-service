package org.arydz.usermanagement.domain.role.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.arydz.usermanagement.domain.DeleteResponse;
import org.arydz.usermanagement.domain.auth.AuthService;
import org.arydz.usermanagement.domain.exception.UserAccessDeniedException;
import org.arydz.usermanagement.domain.exception.UserNotAuthorizedException;
import org.arydz.usermanagement.domain.role.RoleService;
import org.arydz.usermanagement.domain.role.model.RoleDto;
import org.arydz.usermanagement.domain.user.UserService;
import org.arydz.usermanagement.repository.entity.UserEntity;
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
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    AuthService authService;
    UserService userService;
    RoleService roleService;

    @PostMapping
    public RoleDto createRole(@RequestHeader("user-name") String userName, @RequestHeader("token") String token, @Valid @RequestBody RoleDto role) {
        isAuthorizedAdmin(userName, token);
        return roleService.create(role);
    }

    @PutMapping
    public RoleDto updateRole(@RequestHeader("user-name") String userName, @RequestHeader("token") String token, @Valid @RequestBody RoleDto role) {
        isAuthorizedAdmin(userName, token);
        return roleService.update(role);
    }

    @DeleteMapping("/{roleId}")
    public DeleteResponse deleteRole(@RequestHeader("user-name") String userName, @RequestHeader("token") String token, @PathVariable Long roleId) {
        isAuthorizedAdmin(userName, token);
        String message = roleService.delete(roleId);
        return DeleteResponse.builder().message(message).build();
    }

    @GetMapping
    public List<RoleDto> getRoleList(@RequestHeader("user-name") String userName, @RequestHeader("token") String token) {
        isAuthorizedAdmin(userName, token);
        return roleService.findAll();
    }

    private void isAuthorizedAdmin(String userName, String token) {

        boolean userAuthorized = authService.isUserAuthorized(userName, token);
        if (!userAuthorized) {
            throw new UserNotAuthorizedException(userName);
        }
        UserEntity userEntity = userService.findByUserName(userName);
        if (userEntity.getSuperUser().equals(Boolean.FALSE)) {
            throw new UserAccessDeniedException(userName);
        }
    }
}
