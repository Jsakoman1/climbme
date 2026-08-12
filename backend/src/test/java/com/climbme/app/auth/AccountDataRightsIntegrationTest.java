package com.climbme.app.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.climbme.app.ClimbMeApplication;
import com.climbme.app.climbing.ClimbingAttemptRepository;
import com.climbme.app.training.TrainingSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ClimbMeApplication.class, properties = {
        "spring.datasource.url=jdbc:h2:mem:climbme_data_rights;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=validate"
})
@AutoConfigureMockMvc
@Transactional
class AccountDataRightsIntegrationTest {
    @Autowired private MockMvc mvc;
    @Autowired private UserAccountRepository accounts;
    @Autowired private ClimbingAttemptRepository attempts;
    @Autowired private TrainingSessionRepository training;

    @Test
    void ownerCanExportAndPermanentlyDeleteOnlyTheirOwnRecords() throws Exception {
        MockHttpSession owner = register("owner-data@example.com");
        MockHttpSession other = register("other-data@example.com");
        long ownerId = accounts.findByEmail("owner-data@example.com").orElseThrow().getId();
        long otherId = accounts.findByEmail("other-data@example.com").orElseThrow().getId();
        createAttempt(owner, "Owner route");
        createAttempt(other, "Other route");
        createTraining(owner);

        mvc.perform(get("/api/auth/export").session(owner))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=climbme-data.json"))
                .andExpect(jsonPath("$.account.email").value("owner-data@example.com"))
                .andExpect(jsonPath("$.attempts[0].routeName").value("Owner route"))
                .andExpect(jsonPath("$.trainingSessions[0].sessionType").value("HANGBOARD"))
                .andExpect(jsonPath("$.attempts.length()").value(1));

        mvc.perform(delete("/api/auth/account").session(owner).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currentPassword\":\"safe-password-123\"}"))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/auth/me").session(owner)).andExpect(status().isUnauthorized());
        org.assertj.core.api.Assertions.assertThat(accounts.findByEmail("owner-data@example.com")).isEmpty();
        org.assertj.core.api.Assertions.assertThat(attempts.findByUserIdOrderByClimbedOnDescIdDesc(ownerId)).isEmpty();
        org.assertj.core.api.Assertions.assertThat(training.findByUserIdOrderByTrainedOnDescIdDesc(ownerId)).isEmpty();
        org.assertj.core.api.Assertions.assertThat(accounts.findByEmail("other-data@example.com")).isPresent();
        org.assertj.core.api.Assertions.assertThat(attempts.findByUserIdOrderByClimbedOnDescIdDesc(otherId)).hasSize(1);
    }

    private MockHttpSession register(String email) throws Exception {
        MvcResult result = mvc.perform(post("/api/auth/register").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"safe-password-123\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        return (MockHttpSession) result.getRequest().getSession(false);
    }

    private void createAttempt(MockHttpSession session, String routeName) throws Exception {
        mvc.perform(post("/api/attempts").session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"climbedOn\":\"2026-08-12\",\"location\":\"Osp\",\"sector\":\"Misja Pec\",\"routeName\":\"" + routeName + "\",\"grade\":\"6c\",\"style\":\"REDPOINT\",\"sent\":true}"))
                .andExpect(status().isCreated());
    }

    private void createTraining(MockHttpSession session) throws Exception {
        mvc.perform(post("/api/training").session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"trainedOn\":\"2026-08-12\",\"sessionType\":\"HANGBOARD\",\"durationMinutes\":45}"))
                .andExpect(status().isCreated());
    }
}
