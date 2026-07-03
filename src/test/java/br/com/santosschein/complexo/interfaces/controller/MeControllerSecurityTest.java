package br.com.santosschein.complexo.interfaces.controller;

import br.com.santosschein.complexo.application.service.TokenService;
import br.com.santosschein.complexo.application.usecase.GetAuthenticatedUserProfileUseCase;
import br.com.santosschein.complexo.security.JwtAuthenticationFilter;
import br.com.santosschein.complexo.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
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
        mockMvc.perform(get("/me/profile"))
                .andExpect(status().isUnauthorized());
    }
}
