package vn.edu.hcmute.example4;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class NimbusJwtService {
    private final byte[] secret;
    private final Duration lifetime;
    private final String issuer;
    private final Clock clock;

    @Autowired
    NimbusJwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expiration-time:3600000}") long expirationMillis,
            @Value("${security.jwt.issuer:goc-sach-exercises}") String issuer) {
        this(secret.getBytes(StandardCharsets.UTF_8), Duration.ofMillis(expirationMillis), issuer, Clock.systemUTC());
    }

    NimbusJwtService(byte[] secret, Duration lifetime, String issuer, Clock clock) {
        if (secret.length < 32) {
            throw new IllegalArgumentException("Khóa JWT phải có ít nhất 32 byte.");
        }
        this.secret = secret.clone();
        this.lifetime = lifetime;
        this.issuer = issuer;
        this.clock = clock;
    }

    String generateToken(AppUser user) {
        Instant now = clock.instant();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .subject(user.getEmail())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(lifetime)))
                .jwtID(UUID.randomUUID().toString())
                .claim("roles", List.of("ROLE_" + user.getRole()))
                .build();
        SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        try {
            jwt.sign(new MACSigner(secret));
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new IllegalStateException("Không thể tạo mã đăng nhập.", e);
        }
    }

    VerifiedToken verify(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            if (!JWSAlgorithm.HS256.equals(jwt.getHeader().getAlgorithm()) || !jwt.verify(new MACVerifier(secret))) {
                throw new InvalidTokenException("Mã đăng nhập không hợp lệ.");
            }
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            if (!issuer.equals(claims.getIssuer()) || claims.getSubject() == null) {
                throw new InvalidTokenException("Mã đăng nhập không hợp lệ.");
            }
            Date expiresAt = claims.getExpirationTime();
            if (expiresAt == null || !expiresAt.toInstant().isAfter(clock.instant())) {
                throw new InvalidTokenException("Mã đăng nhập đã hết hạn.");
            }
            return new VerifiedToken(claims.getSubject(), claims.getStringListClaim("roles"));
        } catch (ParseException | JOSEException e) {
            throw new InvalidTokenException("Mã đăng nhập không hợp lệ.", e);
        }
    }

    long getExpirationTime() { return lifetime.toMillis(); }

    record VerifiedToken(String subject, List<String> roles) {}

    static class InvalidTokenException extends RuntimeException {
        InvalidTokenException(String message) { super(message); }
        InvalidTokenException(String message, Throwable cause) { super(message, cause); }
    }
}
