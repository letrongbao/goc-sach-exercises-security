package vn.edu.hcmute.example4;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NimbusJwtServiceTests {
    private static final byte[] SECRET = "22110106-LeTrongBao-GocSach-JWT-Nimbus-2026".getBytes(StandardCharsets.UTF_8);

    @Test
    void tokenContainsSubjectAndRole() {
        Clock clock = Clock.fixed(Instant.parse("2026-09-30T03:00:00Z"), ZoneOffset.UTC);
        NimbusJwtService service = new NimbusJwtService(SECRET, Duration.ofHours(1), "test", clock);
        AppUser user = new AppUser("bao@example.com", "encoded", "Lê Trọng Bảo", "USER");

        var token = service.verify(service.generateToken(user));

        assertEquals("bao@example.com", token.subject());
        assertEquals(java.util.List.of("ROLE_USER"), token.roles());
    }

    @Test
    void expiredTokenIsRejected() {
        Instant issuedAt = Instant.parse("2026-09-30T03:00:00Z");
        NimbusJwtService issuer = new NimbusJwtService(SECRET, Duration.ofMinutes(5), "test", Clock.fixed(issuedAt, ZoneOffset.UTC));
        String token = issuer.generateToken(new AppUser("bao@example.com", "encoded", "Lê Trọng Bảo", "USER"));
        NimbusJwtService verifier = new NimbusJwtService(SECRET, Duration.ofMinutes(5), "test", Clock.fixed(issuedAt.plusSeconds(301), ZoneOffset.UTC));

        assertThrows(NimbusJwtService.InvalidTokenException.class, () -> verifier.verify(token));
    }
}
