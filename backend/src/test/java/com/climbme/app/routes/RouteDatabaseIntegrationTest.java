package com.climbme.app.routes;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ClimbMeApplication.class, properties = {"spring.datasource.url=jdbc:h2:mem:climbme_routes;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE", "spring.datasource.username=sa", "spring.datasource.password=", "spring.jpa.hibernate.ddl-auto=validate"})
@AutoConfigureMockMvc
@Transactional
class RouteDatabaseIntegrationTest {
    @Autowired private MockMvc mvc;

    @Test void routesAreDerivedPerOwnerAndCanBeExplicitlyMarkedAbandoned() throws Exception {
        MockHttpSession owner = register("routes-owner@example.com");
        MockHttpSession other = register("routes-other@example.com");
        create(owner, "La Testa", "REDPOINT", false);
        create(owner, "La Testa", "REDPOINT", true);
        create(other, "Other Route", "ONSIGHT", true);
        mvc.perform(get("/api/routes").session(owner)).andExpect(status().isOk()).andExpect(jsonPath("$[0].routeName").value("La Testa")).andExpect(jsonPath("$[0].totalAttempts").value(2)).andExpect(jsonPath("$[0].status").value("SENT"));
        mvc.perform(post("/api/routes/abandon").session(owner).with(csrf()).contentType(MediaType.APPLICATION_JSON).content("{\"location\":\"Paklenica\",\"sector\":\"Klanci\",\"routeName\":\"La Testa\"}")).andExpect(status().isNoContent());
        mvc.perform(get("/api/routes").session(owner)).andExpect(jsonPath("$[0].status").value("ABANDONED"));
        mvc.perform(get("/api/routes").session(other)).andExpect(jsonPath("$[0].routeName").value("Other Route"));
    }
    private MockHttpSession register(String email) throws Exception { MvcResult result = mvc.perform(post("/api/auth/register").with(csrf()).contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"" + email + "\",\"password\":\"safe-password-123\"}")).andReturn(); return (MockHttpSession) result.getRequest().getSession(false); }
    private void create(MockHttpSession session, String route, String style, boolean sent) throws Exception { mvc.perform(post("/api/attempts").session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON).content("{\"climbedOn\":\"2026-08-12\",\"location\":\"Paklenica\",\"sector\":\"Klanci\",\"routeName\":\"" + route + "\",\"grade\":\"7a\",\"style\":\"" + style + "\",\"sent\":" + sent + "}")).andExpect(status().isCreated()); }
}
