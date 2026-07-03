package br.com.santosschein.complexo.interfaces.controller;

import br.com.santosschein.complexo.application.usecase.auth.LoginUseCase;
import br.com.santosschein.complexo.application.usecase.auth.RegisterUserUseCase;
import br.com.santosschein.complexo.shared.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class AuthControllerValidationTest {

    private final LoginUseCase loginUseCase = mock(LoginUseCase.class);
    private final RegisterUserUseCase registerUserUseCase = mock(RegisterUserUseCase.class);
    private final MockMvc mockMvc = standaloneSetup(new AuthController(loginUseCase, registerUserUseCase))
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    @Test
    void rejectsRegistrationWithInvalidData() throws Exception {
        String body = """
                {
                  "firstName": "",
                  "lastName": "",
                  "birthDate": null,
                  "document": "",
                  "email": "email-invalido",
                  "password": ""
                }
                """;

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/api/v1/auth/register"));

        verifyNoInteractions(registerUserUseCase);
    }
}
