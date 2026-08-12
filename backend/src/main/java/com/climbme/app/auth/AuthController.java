package com.climbme.app.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) { return Map.of("token", token.getToken()); }

    @PostMapping("/register")
    public ResponseEntity<AuthService.AccountView> register(
            @Valid @RequestBody RegistrationRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request.email(), request.password(), servletRequest, servletResponse));
    }

    @PostMapping("/login")
    public AuthService.AccountView login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        return authService.login(request.email(), request.password(), servletRequest, servletResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public AuthService.AccountView me(@AuthenticationPrincipal UserAccount account) {
        return new AuthService.AccountView(account.getId(), account.getEmail());
    }

    @PostMapping("/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserAccount account,
            @Valid @RequestBody PasswordChangeRequest request) {
        authService.changePassword(account, request.currentPassword(), request.newPassword());
        return ResponseEntity.noContent().build();
    }

    public record RegistrationRequest(@NotBlank @Email @Size(max = 254) String email,
                                      @NotBlank @Size(min = 12, max = 128) String password) { }
    public record LoginRequest(@NotBlank @Email @Size(max = 254) String email,
                               @NotBlank @Size(max = 128) String password) { }
    public record PasswordChangeRequest(@NotBlank @Size(max = 128) String currentPassword,
                                        @NotBlank @Size(min = 12, max = 128) String newPassword) { }
}
