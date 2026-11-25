package ru.student.grants.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.student.grants.dto.AuthResponse;
import ru.student.grants.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse response = authService.login(request.getUsername(), request.getPassword(), request.getDeviceId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequestDto request) {
        AuthResponse response = authService.register(request.getUsername(), request.getPassword(), request.getDeviceId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        AuthResponse response = authService.refreshTokens(request.getRefreshToken(), request.getDeviceId());
        return ResponseEntity.ok(response);
    }
}

@Data
class LoginRequest {
    @NotBlank private String username;
    @NotBlank private String password;
    private String deviceId;
}

@Data
class RegisterRequestDto {
    @NotBlank private String username;
    @NotBlank private String password;
    private String deviceId;
}

@Data
class RefreshRequest {
    @NotBlank private String refreshToken;
    private String deviceId;
}