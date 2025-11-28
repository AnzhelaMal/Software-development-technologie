package ua.kpi.ia33.shellweb.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kpi.ia33.shellweb.service.AuthService;
import ua.kpi.ia33.shellweb.service.TokenService;

@RestController
@RequestMapping("/api")
public class AuthRestController {

    private final AuthService authService;
    private final TokenService tokenService;

    public AuthRestController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.authenticate(request.getEmail(), request.getPassword())
                .map(user -> {
                    String token = tokenService.issueTokenFor(user);
                    return ResponseEntity.ok(new LoginResponse(token));
                })
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .build());
    }
}
