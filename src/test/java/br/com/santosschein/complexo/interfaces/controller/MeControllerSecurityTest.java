package br.com.santosschein.complexo.interfaces.controller;

import br.com.santosschein.complexo.application.service.TokenService;
import br.com.santosschein.complexo.application.usecase.GetAuthenticatedUserProfileUseCase;
import br.com.santosschein.complexo.security.JwtAuthenticationFilter;
import br.com.santosschein.complexo.security.SecurityConfig;
import br.com.santosschein.complexo.shared.exception.ApiErrorResponseWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, ApiErrorResponseWriter.class})
class MeControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetAuthenticatedUserProfileUseCase getAuthenticatedUserProfileUseCase;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void returnsUnauthorizedWhenRequestHasNoAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Sessao expirada ou token invalido."))
                .andExpect(jsonPath("$.path").value("/api/v1/users/me"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void returnsUnauthorizedWhenRequestHasInvalidToken() throws Exception {
        mockMvc.perform(get("/api/v1/users/me")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Sessao expirada ou token invalido."))
                .andExpect(jsonPath("$.path").value("/api/v1/users/me"));
    }
}
