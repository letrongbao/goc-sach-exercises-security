package vn.edu.hcmute.example1;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
    @Bean UserDetailsService userDetailsService(UserRepository users){
        return email -> users.findByEmail(email).map(u -> org.springframework.security.core.userdetails.User.withUsername(u.getEmail()).password(u.getPassword()).roles(u.getRole()).build())
            .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));
    }
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(a -> a.requestMatchers("/login","/css/**").permitAll().anyRequest().authenticated())
            .formLogin(f -> f.loginPage("/login").usernameParameter("email").defaultSuccessUrl("/",true).permitAll())
            .logout(l -> l.logoutSuccessUrl("/login?logout")).build();
    }
}
