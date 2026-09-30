package vn.edu.hcmute.example4;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class DataInitializer {
    @Bean
    CommandLineRunner seed(UserRepository users, PasswordEncoder encoder) {
        return args -> {
            if (!users.existsByEmailIgnoreCase("admin@gocsach.vn")) {
                users.save(new AppUser("admin@gocsach.vn", encoder.encode("123456"), "Lê Trọng Bảo", "ADMIN"));
            }
        };
    }
}
