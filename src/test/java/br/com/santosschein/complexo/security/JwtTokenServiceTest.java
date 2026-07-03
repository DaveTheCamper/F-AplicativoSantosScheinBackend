package br.com.santosschein.complexo.security;

import br.com.santosschein.complexo.infrastructure.config.AppProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenServiceTest {

    private final JwtTokenService tokenService = new JwtTokenService(new AppProperties(
            new AppProperties.Jwt("test-secret-with-enough-length-for-hmac-signing", 3600),
            new AppProperties.Reservation(60, 2)
    ));

    @Test
    void generatesValidJwtWithUserIdAndEmailClaims() {
        String token = tokenService.generateAccessToken(123L, "joao@email.com");

        assertThat(token).isNotBlank();
        assertThat(tokenService.isValid(token)).isTrue();
        assertThat(tokenService.extractUserId(token)).isEqualTo(123L);
        assertThat(tokenService.extractEmail(token)).isEqualTo("joao@email.com");
    }

    @Test
    void rejectsInvalidJwt() {
        assertThat(tokenService.isValid("invalid.token.value")).isFalse();
    }
}
