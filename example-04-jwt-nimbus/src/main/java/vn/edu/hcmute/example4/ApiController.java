package vn.edu.hcmute.example4;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ApiController {
    private final AuthService authService;
    private final NimbusJwtService jwtService;
    private final UserRepository users;

    ApiController(AuthService authService, NimbusJwtService jwtService, UserRepository users) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.users = users;
    }

    @PostMapping("/auth/signup")
    ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(authService.register(input)));
    }

    @PostMapping("/auth/login")
    LoginResponse login(@Valid @RequestBody LoginRequest input) {
        AppUser user = authService.authenticate(input);
        return new LoginResponse(jwtService.generateToken(user), jwtService.getExpirationTime());
    }

    @GetMapping("/users/me")
    UserResponse me(Authentication authentication) {
        return UserResponse.from(users.findByEmailIgnoreCase(authentication.getName()).orElseThrow());
    }

    @GetMapping("/users")
    List<UserResponse> all() {
        return users.findAll().stream().map(UserResponse::from).toList();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Map<String, Object>> duplicate(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "title", "Dữ liệu chưa hợp lệ",
                "detail", exception.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<Map<String, Object>> loginFailed() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "status", 401,
                "title", "Đăng nhập không thành công",
                "detail", "Email hoặc mật khẩu chưa đúng."));
    }
}
