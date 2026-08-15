package com.climbme.app.auth;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.climbme.app.ClimbMeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ClimbMeApplication.class, properties = {
        "spring.datasource.url=jdbc:h2:mem:climbme_auth;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=validate"
})
@AutoConfigureMockMvc
@Transactional
class AuthIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void registrationCreatesAuthenticatedSessionAndProtectsApi() throws Exception {
        mvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());

        var registration = mvc.perform(post("/api/auth/register").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"climber@example.com\",\"password\":\"safe-password-123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("climber@example.com"))
                .andReturn();

        mvc.perform(get("/api/auth/me").session((MockHttpSession) registration.getRequest().getSession(false)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("climber@example.com"));
    }

    @Test
    void registrationRequiresCsrfAndRejectsDuplicateNormalizedEmail() throws Exception {
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"csrf@example.com\",\"password\":\"safe-password-123\"}"))
                .andExpect(status().isForbidden());

        mvc.perform(post("/api/auth/register").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"same@example.com\",\"password\":\"safe-password-123\"}"))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/auth/register").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"SAME@example.com\",\"password\":\"safe-password-123\"}"))
                .andExpect(status().isConflict())
                .andExpect(status().reason(containsString("account already exists")));
    }

    @Test
    void registrationUsesTheSharedUnicodeSingleFactorPasswordPolicy() throws Exception {
        mvc.perform(post("/api/auth/register").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"too-short@example.com\",\"password\":\"" + "🔐".repeat(14) + "\"}"))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/auth/register").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"unicode-valid@example.com\",\"password\":\"" + "🔐".repeat(15) + "\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void failedAuthenticationIsTemporarilyLimitedPerClientAddress() throws Exception {
        String blockedAddress = "198.51.100.41";
        for (int attempt = 0; attempt < 5; attempt++) {
            mvc.perform(post("/api/auth/login")
                            .with(request -> { request.setRemoteAddr(blockedAddress); return request; })
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"missing@example.com\",\"password\":\"safe-password-123\"}"))
                    .andExpect(status().isUnauthorized());
        }

        mvc.perform(post("/api/auth/login")
                        .with(request -> { request.setRemoteAddr(blockedAddress); return request; })
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"missing@example.com\",\"password\":\"safe-password-123\"}"))
                .andExpect(status().isTooManyRequests());

        mvc.perform(post("/api/auth/register")
                        .with(request -> { request.setRemoteAddr("198.51.100.42"); return request; })
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"different-client@example.com\",\"password\":\"safe-password-123\"}"))
                .andExpect(status().isCreated());
    }
}
