package vn.edu.hcmute.example2;
import org.springframework.context.annotation.*; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.core.userdetails.*; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.web.SecurityFilterChain;
@Configuration public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean UserDetailsService userDetailsService(UserRepository users){return login->users.findByUsernameOrEmail(login,login).map(AppPrincipal::new).orElseThrow(()->new UsernameNotFoundException("Không tìm thấy tài khoản"));}
 @Bean SecurityFilterChain filter(HttpSecurity http)throws Exception{return http.authorizeHttpRequests(a->a.requestMatchers("/login","/css/**").permitAll().requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated()).formLogin(f->f.loginPage("/login").usernameParameter("login").defaultSuccessUrl("/",true).permitAll()).logout(l->l.logoutSuccessUrl("/login?logout")).build();}
}
