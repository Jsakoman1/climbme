package com.climbme.app.climbing;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ClimbMeApplication.class, properties = {
        "spring.datasource.url=jdbc:h2:mem:climbme_attempts;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa", "spring.datasource.password=", "spring.jpa.hibernate.ddl-auto=validate"
})
@AutoConfigureMockMvc
@Transactional
class ClimbingAttemptIntegrationTest {
    @Autowired private MockMvc mvc;

    @Test
    void ownerCanManageTheirAttemptButAnotherAccountCannotReadOrDeleteIt() throws Exception {
        MockHttpSession owner = register("owner@example.com");
        MockHttpSession other = register("other@example.com");

        MvcResult created = mvc.perform(post("/api/attempts").session(owner).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(attempt("7a+", "REDPOINT", true)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.grade").value("7a+"))
                .andExpect(jsonPath("$.attemptNumber").value(1))
                .andReturn();
        long id = Long.parseLong(created.getResponse().getContentAsString().replaceAll(".*\\\"id\\\":(\\d+).*", "$1"));

        mvc.perform(get("/api/attempts").session(owner)).andExpect(status().isOk()).andExpect(jsonPath("$[0].routeName").value("La Testa"));
        mvc.perform(put("/api/attempts/{id}", id).session(other).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(attempt("7b", "REDPOINT", true))).andExpect(status().isNotFound());
        mvc.perform(delete("/api/attempts/{id}", id).session(other).with(csrf())).andExpect(status().isNotFound());
        mvc.perform(delete("/api/attempts/{id}", id).session(owner).with(csrf())).andExpect(status().isNoContent());
    }

    @Test
    void projectCannotBeClaimedAsASendAndGradeMustBeFromCatalog() throws Exception {
        MockHttpSession owner = register("validation@example.com");
        mvc.perform(post("/api/attempts").session(owner).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(attempt("10z", "PROJECT", true)))
                .andExpect(status().isBadRequest());
    }

    private MockHttpSession register(String email) throws Exception {
        MvcResult result = mvc.perform(post("/api/auth/register").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"safe-password-123\"}"))
                .andExpect(status().isCreated()).andReturn();
        return (MockHttpSession) result.getRequest().getSession(false);
    }

    private String attempt(String grade, String style, boolean sent) {
        return "{\"climbedOn\":\"2026-08-12\",\"location\":\"Paklenica\",\"sector\":\"Klanci\",\"routeName\":\"La Testa\",\"grade\":\"" + grade + "\",\"lengthMeters\":25,\"style\":\"" + style + "\",\"sent\":" + sent + ",\"rpe\":8}";
    }
}
