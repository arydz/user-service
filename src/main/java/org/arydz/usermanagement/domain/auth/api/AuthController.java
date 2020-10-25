package org.arydz.usermanagement.domain.auth.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.arydz.usermanagement.domain.auth.AuthResponse;
import org.arydz.usermanagement.domain.auth.AuthService;
import org.arydz.usermanagement.domain.auth.model.UserChangePasswordDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @GetMapping
    public AuthResponse login(@RequestHeader("user-name") String userName, @RequestHeader("password") String password) {
        return authService.login(userName, password);
    }

    @GetMapping("/forgotPassword/{userName}")
    public AuthResponse forgotPassword(@PathVariable String userName) {
        return authService.forgotPassword(userName);
    }

    @PutMapping("/resetPassword/{token}")
    public AuthResponse resetPassword(@PathVariable String token, @Valid @RequestBody UserChangePasswordDto userChangePasswordDto) {
        return authService.resetPassword(token, userChangePasswordDto);
    }
}
