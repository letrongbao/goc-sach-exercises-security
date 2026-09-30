package vn.edu.hcmute.example4;

import java.util.Locale;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;

    AuthService(UserRepository users, PasswordEncoder encoder, AuthenticationManager authenticationManager) {
        this.users = users;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    AppUser register(RegisterRequest input) {
        String email = input.email().trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email đã được sử dụng.");
        }
        return users.save(new AppUser(email, encoder.encode(input.password()), input.fullName().trim(), "USER"));
    }

    AppUser authenticate(LoginRequest input) {
        String email = input.email().trim().toLowerCase(Locale.ROOT);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, input.password()));
        return users.findByEmailIgnoreCase(email).orElseThrow();
    }
}
