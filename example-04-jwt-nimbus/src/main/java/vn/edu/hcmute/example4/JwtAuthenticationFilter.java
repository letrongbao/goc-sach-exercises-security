package vn.edu.hcmute.example4;

import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final NimbusJwtService jwtService;
    private final UserRepository users;
    private final ObjectMapper objectMapper;

    JwtAuthenticationFilter(NimbusJwtService jwtService, UserRepository users, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.users = users;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        try {
            var verified = jwtService.verify(header.substring(7));
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                AppUser user = users.findByEmailIgnoreCase(verified.subject())
                        .orElseThrow(() -> new NimbusJwtService.InvalidTokenException("Tài khoản không còn tồn tại."));
                var authorities = verified.roles().stream().map(SimpleGrantedAuthority::new).toList();
                var authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        } catch (NimbusJwtService.InvalidTokenException e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            objectMapper.writeValue(response.getWriter(), Map.of(
                    "status", 401,
                    "title", "Không thể xác thực",
                    "detail", e.getMessage()));
        }
    }
}
